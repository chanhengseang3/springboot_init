package com.loan24.basic.file.repository;

import com.loan24.basic.file.domain.FileCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileCategoryRepository extends JpaRepository<FileCategory, Long> {
}
