package com.loan24.user.authentication.service;

import com.loan24.exception.PasswordInvalidException;
import com.loan24.persistence.exception.ResourceNotFoundException;
import com.loan24.persistence.service.EntityDataMapper;
import com.loan24.user.authentication.domain.AppUser;
import com.loan24.user.authentication.repository.AppUserRepository;
import com.loan24.user.authorization.service.UserRoleService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@AllArgsConstructor
@Transactional
public class AppUserService {

    private final AppUserRepository repository;
    private final UserRoleService roleService;
    private final PasswordEncoder encoder;
    private final EntityDataMapper entityDataMapper;

    public AppUser getUserByUserName(final String name) {
        return repository.findByUserName(name).orElseThrow(() -> new ResourceNotFoundException(AppUser.class, name));
    }

    public AppUser getUserByEmail(@Email @NotNull final String email) {
        return repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(AppUser.class, email));
    }

    public List<AppUser> getByRole(Long id) {
        return repository.findAllByRoleId(id);
    }

    public AppUser getById(Long id) {
        return repository.findById(id).orElseThrow(() -> new ResourceNotFoundException(AppUser.class, id));
    }

    public List<AppUser> getAll() {
        return repository.findAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public AppUser assignRole(Long userId, Long roleId) {
        var role = roleService.getById(roleId);
        var user = repository.findById(userId).orElseThrow();
        user.setRole(role);
        return repository.save(user);
    }

    public AppUser changePassword(final AppUser user, final String oldPass, final String newPass) {
        if (user != null && encoder.matches(oldPass, user.getPassword())) {
            assert user.getId() != null;
            final var appUser = repository.findById(user.getId()).orElseThrow();
            appUser.setPassword(encoder.encode(newPass));
            return repository.save(appUser);
        }
        throw new PasswordInvalidException();
    }

    public AppUser updateUser(Long id, final AppUser sourceUser) {
        final var targetUser = getById(id);
        final var password = targetUser.getPassword();
        try {
            final var user = entityDataMapper.mapObject(sourceUser, targetUser, AppUser.class);
            if (sourceUser.getPassword() == null) { //keep password
                user.setPassword(password);
            }
            return repository.save(user);
        } catch (Exception exception) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, exception.getMessage(), exception);
        }
    }

    public AppUser createUser(final AppUser appUser) {
        return repository.save(appUser);
    }
}
