package com.whattobake.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nullable;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;
import org.springframework.data.neo4j.core.schema.Relationship;

import java.time.Instant;
import java.util.List;

@Node("RECIPE")
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
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

    @ReadOnlyProperty
    private Long likes;

    @Nullable
    @Relationship("NEEDS")
    private List<@Valid Product> products;

    @Nullable
    @Relationship("HAS_TAG")
    private List<@Valid Tag> tags;

    @CreatedDate
    @JsonProperty("creation_date")
    @Property(name = "creation_date")
    private Instant creationDate;

}