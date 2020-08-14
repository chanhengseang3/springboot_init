package com.loan24.basic.keyvalue.service;

import com.loan24.basic.keyvalue.dto.KeyValueDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface KeyValueService {
    KeyValueDTO save(KeyValueDTO dto);

    void save(List<KeyValueDTO> dtos);

    void deleteById(Long id);

    Optional<KeyValueDTO> findById(Long id);

    List<KeyValueDTO> findAll();

    Page<KeyValueDTO> findAll(Pageable pageable);

    KeyValueDTO updateById(Long id);
}