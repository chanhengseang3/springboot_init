package com.loan24.user.authorization.dto;

import com.loan24.user.authorization.domain.ActionName;
import com.loan24.user.authorization.domain.PermissionScope;
import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class PermissionDto {
    private Long id;
    private ActionName actionName;
    private String entityName;
    private String codeName;
    private PermissionScope scope;
    private String description;
    private boolean selected;
}
