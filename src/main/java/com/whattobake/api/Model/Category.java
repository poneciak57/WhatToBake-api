package com.whattobake.api.Model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.time.Instant;

@Node("CATEGORY")
@Data
@Builder
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @NotEmpty
    @Size(max = 30)
    private String name;

    @NotNull
    @NotEmpty
    private String icon;

    @CreatedDate
    @Property(name = "creation_date")
    private Instant creationDate;

}
