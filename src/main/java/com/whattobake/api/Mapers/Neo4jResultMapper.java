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

    private final String ret;
    private final String row_name;


    public Neo4jResultMapper(ReactiveNeo4jClient client,String ret,String row_name) {
        this.client = client;
        ParameterizedType parameterizedType = (ParameterizedType) getClass()
                .getGenericSuperclass();
        this.modelClass = (Class<Model>) parameterizedType.getActualTypeArguments()[0];
        this.ret = ret;
        this.row_name = row_name;
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

    public MapperQuery getMapperQuery(String query){
        return MapperQuery.builder()
                .query(query + this.ret)
                .rowName(this.row_name)
                .build();
    }
}