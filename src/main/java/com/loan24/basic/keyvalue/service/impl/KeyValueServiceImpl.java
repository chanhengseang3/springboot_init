package com.loan24.basic.keyvalue.service.impl;

import com.loan24.basic.keyvalue.dao.KeyValueRepository;
import com.loan24.basic.keyvalue.domain.KeyValue;
import com.loan24.basic.keyvalue.dto.KeyValueDTO;
import com.loan24.basic.keyvalue.mapper.KeyValueMapper;
import com.loan24.basic.keyvalue.service.KeyValueService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class KeyValueServiceImpl implements KeyValueService {
    private final KeyValueMapper mapper;
    private final KeyValueRepository repository;

    public KeyValueServiceImpl(KeyValueMapper mapper, KeyValueRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public KeyValueDTO save(KeyValueDTO dto) {
        return mapper.toDto(repository.save(mapper.toEntity(dto)));
    }

    @Override
    public void save(List<KeyValueDTO> dtos) {
        repository.saveAll(mapper.toEntityList(dtos));
    }

    @Override
    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    @Override
    public Optional<KeyValueDTO> findById(Long id) {
        Optional<KeyValue> entityOptional = repository.findById(id);
        return entityOptional.map(entity -> Optional.ofNullable(mapper.toDto(entity))).orElse(null);
    }

    @Override
    public List<KeyValueDTO> findAll() {
        return mapper.toDtoList(repository.findAll());
    }

    @Override
    public Page<KeyValueDTO> findAll(Pageable pageable) {
        Page<KeyValue> entityPage = repository.findAll(pageable);
        List<KeyValueDTO> dtos = mapper.toDtoList(entityPage.getContent());
        return new PageImpl<>(dtos, pageable, entityPage.getTotalElements());
    }

    @Override
    public KeyValueDTO updateById(Long id) {
        Optional<KeyValueDTO> optionalDto = findById(id);
        return optionalDto.map(this::save).orElse(null);
    }
}