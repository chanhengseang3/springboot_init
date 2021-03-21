package com.chs.basic.audit.domain;

import com.chs.persistence.domain.VersionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class AuditLog extends VersionEntity {

    private String userName;

    @Column(columnDefinition = "text")
    private String action;

    private String entityType;

    @Enumerated(EnumType.STRING)
    private ActionType actionType;

    @Column(columnDefinition = "text")
    private String input;

    private LocalDateTime time;

    private String other;
}
