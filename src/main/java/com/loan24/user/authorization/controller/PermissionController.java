package com.loan24.user.authorization.controller;

import com.loan24.persistence.service.EntityDataMapper;
import com.loan24.user.authorization.domain.Permission;
import com.loan24.user.authorization.repository.PermissionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/permission")
public class PermissionController {

    @Autowired
    private PermissionRepository repository;
    @Autowired
    private EntityDataMapper dataMapper;

    @GetMapping
    public List<Permission> getAll() {
        return repository.findAll();
    }

    @GetMapping("/{id}")
    public Permission getById(@PathVariable Long id) {
        return repository.findById(id).orElseThrow();
    }

    @GetMapping("/page")
    public Page<Permission> getAllAsPage(Pageable pageable) {
        return repository.findAll(pageable);
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasAuthority('ALL_ALL_ALL')")
    public Permission update(@PathVariable Long id, @RequestBody Permission permission) {
        var target = repository.findById(id).orElseThrow();
        target = dataMapper.mapObject(permission, target, Permission.class);
        return repository.save(target);
    }
}
