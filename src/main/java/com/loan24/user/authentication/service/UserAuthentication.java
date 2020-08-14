package com.loan24.user.authentication.service;

import com.loan24.user.authentication.domain.AppUser;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

@Getter
public class UserAuthentication extends User {

    private final AppUser appUser;

    public UserAuthentication(String username, String password, Collection<? extends GrantedAuthority> authorities, AppUser appUser) {
        super(username, password, authorities);
        this.appUser = appUser;
    }
}
