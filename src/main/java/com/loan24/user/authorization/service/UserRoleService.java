package com.loan24.user.authorization.service;

import com.loan24.user.authorization.domain.Permission;
import com.loan24.user.authorization.domain.RolePermission;
import com.loan24.user.authorization.domain.UserRole;
import com.loan24.user.authorization.dto.PermissionDto;
import com.loan24.user.authorization.dto.PermissionMapper;
import com.loan24.user.authorization.dto.RoleDto;
import com.loan24.user.authorization.repository.RolePermissionRepository;
import com.loan24.user.authorization.repository.UserRoleRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class UserRoleService {

    private final UserRoleRepository repository;
    private final RolePermissionRepository rolePermissionRepository;
    private final PermissionMapper permissionMapper;
    private final PermissionService permissionService;

    public UserRole save(RoleDto roleDto) {
        var role = new UserRole().setName(roleDto.getName());
        final var newRole = repository.save(role);
        if (roleDto.getPermissions() != null) {
            var permissionIds = roleDto.getPermissions().stream()
                    .filter(PermissionDto::isSelected)
                    .map(PermissionDto::getId).collect(Collectors.toList());
            var permissions = permissionService.getAllByIds(permissionIds);
            var rolePermissions = permissions.stream()
                    .map(permission -> newRolePermission(newRole, permission))
                    .collect(Collectors.toList());
            rolePermissionRepository.saveAll(rolePermissions);
        }
        return newRole;
    }

    private RolePermission newRolePermission(final UserRole role, Permission permission) {
        return new RolePermission().setPermission(permission).setRole(role);
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public UserRole getById(Long id) {
        return repository.findById(id).orElseThrow();
    }

    public List<UserRole> getAll() {
        return repository.findAll();
    }

    public Page<UserRole> getAll(Pageable pageable) {
        return repository.findAll(pageable);
    }

    public List<Permission> getRolePermission(Long id) {
        return rolePermissionRepository.findAllByRoleId(id)
                .stream()
                .map(RolePermission::getPermission)
                .collect(Collectors.toList());
    }

    public List<PermissionDto> getRolePermissionDto(Long id) {
        final var role = getById(id);
        final var permissions = rolePermissionRepository.findAllByRole(role)
                .stream()
                .map(RolePermission::getPermission)
                .collect(Collectors.toList());
        return permissionMapper.toRoleDto(role, permissions).getPermissions();
    }

    public UserRole updateById(Long id, RoleDto dto) {
        var role = getById(id);
        role.setName(dto.getName());
        final var newRole = repository.save(role);
        final var permissionDtos = dto.getPermissions();
        if (permissionDtos != null) {
            var permissionIds = permissionDtos.stream()
                    .filter(PermissionDto::isSelected)
                    .map(PermissionDto::getId)
                    .collect(Collectors.toList());
            var permissions = permissionService.getAllByIds(permissionIds);
            var newRolePermissions = permissions.stream()
                    .map(permission -> newRolePermission(newRole, permission))
                    .collect(Collectors.toList());
            var oldRolePermissions = rolePermissionRepository.findAllByRole(role);
            rolePermissionRepository.deleteAll(oldRolePermissions);
            rolePermissionRepository.saveAll(newRolePermissions);
        }
        return newRole;
    }
}