package com.loan24.persistence.service;

import org.springframework.beans.BeanWrapperImpl;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Service
@SuppressWarnings("unchecked")
public class EntityDataMapper {

    @PersistenceContext
    private EntityManager entityManager;

    public <T> T mapObject(final T sourceObject, final T targetObject, Class<T> clazz) {
        final var targetWrapper = new BeanWrapperImpl(targetObject);
        final var sourceWrapper = new BeanWrapperImpl(sourceObject);
        final var en = entityManager.getMetamodel().entity(clazz);
        en.getAttributes().forEach(attribute -> {
            if (!attribute.getName().equalsIgnoreCase("id"))
                targetWrapper.setPropertyValue(attribute.getName(), sourceWrapper.getPropertyValue(attribute.getName()));
        });
        return (T) targetWrapper.getWrappedInstance();
    }
}
