package com.chs.user.authentication.service;

import com.chs.persistence.exception.ResourceNotFoundException;
import com.chs.user.authentication.domain.AppUser;
import com.chs.user.authentication.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final AppUserRepository repository;

    public List<SimpleGrantedAuthority> grantedAuthorities(AppUser user) {
        var role = user.getRole();
        if (role == null) {
            return List.of();
        }
        return List.of(new SimpleGrantedAuthority(user.getRole().name()));
    }

    public AppUser getUserByUserName(final String name) {
        return repository.findByUserName(name).orElseThrow(() -> new ResourceNotFoundException(AppUser.class, name));
    }
}
