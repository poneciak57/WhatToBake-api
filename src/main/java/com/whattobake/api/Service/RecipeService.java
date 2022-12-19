package com.whattobake.api.Service;

import com.whattobake.api.Dto.RecipeFilters;
import com.whattobake.api.Dto.RecipeInsertRequest;
import com.whattobake.api.Dto.RecipeUpdateRequest;
import com.whattobake.api.Enum.RecipeProductOrder;
import com.whattobake.api.Model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.ReactiveNeo4jTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

    private final Long RECIPES_PER_PAGE = 10L;
    private final ReactiveNeo4jTemplate template;
//    private final ReactiveNeo4jClient client;
//    private final RecipeRepository recipeRepository;

    public Mono<Recipe> updateRecipe(RecipeUpdateRequest recipeUpdateRequest){
        String q = """
            MATCH (recipe:RECIPE) WHERE ID(recipe) = $id
            SET recipe.title = $title
            SET recipe.link = $link
            SET recipe.image = $image
            WITH recipe
            CALL{
                WITH recipe
                MATCH (recipe)-[r:NEEDS]->(:PRODUCT)
                DELETE r
            }
            WITH recipe
            CALL{
                WITH recipe
                MATCH (product:PRODUCT)-[pc_rel:HAS_CATEGORY]->(category:CATEGORY) WHERE ID(product) IN $products
                MERGE (recipe)-[rp_rel:NEEDS]->(product)
                RETURN COLLECT(product) AS product, COLLECT(rp_rel) AS rp_rel
                    ,COLLECT(category) AS category, COLLECT(pc_rel) AS pc_rel
            }
            WITH *
            RETURN recipe,product,category,pc_rel,rp_rel
        """;
        return template.findOne(q,Map.of(
                "id", recipeUpdateRequest.getId(),
                "title", recipeUpdateRequest.getTitle(),
                "link", recipeUpdateRequest.getLink(),
                "image", recipeUpdateRequest.getImage(),
                "products", recipeUpdateRequest.getProducts()
        ),Recipe.class);
    }
    public Mono<Void> deleteRecipe(Long id){
        return template.deleteById(id,Recipe.class);
    }
    public Mono<Recipe> newRecipe(RecipeInsertRequest recipeInsertRequest){
        String q = """
                CREATE (recipe:RECIPE{title:$title,link:$link,image:$image})
                WITH recipe
                CALL{
                    WITH recipe
                    MATCH (product:PRODUCT)-[pc_rel:HAS_CATEGORY]->(category:CATEGORY) WHERE ID(product) IN $products
                    MERGE (recipe)-[rp_rel:NEEDS]->(product)
                    RETURN COLLECT(product) AS product, COLLECT(rp_rel) AS rp_rel
                        ,COLLECT(category) AS category, COLLECT(pc_rel) AS pc_rel
                }
                WITH *
                RETURN recipe,product,category,pc_rel,rp_rel
                """;
        return template.findOne(q, Map.of(
                "title", recipeInsertRequest.getTitle(),
                "link", recipeInsertRequest.getLink(),
                "image", recipeInsertRequest.getImage(),
                "products", recipeInsertRequest.getProducts()
        ),Recipe.class);
    }
    public Flux<Recipe> getAllRecipes(RecipeFilters recipeFilters){
        String q = """
                MATCH (recipe:RECIPE)
                CALL{
                    WITH recipe
                    MATCH(recipe)-[rp_rel:NEEDS]->(product:PRODUCT)-[pc_rel:HAS_CATEGORY]->(category:CATEGORY)
                    RETURN COLLECT(product) AS products, COLLECT(category) AS categories
                    ,COLLECT(rp_rel) AS rp_rels, COLLECT(pc_rel) AS pc_rels, COUNT(product) AS prodCount
                }
                CALL{
                    WITH recipe
                    MATCH (recipe:RECIPE)-[:NEEDS]->(p:PRODUCT)
                    WHERE ID(p) IN $products
                    RETURN COUNT(p) AS HasProducts
                }
                RETURN recipe, products,categories, rp_rels, pc_rels
                    ,HasProducts , (prodCount - HasProducts) AS HasNotProducts
                """;
        if(!recipeFilters.getProductOrder().isEmpty()){
            q +=" ORDER BY " + recipeFilters.getProductOrder().stream()
                    .map(RecipeProductOrder::getValue)
                    .collect(Collectors.joining(","));
        }
        q += (" SKIP " + RECIPES_PER_PAGE * recipeFilters.getPage() + " LIMIT " + RECIPES_PER_PAGE);
        return template.findAll(q, Map.of("products", recipeFilters.getProducts()),Recipe.class);
    }

    public Mono<Recipe> getOneById(Long id) {
        return template.findById(id,Recipe.class);
    }
}
