package com.whattobake.api.Repository.Implementations;

import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Dto.InfoDto.RecipeInfo;
import com.whattobake.api.Enum.RecipeProductOrder;
import com.whattobake.api.Mapers.MapperQuery;
import com.whattobake.api.Mapers.RecipeMaper;
import com.whattobake.api.Model.Recipe;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class RecipeRepositoryImpl {

    @Value("${w2b.recipes.pageCount}")
    private Long RECIPES_PER_PAGE;
    private final ReactiveNeo4jClient client;
    private final RecipeMaper recipeMaper;

    @SuppressWarnings("unused")
    public Flux<Recipe> getLikedRecipes(String pbUid){
        String q = """
            MATCH (u:USER)-[:LIKES]->(recipe:RECIPE)
            WHERE u.pbId = $pbId
            RETURN""";
        return recipeMaper.resultAsFlux(recipeMaper.getMapperQuery(q),Map.of("pbId",pbUid));
    }
    @SuppressWarnings("unused")
    public Mono<RecipeInfo> info(RecipeFilters recipeFilters) {
        String q = """
            CALL { MATCH (recipe:RECIPE) RETURN COUNT(recipe) AS countAll }
            CALL {
                MATCH (recipe:RECIPE)
                """ + recipeFilters.getTagOption().getValue() + """
                RETURN COUNT(recipe) AS countWithFilters
            }
            RETURN countAll as count, countWithFilters
            """;
        return client.query(q)
                .bindAll(Map.of(
                        "tags",recipeFilters.getTags(),
                        "tags_size", recipeFilters.getTags().size()
                ))
                .fetchAs(RecipeInfo.class)
                .mappedBy((ts,r)->
                        RecipeInfo.builder()
                                .count(r.get("count").asLong())
                                .countWithFilters(r.get("countWithFilters").asLong())
                                .build()
                ).first();
    }

    @SuppressWarnings("unused")
    public Flux<Recipe> findAll(RecipeFilters recipeFilters) {
        String q = """
                MATCH (recipe:RECIPE)
                """+ recipeFilters.getTagOption().getValue() +"""
                CALL{ WITH recipe MATCH (recipe)-[:NEEDS]->(p:PRODUCT) RETURN COUNT(p) AS prodCount }
                CALL{ WITH recipe MATCH (recipe)-[:NEEDS]->(p:PRODUCT) WHERE ID(p) IN $products RETURN COUNT(p) AS HasProducts }
                CALL { WITH recipe MATCH (recipe)<-[l:LIKES]-(:USER) RETURN COUNT(l) as likes }
                RETURN""" + RecipeMaper.RETURN + """
                    ,HasProducts ,(prodCount - HasProducts) AS HasNotProducts, prodCount AS AllProducts,((HasProducts*100)/prodCount) AS Progress
                """;
        if(!recipeFilters.getProductOrder().isEmpty()){
            q +=" ORDER BY " + recipeFilters.getProductOrder().stream()
                    .map(RecipeProductOrder::getValue)
                    .collect(Collectors.joining(",")) + ", recipe.id ASC ";
        }
        q += (" SKIP " + RECIPES_PER_PAGE * recipeFilters.getPage() + " LIMIT " + RECIPES_PER_PAGE);
        return recipeMaper.resultAsFlux(MapperQuery.builder().query(q).rowName(RecipeMaper.ROW_NAME).build(),Map.of(
                "products",recipeFilters.getProducts(),
                "tags",recipeFilters.getTags(),
                "tags_size",recipeFilters.getTags().size()
        ));
    }

    @SuppressWarnings("unused")
    public Mono<Recipe> findOne(Long id) {
        String q = "MATCH (recipe:RECIPE) WHERE ID(recipe) = $id RETURN";
        return recipeMaper.resultAsMono(recipeMaper.getMapperQuery(q),Map.of("id",id));
    }

    @SuppressWarnings("unused")
    public Mono<Recipe> create(Map<String, Object> recipe) {
        String q = """
                CREATE (recipe:RECIPE{title:$title,link:$link,image:$image})
                WITH recipe
                CALL{
                    WITH recipe
                    MATCH (product:PRODUCT) WHERE ID(product) IN $products
                    MERGE (recipe)-[:NEEDS]->(product)
                }
                CALL{
                    WITH recipe
                    MATCH (tag:TAG) WHERE ID(tag) IN $tags
                    MERGE (recipe)-[:HAS_TAG]->(tag)
                }
                RETURN""";
        return recipeMaper.resultAsMono(recipeMaper.getMapperQuery(q),recipe);
    }

    @SuppressWarnings("unused")
    public Mono<Recipe> update(Map<String, Object> recipe) {
        String q = """
            MATCH (recipe:RECIPE) WHERE ID(recipe) = $id
            SET recipe.title = $title
            SET recipe.link = $link
            SET recipe.image = $image
            WITH recipe
            CALL{ WITH recipe MATCH (recipe)-[r:NEEDS]->(:PRODUCT) DELETE r }
            CALL{ WITH recipe
                MATCH (product:PRODUCT) WHERE ID(product) IN $products
                MERGE (recipe)-[:NEEDS]->(product)
            }
            CALL{ WITH recipe MATCH (recipe)-[r:HAS_TAG]->(:TAG) DELETE r }
            CALL{ WITH recipe
                MATCH (tag:TAG) WHERE ID(tag) IN $tags
                MERGE (recipe)-[:HAS_TAG]->(tag)
            }
            RETURN""";
        return recipeMaper.resultAsMono(recipeMaper.getMapperQuery(q), recipe);
    }
}