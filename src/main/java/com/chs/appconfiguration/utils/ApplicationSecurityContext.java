package com.chs.appconfiguration.utils;

import com.chs.user.authentication.domain.AppUser;
import com.chs.user.authentication.service.AppUserService;
import com.chs.user.authentication.service.UserAuthentication;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class ApplicationSecurityContext {

    @Autowired
    private AppUserService service;

    public Authentication getAuth() {
        final var context = SecurityContextHolder.getContext();
        return context == null ? null : context.getAuthentication();
    }

    public AppUser authenticatedUser() {
        try {
            var authentication = getAuth();
            var principle = authentication == null ? null : authentication.getPrincipal();
            if (principle == null) {
                return null;
            } else if (principle instanceof UserAuthentication) {
                return ((UserAuthentication) principle).getAppUser();
            } else if (principle instanceof String && !principle.equals("anonymousUser")) {
                return service.getUserByUserName((String) principle);
            }
            return null;
        } catch (Exception e) {
            log.error(e.getMessage());
            return null;
        }
    }

    public boolean hasPermission(final String permission) {
        var permissions = userPermissions();
        if (permissions == null) return false;
        return permissions.contains(permission);
    }

    public List<String> userPermissions() {
        var authentication = getAuth();
        if (authentication == null) {
            return null;
        }
        return getAuth().getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
    }
}
