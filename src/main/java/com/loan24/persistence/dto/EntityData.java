package com.loan24.persistence.dto;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
@Getter
@Setter
public abstract class EntityData {
    @Id
    private Long id;
}
