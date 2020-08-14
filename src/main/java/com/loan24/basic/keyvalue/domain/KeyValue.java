package com.loan24.basic.keyvalue.domain;

import com.loan24.persistence.domain.VersionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.hibernate.annotations.ResultCheckStyle;
import org.hibernate.annotations.SQLDelete;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Entity
@Getter
@Setter
@Table(name = "key_value")
@Accessors(chain = true)
@SQLDelete(sql = "delete from key_value where removable <> 0 and id = ? and version = ?", check = ResultCheckStyle.COUNT)
public class KeyValue extends VersionEntity {

    @Enumerated(EnumType.STRING)
    @Column(name = "key_value_group")
    private KeyValueGroup group = KeyValueGroup.OTHER;

    @NotNull
    @Column(name = "key_", nullable = false)
    private String key;

    private String value;

    private String remarks;

    @Column(updatable = false)
    private boolean removable = true;
}
