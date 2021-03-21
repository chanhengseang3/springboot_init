package com.chs.user.authentication.controller;

import com.chs.appconfiguration.utils.ApplicationSecurityContext;
import com.chs.user.authentication.data.UserData;
import com.chs.user.authentication.data.UserDataMapper;
import com.chs.user.authentication.domain.AppUser;
import com.chs.user.authentication.service.AppUserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("user")
@AllArgsConstructor
public class AppUserController {

    private final AppUserService service;
    private final UserDataMapper mapper;
    private final ApplicationSecurityContext context;

    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public List<AppUser> getAllUser() {
        return service.getAll();
    }

    @GetMapping("{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public AppUser getUserById(@NotNull @PathVariable("id") final Long id) {
        return service.getById(id);
    }

    @GetMapping("current")
    public AppUser getCurrentUser() {
        final var user = context.authenticatedUser();
        if (user == null) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "unauthorized request");
        }
        return user;
    }

    @PutMapping("changePassword")
    public AppUser changePassword(@RequestParam("old") final String oldPassword,
                                  @RequestParam("new") final String newPassword) {
        return service.changePassword(context.authenticatedUser(), oldPassword, newPassword);
    }

    @PutMapping("{id}")
    public AppUser updateUser(@NotNull @PathVariable final Long id, @RequestBody final UserData data) {
        return service.updateUser(id, mapper.map(data));
    }

    @PostMapping
    public AppUser createUser(@Valid @RequestBody final UserData data) {
        return service.createUser(mapper.map(data));
    }

    @DeleteMapping("{id}")
    public boolean deleteUser(@NotNull @PathVariable("id") final Long id) {
        if (id.equals(context.authenticatedUser().getId())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot delete current user");
        }
        if (id == 1L) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "cannot delete admin user");
        }
        service.deleteById(id);
        return true;
    }
}
