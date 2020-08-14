package com.loan24.graphql;

import com.introproventures.graphql.jpa.query.autoconfigure.GraphQLJpaQueryProperties;
import com.introproventures.graphql.jpa.query.autoconfigure.GraphQLSchemaConfigurer;
import com.introproventures.graphql.jpa.query.autoconfigure.GraphQLShemaRegistration;
import com.introproventures.graphql.jpa.query.schema.impl.GraphQLJpaSchemaBuilder;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;

import javax.persistence.EntityManager;

@Configuration
@AllArgsConstructor
public class GraphQLSchemaConfig implements GraphQLSchemaConfigurer {

    private final EntityManager entityManager;
    private final GraphQLJpaQueryProperties properties;

    @Override
    public void configure(GraphQLShemaRegistration registry) {
        registry.register(
                new GraphQLJpaSchemaBuilder(entityManager)
                        .name("GraphQLApi")
                        .useDistinctParameter(properties.isUseDistinctParameter())
                        .build()
        );
    }
}
