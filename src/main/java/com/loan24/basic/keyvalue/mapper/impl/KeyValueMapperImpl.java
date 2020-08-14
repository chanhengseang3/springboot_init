package com.loan24.basic.keyvalue.mapper.impl;

import com.loan24.basic.keyvalue.domain.KeyValue;
import com.loan24.basic.keyvalue.dto.KeyValueDTO;
import com.loan24.basic.keyvalue.mapper.KeyValueMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class KeyValueMapperImpl implements KeyValueMapper {
    @Override
    public KeyValue toEntity(KeyValueDTO dto) {
        KeyValue entity = new KeyValue();
        entity.setGroup(dto.getGroup());
        entity.setKey(dto.getKey());
        entity.setValue(dto.getValue());
        entity.setRemarks(dto.getRemarks());
        return entity;
    }

    @Override
    public KeyValueDTO toDto(KeyValue entity) {
        KeyValueDTO dto = new KeyValueDTO();
        dto.setGroup(entity.getGroup());
        dto.setKey(entity.getKey());
        dto.setValue(entity.getValue());
        dto.setRemarks(entity.getRemarks());
        return dto;
    }

    @Override
    public List<KeyValue> toEntityList(List<KeyValueDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<KeyValueDTO> toDtoList(List<KeyValue> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}