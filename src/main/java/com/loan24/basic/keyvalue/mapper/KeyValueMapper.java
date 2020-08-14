package com.loan24.basic.keyvalue.mapper;

import com.loan24.basic.keyvalue.domain.KeyValue;
import com.loan24.basic.keyvalue.dto.KeyValueDTO;
import com.loan24.persistence.mapper.EntityMapper;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface KeyValueMapper extends EntityMapper<KeyValueDTO, KeyValue> {
}