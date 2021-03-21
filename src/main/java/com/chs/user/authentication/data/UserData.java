package com.chs.user.authentication.data;

import com.chs.user.authentication.domain.UserRole;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(value = "password", allowSetters = true)
public class UserData {
    @NotNull
    String userName;
    @Email
    String email;
    String mobile;
    @NotNull
    String password;
    UserRole role;
}
