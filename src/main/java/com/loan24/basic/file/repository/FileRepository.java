package com.loan24.basic.file.repository;

import com.loan24.basic.file.domain.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
    void deleteByName(final String name);
}
