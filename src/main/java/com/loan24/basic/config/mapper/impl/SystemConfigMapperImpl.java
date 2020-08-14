package com.loan24.basic.config.mapper.impl;

import com.loan24.basic.config.domain.SystemConfig;
import com.loan24.basic.config.dto.SystemConfigDTO;
import com.loan24.basic.config.mapper.SystemConfigMapper;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class SystemConfigMapperImpl implements SystemConfigMapper {
    @Override
    public SystemConfig toEntity(SystemConfigDTO dto) {
        SystemConfig entity = new SystemConfig();
        entity.setCode(dto.getCode());
        entity.setValue(dto.getValue());
        return entity;
    }

    @Override
    public SystemConfigDTO toDto(SystemConfig entity) {
        SystemConfigDTO dto = new SystemConfigDTO();
        dto.setCode(entity.getCode());
        dto.setValue(entity.getValue());
        return dto;
    }

    @Override
    public List<SystemConfig> toEntityList(List<SystemConfigDTO> dtoList) {
        return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
    }

    @Override
    public List<SystemConfigDTO> toDtoList(List<SystemConfig> entityList) {
        return entityList.stream().map(this::toDto).collect(Collectors.toList());
    }
}