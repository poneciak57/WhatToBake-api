package com.whattobake.api.Model;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
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

    @NotNull
    @NotEmpty
    @Size(max = 30)
    private String title;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String link;

    @NotNull
    @NotEmpty
    @Size(max = 50)
    private String image;

    private Long likes;

    @Nullable
    @Relationship("NEEDS")
    private List<Product> products;

    @Nullable
    @Relationship("HAS_TAG")
    private List<Tag> tags;
}
