package com.whattobake.api.Repository;

import com.whattobake.api.Model.Tag;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TagRepository extends ReactiveNeo4jRepository<Tag,Long> {
}
