package com.loan24.basic.file.storage;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.util.stream.Stream;

public interface StorageService {

    void init();

    void writeXOutput(final String filename);

    void writeXxOutput(final String filename);

    String store(MultipartFile file);

    Stream<Path> loadAll();

    Path load(String filename);

    Resource loadAsResource(final String filename);

    Resource loadXAsResource(final String filename);

    Resource loadXxAsResource(final String filename);

    boolean deleteByName(final String name);

    void deleteAll();

}
