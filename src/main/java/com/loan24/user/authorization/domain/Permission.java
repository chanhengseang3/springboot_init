package com.loan24.user.authorization.domain;

import com.loan24.persistence.domain.VersionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class Permission extends VersionEntity {

    @Enumerated(EnumType.STRING)
    private ActionName actionName;

    private String entityName;

    private String codeName;

    @Enumerated(EnumType.STRING)
    private PermissionScope scope = PermissionScope.ALL;

    private String description;
}
