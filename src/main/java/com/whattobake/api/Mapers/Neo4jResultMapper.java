package com.whattobake.api.Mapers;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.neo4j.core.ReactiveNeo4jClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.lang.reflect.ParameterizedType;
import java.util.Map;

public class Neo4jResultMapper<Model> {

    private final Class<Model> modelClass;

    private final ReactiveNeo4jClient client;


    public Neo4jResultMapper(ReactiveNeo4jClient client) {
        this.client = client;
        ParameterizedType parameterizedType = (ParameterizedType) getClass()
                .getGenericSuperclass();
        this.modelClass = (Class<Model>) parameterizedType.getActualTypeArguments()[0];
    }

    public Mono<Model> resultAsMono(MapperQuery query, Map<String,Object> params){
        ObjectMapper mapper = new ObjectMapper();
        return client.query(query.getQuery())
                .bindAll(params)
                .fetchAs(modelClass)
                .mappedBy((ts,r)-> mapper.convertValue(r.get(query.getRowName()).asMap(), modelClass)
                ).first();
    }
    public Flux<Model> resultAsFlux(MapperQuery query, Map<String,Object> params){
        ObjectMapper mapper = new ObjectMapper();
        return client.query(query.getQuery())
                .bindAll(params)
                .fetchAs(modelClass)
                .mappedBy((ts,r)-> mapper.convertValue(r.get(query.getRowName()).asMap(), modelClass)
                ).all();
    }
}