package com.loan24.basic.config.service.impl;

import com.loan24.basic.config.dao.SystemConfigRepository;
import com.loan24.basic.config.domain.SystemConfig;
import com.loan24.basic.config.dto.SystemConfigDTO;
import com.loan24.basic.config.mapper.SystemConfigMapper;
import com.loan24.basic.config.service.SystemConfigService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class SystemConfigServiceImpl implements SystemConfigService {
    private final SystemConfigMapper mapper;
    private final SystemConfigRepository repository;

    public SystemConfigServiceImpl(SystemConfigMapper mapper, SystemConfigRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public SystemConfigDTO save(SystemConfigDTO dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public void save(List<SystemConfigDTO> dtos) {
        repository.saveAll(mapper.toEntityList(dtos));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<SystemConfigDTO> findById(Long id) {
        Optional<SystemConfig> entityOptional = repository.findById(id);
        return entityOptional.map(entity -> Optional.ofNullable(mapper.toDto(entity))).orElse(null);
    }

    @Override
    public List<SystemConfigDTO> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public Page<SystemConfigDTO> findAll(Pageable pageable) {
        Page<SystemConfig> entityPage = repository.findAll(pageable);
        List<SystemConfigDTO> dtos = mapper.toDtoList(entityPage.getContent());
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }

    @Override
    public SystemConfigDTO updateById(Long dto) {
        Optional<SystemConfigDTO> optionalDto = findById(dto);
        return optionalDto.map(this::save).orElse(null);
    }
}