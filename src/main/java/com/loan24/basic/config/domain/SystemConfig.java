package com.loan24.basic.config.domain;

import com.loan24.persistence.domain.VersionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.Filter;

import javax.persistence.Column;
import javax.persistence.Entity;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@Filter(name = "adminFilter", condition = "1 = 0")
public class SystemConfig extends VersionEntity {

    @Column(nullable = false, unique = true, updatable = false)
    private String code;

    private String value;
}
