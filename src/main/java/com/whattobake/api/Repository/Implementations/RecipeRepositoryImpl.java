package com.whattobake.api.Repository.Implementations;

import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Dto.InfoDto.RecipeInfo;
import com.whattobake.api.Enum.RecipeOrder;
import com.whattobake.api.Mapers.RecipeMapper;
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
    private final RecipeMapper recipeMapper;

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
                .bindAll(Map.of("tags",recipeFilters.getTags(), "tags_size", recipeFilters.getTags().size()))
                .fetchAs(RecipeInfo.class)
                .mappedBy((ts,r)-> RecipeInfo.builder()
                                .count(r.get("count").asLong())
                                .countWithFilters(r.get("countWithFilters").asLong()).build()
                ).first();
    }

    @SuppressWarnings("unused")
    public Flux<Recipe> findAll(RecipeFilters recipeFilters) {
        String q = """
                MATCH (recipe:RECIPE)
                """+ recipeFilters.getTagOption().getValue() +"""
                CALL { WITH recipe MATCH (recipe)-[:NEEDS]->(p:PRODUCT) RETURN COUNT(p) AS prodCount }
                CALL { WITH recipe MATCH (recipe)-[:NEEDS]->(p:PRODUCT) WHERE ID(p) IN $products RETURN COUNT(p) AS HasProducts }
                CALL { WITH recipe MATCH (recipe)<-[l:LIKES]-(:USER) RETURN COUNT(l) as likes }
                RETURN""" + RecipeMapper.RETURN + """
                    ,HasProducts ,(prodCount - HasProducts) AS HasNotProducts, prodCount AS AllProducts,
                    CASE prodCount
                        WHEN 0 THEN 0
                        ELSE (HasProducts * 100) / prodCount
                    END AS Progress
                """;
        if(!recipeFilters.getOrderList().isEmpty()){
            q +=" ORDER BY " + recipeFilters.getOrderList().stream()
                    .map(RecipeOrder::getValue)
                    .collect(Collectors.joining(","));
        }
        q += (" SKIP " + RECIPES_PER_PAGE * recipeFilters.getPage() + " LIMIT " + RECIPES_PER_PAGE);
        return recipeMapper.resultAsFlux(recipeMapper.getMapperQueryNoAddon(q),Map.of(
                "products",recipeFilters.getProducts(),
                "tags",recipeFilters.getTags(),
                "tags_size",recipeFilters.getTags().size()
        ));
    }

    @SuppressWarnings("unused")
    public Mono<Recipe> findOne(Long id) {
        String q = "MATCH (recipe:RECIPE) WHERE ID(recipe) = $id RETURN";
        return recipeMapper.resultAsMono(recipeMapper.getMapperQuery(q),Map.of("id",id));
    }

    @SuppressWarnings("unused")
    public Mono<Recipe> create(Map<String, Object> recipe) {
        String q = """
                CREATE (recipe:RECIPE{title:$title,link:$link,image:$image,create_date: datetime()})
                WITH recipe
                CALL {
                    WITH recipe
                    MATCH (product:PRODUCT) WHERE ID(product) IN $products
                    MERGE (recipe)-[:NEEDS]->(product)
                }
                CALL {
                    WITH recipe
                    MATCH (tag:TAG) WHERE ID(tag) IN $tags
                    MERGE (recipe)-[:HAS_TAG]->(tag)
                }
                RETURN""";
        return recipeMapper.resultAsMono(recipeMapper.getMapperQuery(q),recipe);
    }

    @SuppressWarnings("unused")
    public Mono<Recipe> update(Map<String, Object> recipe) {
        String q = """
            MATCH (recipe:RECIPE) WHERE ID(recipe) = $id
            SET recipe.title = $title
            SET recipe.link = $link
            SET recipe.image = $image
            WITH recipe
            CALL { WITH recipe MATCH (recipe)-[r:NEEDS]->(:PRODUCT) DELETE r }
            CALL { WITH recipe
                MATCH (product:PRODUCT) WHERE ID(product) IN $products
                MERGE (recipe)-[:NEEDS]->(product)
            }
            CALL { WITH recipe MATCH (recipe)-[r:HAS_TAG]->(:TAG) DELETE r }
            CALL { WITH recipe
                MATCH (tag:TAG) WHERE ID(tag) IN $tags
                MERGE (recipe)-[:HAS_TAG]->(tag)
            }
            RETURN""";
        return recipeMapper.resultAsMono(recipeMapper.getMapperQuery(q), recipe);
    }
}
