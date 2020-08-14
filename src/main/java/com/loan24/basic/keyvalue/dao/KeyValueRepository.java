package com.loan24.basic.keyvalue.dao;

import com.loan24.basic.keyvalue.domain.KeyValue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyValueRepository extends JpaRepository<KeyValue, Long> {
}