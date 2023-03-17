package com.whattobake.api.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
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
import org.springframework.data.neo4j.core.schema.*;

import java.time.Instant;

@Node("PRODUCT")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Product {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 30)
    private String name;

    @Nullable
    @Valid
    @Relationship("HAS_CATEGORY")
    private Category category;

    @CreatedDate
    @JsonProperty("creation_date")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
    @Property(name = "creation_date")
    private Instant creationDate;

}
