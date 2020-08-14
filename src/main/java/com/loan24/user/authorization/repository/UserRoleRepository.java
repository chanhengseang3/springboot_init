package com.loan24.user.authorization.repository;

import com.loan24.user.authorization.domain.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
