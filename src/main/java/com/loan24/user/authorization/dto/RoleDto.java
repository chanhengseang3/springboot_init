package com.loan24.user.authorization.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@Accessors(chain = true)
public class RoleDto {
    private String name;
    private List<PermissionDto> permissions;
}
