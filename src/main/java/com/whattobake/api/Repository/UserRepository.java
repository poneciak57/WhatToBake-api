package com.whattobake.api.Repository;

import com.whattobake.api.Model.User;
import org.springframework.data.neo4j.repository.ReactiveNeo4jRepository;
import org.springframework.stereotype.Repository;

@Repository
@Deprecated
public interface UserRepository extends ReactiveNeo4jRepository<User,String> {
}
