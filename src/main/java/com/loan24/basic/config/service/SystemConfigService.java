package com.loan24.basic.config.service;

import com.loan24.basic.config.dto.SystemConfigDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface SystemConfigService {
    SystemConfigDTO save(SystemConfigDTO dto);

    void save(List<SystemConfigDTO> dtos);

    void deleteById(Long id);

    Optional<SystemConfigDTO> findById(Long id);

    List<SystemConfigDTO> findAll();

    Page<SystemConfigDTO> findAll(Pageable pageable);

    SystemConfigDTO updateById(Long dto);
}