package com.chs.persistence.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.core.GenericTypeResolver;

import java.util.function.Function;

@SuppressWarnings("unchecked")
public abstract class DataMapper<Entity, Data> implements Function<Entity, Data> {

    protected final ModelMapper modelMapper;
    private final Class<Entity> entityClass;
    private final Class<Data> dataClass;

    protected DataMapper() {
        final var typeArguments = GenericTypeResolver.resolveTypeArguments(getClass(), DataMapper.class);
        if (typeArguments == null || typeArguments.length != 2) {
            throw new RuntimeException("Type arguments in data mapper does not exist");
        }
        this.entityClass = (Class<Entity>) typeArguments[0];
        this.dataClass = (Class<Data>) typeArguments[1];
        this.modelMapper = new ModelMapper();
        this.modelMapper.getConfiguration().setAmbiguityIgnored(true);
    }

    @Override
    public Data apply(final Entity entity) {
        return modelMapper.map(entity, dataClass);
    }

    public Entity map(final Data data) {
        return modelMapper.map(data, entityClass);
    }
}
