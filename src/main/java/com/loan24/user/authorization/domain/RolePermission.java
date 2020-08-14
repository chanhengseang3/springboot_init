package com.loan24.user.authorization.domain;

import com.loan24.persistence.domain.VersionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Getter
@Setter
@Table(name = "role_permission")
@Accessors(chain = true)
public class RolePermission extends VersionEntity {

    @ManyToOne
    @JoinColumn
    private UserRole role;

    @ManyToOne
    @JoinColumn
    private Permission permission;
}
