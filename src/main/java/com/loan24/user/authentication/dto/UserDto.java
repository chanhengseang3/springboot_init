package com.loan24.user.authentication.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.Email;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDto {
    String userName;
    @Email
    String email;
    String mobile;
    String password;
    Long roleId;
}
