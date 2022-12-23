package com.whattobake.api.Service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Dto.InfoDto.RecipeInfo;
import com.whattobake.api.Dto.InsertDto.RecipeInsertRequest;
import com.whattobake.api.Dto.UpdateDto.RecipeUpdateRequest;
import com.whattobake.api.Enum.RecipeProductOrder;
import com.whattobake.api.Mapers.RecipeMaper;
import com.whattobake.api.Model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.data.neo4j.core.ReactiveNeo4jTemplate;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RecipeService {

//    TODO przepisac wszystko na maper dodac tagi
    private final Long RECIPES_PER_PAGE = 10L;
    private final ReactiveNeo4jTemplate template;
    private final ReactiveNeo4jClient client;
    private final RecipeMaper recipeMaper;
//    private final RecipeRepository recipeRepository;
    public Mono<RecipeInfo> info(RecipeFilters recipeFilters){
        String q = """
            CALL { MATCH (r:recipe) RETURN COUNT(r) AS countAll }
            CALL { MATCH (r:recipe) RETURN COUNT(r) AS countWithFilters }
            RETURN {
                count: countAll,
                countWithFilters: countWithFilters
            }""";
        ObjectMapper mapper = new ObjectMapper();
        return client.query(q)
//                .bindAll(params)
                .fetchAs(RecipeInfo.class)
                .mappedBy((ts,r)-> mapper.convertValue(r.get("recipe").asMap(), RecipeInfo.class)
                ).first();
    }

    public Mono<Recipe> updateRecipe(RecipeUpdateRequest recipeUpdateRequest){
        String q = """
            MATCH (recipe:RECIPE) WHERE ID(recipe) = $id
            SET recipe.title = $title
            SET recipe.link = $link
            SET recipe.image = $image
            WITH recipe
            CALL{ WITH recipe MATCH (recipe)-[r:NEEDS]->(:PRODUCT) DELETE r }
            WITH recipe
            CALL{ WITH recipe
                MATCH (product:PRODUCT)-[pc_rel:HAS_CATEGORY]->(category:CATEGORY) WHERE ID(product) IN $products
                MERGE (recipe)-[rp_rel:NEEDS]->(product)
            }
            RETURN""" + RecipeMaper.RETURN;
        return recipeMaper.resultAsRecipe(q,Map.of(
                "id", recipeUpdateRequest.getId(),
                "title", recipeUpdateRequest.getTitle(),
                "link", recipeUpdateRequest.getLink(),
                "image", recipeUpdateRequest.getImage(),
                "products", recipeUpdateRequest.getProducts()
        ));
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
                }
                RETURN"""+RecipeMaper.RETURN;
        return recipeMaper.resultAsRecipe(q,Map.of(
                "title", recipeInsertRequest.getTitle(),
                "link", recipeInsertRequest.getLink(),
                "image", recipeInsertRequest.getImage(),
                "products", recipeInsertRequest.getProducts()
        ));
    }
    public Flux<Recipe> getAllRecipes(RecipeFilters recipeFilters){
        String q = """
                MATCH (recipe:RECIPE)
                CALL{ WITH recipe MATCH(recipe)-[rp_rel:NEEDS]->(product:PRODUCT) RETURN COUNT(product) AS prodCount }
                CALL{ WITH recipe MATCH (recipe:RECIPE)-[:NEEDS]->(p:PRODUCT) WHERE ID(p) IN $products RETURN COUNT(p) AS HasProducts }
                RETURN""" + RecipeMaper.RETURN + """
                    ,HasProducts , (prodCount - HasProducts) AS HasNotProducts
                """;
        if(!recipeFilters.getProductOrder().isEmpty()){
            q +=" ORDER BY " + recipeFilters.getProductOrder().stream()
                    .map(RecipeProductOrder::getValue)
                    .collect(Collectors.joining(","));
        }
        q += (" SKIP " + RECIPES_PER_PAGE * recipeFilters.getPage() + " LIMIT " + RECIPES_PER_PAGE);
        return recipeMaper.resultAsRecipes(q,Map.of("products",recipeFilters.getProducts()));
    }

    public Mono<Recipe> getOneById(Long id) {
        String q = """
            MATCH (recipe:RECIPE) WHERE ID(recipe) = $id
            RETURN"""+ RecipeMaper.RETURN;
        ObjectMapper mapper = new ObjectMapper();
        return recipeMaper.resultAsRecipe(q,Map.of("id",id));
    }
}
