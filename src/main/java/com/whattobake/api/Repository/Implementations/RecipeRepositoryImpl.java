package com.whattobake.api.Repository.Implementations;

import com.whattobake.api.Dto.FilterDto.RecipeFilters;
import com.whattobake.api.Dto.InfoDto.RecipeInfo;
import com.whattobake.api.Enum.RecipeOrder;
import com.whattobake.api.Mapers.RecipeMapper;
import com.whattobake.api.Model.Recipe;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class RecipeRepositoryImpl {

    @Value("${w2b.recipes.pageCount}")
    private Long RECIPES_PER_PAGE;

    private final ReactiveNeo4jClient client;

    private final RecipeMapper recipeMapper;

    @SuppressWarnings("unused")
    public Mono<RecipeInfo> info(RecipeFilters recipeFilters) {
        String q = """
                MATCH (recipe:RECIPE)
                CALL { MATCH (recipe:RECIPE) RETURN COUNT(recipe) as countAll }
                CALL { WITH recipe MATCH (recipe)-[:NEEDS]->(p:PRODUCT) RETURN COUNT(p) AS prodCount }
                CALL { WITH recipe MATCH (recipe)-[:NEEDS]->(p:PRODUCT) WHERE ID(p) IN $key_products RETURN COUNT(p) AS HasKeyProducts }
                WITH *
                """ + recipeFilters.getTagOption().getValue() + """
                AND HasKeyProducts = $key_products_count
                AND prodCount >= $min_products AND prodCount <= $max_products
                AND coalesce(apoc.coll.avg([(recipe)<-[rate:RATING]-(:USER) | rate.stars]), 0) >= $rating
                RETURN countAll as count, COUNT(recipe) as countWithFilters
                """;
        return client.query(q)
                .bindAll(Map.of(
                        "tags",recipeFilters.getTags(),
                        "tags_size", recipeFilters.getTags().size(),
                        "rating", recipeFilters.getRating(),
                        "min_products", recipeFilters.getMinProducts(),
                        "max_products", recipeFilters.getMaxProducts(),
                        "key_products", recipeFilters.getKeyProducts(),
                        "key_products_count", recipeFilters.getKeyProducts().size()
                ))
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
                CALL { WITH recipe MATCH (recipe)-[:NEEDS]->(p:PRODUCT) WHERE ID(p) IN $key_products RETURN COUNT(p) AS HasKeyProducts }
                CALL { WITH recipe MATCH (recipe)<-[l:LIKES]-(:USER) RETURN COUNT(l) as likes }
                WITH *
                WHERE coalesce(apoc.coll.avg([(recipe)<-[rate:RATING]-(:USER) | rate.stars]), 0) >= $rating
                AND prodCount >= $min_products AND prodCount <= $max_products
                AND HasKeyProducts = $key_products_count
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

        return recipeMapper.resultAsFlux(recipeMapper.getMapperQueryNoAddon(q), Map.of(
                "products",recipeFilters.getProducts(),
                "tags",recipeFilters.getTags(),
                "tags_size",recipeFilters.getTags().size(),
                "rating", recipeFilters.getRating(),
                "min_products", recipeFilters.getMinProducts(),
                "max_products", recipeFilters.getMaxProducts(),
                "key_products", recipeFilters.getKeyProducts(),
                "key_products_count", recipeFilters.getKeyProducts().size()
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
                MERGE (recipe:RECIPE{title:$title,link:$link,image:$image})
                ON CREATE
                    SET recipe.creation_date = datetime()
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
