package com.loan24.persistence.domain;

import com.loan24.user.authentication.domain.AppUser;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Where;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
@EntityListeners(AuditingEntityListener.class)
@Where(clause = "status <> 'DELETED'")
@JsonIgnoreProperties(value = {"createdAt", "updatedAt", "verifiedAt", "approvedAt", "status"}, allowGetters = true)
public abstract class AuditingEntity extends VersionEntity {

    @CreatedBy
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "created_by", updatable = false)
    private AppUser createdBy;

    @CreatedDate
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedBy
    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "updated_by")
    private AppUser updatedBy;

    @LastModifiedDate
    @Column(name = "updated_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "verified_by")
    private AppUser verifiedBy;

    @Column(name = "verified_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime verifiedAt;

    @ManyToOne
    @JsonIgnore
    @JoinColumn(name = "approved_by")
    private AppUser approvedBy;

    @Column(name = "approved_at")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime approvedAt;

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private ObjectStatus status = ObjectStatus.OPEN;
}
