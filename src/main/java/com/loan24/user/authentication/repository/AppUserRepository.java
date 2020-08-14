package com.loan24.user.authentication.repository;

import com.loan24.user.authentication.domain.AppUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface AppUserRepository extends JpaRepository<AppUser, Long> {

    Optional<AppUser> findByUserName(final String name);

    Optional<AppUser> findByEmail(final String email);

    Optional<AppUser> findByMobile(final String mobile);

    List<AppUser> findAllByRoleId(final Long id);
}
