package com.loan24.graphql.instrumentation;

import com.loan24.persistence.filter.FilterConfig;
import com.loan24.user.authorization.domain.ActionName;
import graphql.execution.instrumentation.SimpleInstrumentation;
import graphql.execution.instrumentation.parameters.InstrumentationFieldFetchParameters;
import graphql.schema.DataFetcher;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.AbstractMap;
import java.util.Map;

@Component
@AllArgsConstructor
public class FilterInstrumentation extends SimpleInstrumentation {

    private final FilterConfig filterConfig;
    private static final Map<String, String> ENTITY_NAME = Map.ofEntries(
            new AbstractMap.SimpleEntry<>("AppUser", "user"),
            new AbstractMap.SimpleEntry<>("AppUsers", "user"),
            new AbstractMap.SimpleEntry<>("Project", "project"),
            new AbstractMap.SimpleEntry<>("Projects", "project"),
            new AbstractMap.SimpleEntry<>("Task", "task"),
            new AbstractMap.SimpleEntry<>("Tasks", "task"),
            new AbstractMap.SimpleEntry<>("House", "house"),
            new AbstractMap.SimpleEntry<>("Houses", "house"),
            new AbstractMap.SimpleEntry<>("Street", "street"),
            new AbstractMap.SimpleEntry<>("Streets", "street"),
            new AbstractMap.SimpleEntry<>("BOQ", "boq"),
            new AbstractMap.SimpleEntry<>("BOQs", "boq"),
            new AbstractMap.SimpleEntry<>("Payment", "payment"),
            new AbstractMap.SimpleEntry<>("Payments", "payment"),
            new AbstractMap.SimpleEntry<>("SubConstructor", "sub_constructor"),
            new AbstractMap.SimpleEntry<>("SubConstructors", "sub_constructor")
    );

    @Override
    public DataFetcher<?> instrumentDataFetcher(DataFetcher<?> dataFetcher, InstrumentationFieldFetchParameters parameters) {
        final var fieldName = parameters.getField().getName();
        var entityName = getEntityName(fieldName);
        if (entityName != null) {
            filterConfig.configureFilter(ActionName.READ, entityName);
        }
        return super.instrumentDataFetcher(dataFetcher, parameters);
    }

    private String getEntityName(String name) {
        if (ENTITY_NAME.containsKey(name)) return ENTITY_NAME.get(name);
        return null;
    }
}
