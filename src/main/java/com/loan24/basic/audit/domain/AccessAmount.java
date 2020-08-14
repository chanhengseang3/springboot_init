package com.loan24.basic.audit.domain;

import com.loan24.persistence.domain.VersionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Min;
import java.time.Month;

@Entity
@Getter
@Setter
@Accessors(chain = true)
public class AccessAmount extends VersionEntity {

    @Min(2020)
    private int year;

    @Enumerated(EnumType.STRING)
    private Month month;

    @Min(0)
    private Integer amount = 0;
}
