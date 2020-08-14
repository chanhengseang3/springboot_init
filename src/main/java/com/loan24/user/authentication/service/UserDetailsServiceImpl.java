package com.loan24.user.authentication.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserService service;

    @Override
    public UserAuthentication loadUserByUsername(final String name) throws UsernameNotFoundException {
        final var appUser = service.getUserByUserName(name);
        return new UserAuthentication(appUser.getUserName(), appUser.getPassword(), service.grantedAuthorities(appUser), appUser);
    }
}
