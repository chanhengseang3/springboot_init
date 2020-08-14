package com.loan24.user.authentication.service;

import com.loan24.persistence.exception.ResourceNotFoundException;
import com.loan24.user.authentication.domain.AppUser;
import com.loan24.user.authentication.repository.AppUserRepository;
import com.loan24.user.authorization.domain.ActionName;
import com.loan24.user.authorization.domain.Permission;
import com.loan24.user.authorization.repository.PermissionRepository;
import com.loan24.user.authorization.service.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
@Transactional
@SuppressWarnings("unchecked")
public class UserService {

    public static final String ALL_PERMISSION = "ALL_ALL_ALL";
    public static final String READ_ALL_PERMISSION = "READ_ALL_ALL";

    private final AppUserRepository repository;
    private final PermissionRepository permissionRepository;
    private final UserRoleService roleService;

    public List<SimpleGrantedAuthority> grantedAuthorities(AppUser user) {
        var role = user.getRole();
        if (role == null) {
            return Collections.EMPTY_LIST;
        }
        var permissions = roleService.getRolePermission(role.getId());
        if (!permissions.isEmpty()) {
            if (permissions.stream().anyMatch(permission -> permission.getCodeName().equals(ALL_PERMISSION))) {
                return allAuthorities();
            }
            if (permissions.stream().anyMatch(permission -> permission.getCodeName().equals(READ_ALL_PERMISSION))) {
                return readAllAuthorities();
            }
            return permissions.stream().map(this::getAuthorityFromPermission).collect(Collectors.toList());
        }
        return Collections.EMPTY_LIST;
    }

    private List<SimpleGrantedAuthority> allAuthorities() {
        return permissionRepository.findAll().stream()
                .map(this::getAuthorityFromPermission)
                .collect(Collectors.toList());
    }

    private List<SimpleGrantedAuthority> readAllAuthorities() {
        return permissionRepository.findAllByActionName(ActionName.READ).stream()
                .map(this::getAuthorityFromPermission)
                .collect(Collectors.toList());
    }

    private SimpleGrantedAuthority getAuthorityFromPermission(Permission permission) {
        return new SimpleGrantedAuthority(permission.getCodeName());
    }

    public AppUser getUserByUserName(final String name) {
        return repository.findByUserName(name).orElseThrow(() -> new ResourceNotFoundException(AppUser.class, name));
    }
}
