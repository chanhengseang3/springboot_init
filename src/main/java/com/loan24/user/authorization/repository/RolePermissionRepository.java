package com.loan24.user.authorization.repository;

import com.loan24.user.authorization.domain.RolePermission;
import com.loan24.user.authorization.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RolePermissionRepository extends JpaRepository<RolePermission, Long> {
    List<RolePermission> findAllByRole(final UserRole role);
    List<RolePermission> findAllByRoleId(final Long id);
}
