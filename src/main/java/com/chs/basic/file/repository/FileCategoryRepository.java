package com.chs.basic.file.repository;

import com.chs.basic.file.domain.FileCategory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileCategoryRepository extends JpaRepository<FileCategory, Long> {
}
