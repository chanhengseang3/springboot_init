package com.loan24.graphql;

import com.loan24.graphql.instrumentation.FilterInstrumentation;
import graphql.GraphQLContext;
import graphql.execution.instrumentation.Instrumentation;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.context.annotation.RequestScope;

import javax.servlet.http.HttpServletRequest;
import java.util.function.Supplier;

@Configuration
@AllArgsConstructor
public class GraphQLConfig {

    private final FilterInstrumentation filterInstrumentation;

    @Bean
    @RequestScope
    public Supplier<GraphQLContext> graphqlContext(HttpServletRequest request) {
        return () -> GraphQLContext.newContext()
                .of("request", request)
                .build();
    }

    @Bean
    @RequestScope
    public Supplier<Instrumentation> instrumentation(HttpServletRequest request) {
        return () -> filterInstrumentation;
    }
}
