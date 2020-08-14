package com.loan24.basic.config.mapper;

import com.loan24.basic.config.domain.SystemConfig;
import com.loan24.basic.config.dto.SystemConfigDTO;
import com.loan24.persistence.mapper.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface SystemConfigMapper extends EntityMapper<SystemConfigDTO, SystemConfig> {
}