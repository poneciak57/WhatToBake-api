package com.whattobake.api.Model;

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
import org.springframework.data.neo4j.core.schema.*;

import java.time.LocalDateTime;
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

    @ReadOnlyProperty
    private Long likes;

    @Nullable
    @Relationship("NEEDS")
    private List<@Valid Product> products;

    @Nullable
    @Relationship("HAS_TAG")
    private List<@Valid Tag> tags;

    @CreatedDate
    @Property(name = "creation_date")
    private LocalDateTime creationDate;
}
