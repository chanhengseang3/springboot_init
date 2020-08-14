package com.loan24.persistence.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.hibernate.annotations.FilterDef;
import org.springframework.data.domain.Persistable;

import javax.persistence.*;
import java.io.Serializable;

@MappedSuperclass
@Getter
@Setter
@JsonIgnoreProperties(ignoreUnknown = true)
@FilterDef(name = "noAccessFilter", defaultCondition = "1 = 0")
@Filter(name = "noAccessFilter")
public abstract class VersionEntity implements Serializable, Persistable<Long> {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Version
    @JsonIgnore
    @Column(columnDefinition = "SMALLINT default 0")
    private Short version;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return version == null;
    }
}
