package com.chs.user.authentication.service;

import com.chs.exception.PasswordInvalidException;
import com.chs.persistence.exception.ResourceNotFoundException;
import com.chs.persistence.service.EntityDataMapper;
import com.chs.user.authentication.domain.AppUser;
import com.chs.user.authentication.repository.AppUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AppUserService {

    private final AppUserRepository repository;
    private final PasswordEncoder encoder;
    private final EntityDataMapper entityDataMapper;

    public AppUser getUserByUserName(final String name) {
        return repository.findByUserName(name).orElseThrow(() -> new ResourceNotFoundException(AppUser.class, name));
    }

    public AppUser getUserByEmail(@Email @NotNull final String email) {
        return repository.findByEmail(email).orElseThrow(() -> new ResourceNotFoundException(AppUser.class, email));
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
