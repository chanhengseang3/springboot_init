package com.chs.user.authentication.domain;

import com.chs.persistence.domain.VersionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "users", uniqueConstraints = @UniqueConstraint(columnNames = "user_name"))
@Accessors(chain = true)
@Where(clause = "status <> 'DELETED'")
@SQLDelete(sql = "update users set status = 'DELETED' where id = ? and version = ?")
public class AppUser extends VersionEntity {

    @NotNull
    @Column(name = "user_name", unique = true, nullable = false)
    private String userName;

    private String email;

    private String mobile;

    @NotNull
    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private UserStatus status;

    @PrePersist
    private void prePersist() {
        if (status == null) {
            status = UserStatus.ACTIVE;
        }
        if (role == null) {
            role = UserRole.USER;
        }
    }
}
