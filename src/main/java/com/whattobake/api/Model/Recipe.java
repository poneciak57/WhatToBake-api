package com.whattobake.api.Model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Transient;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.util.List;

@Node("RECIPE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Recipe {
    @Id
    @GeneratedValue
    private Long id;
    private String title;
    private String link;
    private String image;
    private Long likes;

    @Relationship("NEEDS")
    private List<Product> products;
    @Relationship("HAS_TAG")
    private List<Tag> tags;
}
