package com.loan24.user.authorization.service;

import com.loan24.persistence.exception.ResourceNotFoundException;
import com.loan24.user.authorization.domain.Permission;
import com.loan24.user.authorization.repository.PermissionRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class PermissionService {

    private final PermissionRepository repository;

    public List<Permission> getAll() {
        return repository.findAll();
    }

    public Permission getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(Permission.class, id));
    }

    public List<Permission> getAllByIds(List<Long> ids){
        return repository.findAllById(ids);
    }
}
