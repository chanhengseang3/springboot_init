package com.chs.basic.file.storage;

import lombok.extern.java.Log;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.util.FileSystemUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import java.util.stream.Stream;

@Log
@Service
public class FileSystemStorageService implements StorageService {

    private final Path rootLocation;
    private final Path xLocation;
    private final Path xxLocation;
    private final StorageProperties properties;

    public FileSystemStorageService(final StorageProperties properties) {
        this.rootLocation = Paths.get(properties.getLocation());
        this.xLocation = Paths.get(properties.getXLocation());
        this.xxLocation = Paths.get(properties.getXxLocation());
        this.properties = properties;
    }

    @Override
    public String store(@NotNull MultipartFile file) {
        if (file.getOriginalFilename() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "File name not found");
        }
        String filename = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            if (file.isEmpty()) {
                throw new StorageException("Failed to store empty file " + filename);
            }
            if (filename.contains("..")) {
                // This is a security check
                throw new StorageException(
                        "Cannot store file with relative path outside current directory "
                                + filename);
            }
            try (InputStream inputStream = file.getInputStream()) {
                var name = newName(filename);
                Files.copy(inputStream, this.rootLocation.resolve(name),
                        StandardCopyOption.REPLACE_EXISTING);
                return name;
            }
        } catch (IOException e) {
            throw new StorageException("Failed to store file " + filename, e);
        }
    }

    @Override
    public void writeXOutput(final String name) {
        try {
            Thumbnails.of(load(name).toFile())
                    .size(properties.getXWidth(), properties.getXHeight())
                    .useOriginalFormat()
                    .toFile(xLocation.resolve(name).toFile());
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }

    @Override
    public void writeXxOutput(final String name) {
        try {
            Thumbnails.of(load(name).toFile())
                    .size(properties.getXxWidth(), properties.getXxHeight())
                    .useOriginalFormat()
                    .toFile(xxLocation.resolve(name).toFile());
        } catch (IOException e) {
            log.warning(e.getMessage());
        }
    }

    private String newName(@NotNull final String name) {
        return UUID.randomUUID().toString().replaceAll("-", "")
                + "." + StringUtils.getFilenameExtension(name);
    }

    @Override
    public Stream<Path> loadAll() {
        try {
            return Files.walk(this.rootLocation, 1)
                    .filter(path -> !path.equals(this.rootLocation))
                    .map(this.rootLocation::relativize);
        } catch (IOException e) {
            throw new StorageException("Failed to read stored files", e);
        }
    }

    @Override
    public Path load(String filename) {
        return rootLocation.resolve(filename);
    }

    private Path loadX(String filename) {
        return xLocation.resolve(filename);
    }

    private Path loadXx(String filename) {
        return xxLocation.resolve(filename);
    }

    @Override
    public Resource loadAsResource(String filename) {
        final var file = load(filename);
        return loadResource(file, filename);
    }

    @Override
    public Resource loadXAsResource(final String filename) {
        final var file = loadX(filename);
        return loadResource(file, filename);
    }

    @Override
    public Resource loadXxAsResource(final String filename) {
        final var file = loadXx(filename);
        return loadResource(file, filename);
    }

    private Resource loadResource(final Path file, final String filename) {
        try {
            final var resource = new UrlResource(file.toUri());
            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new StorageFileNotFoundException("Could not read file: " + filename);
            }
        } catch (MalformedURLException e) {
            throw new StorageFileNotFoundException("Could not read file: " + filename, e);
        }
    }

    @Override
    public boolean deleteByName(final String filename) {
        try {
            final var file = load(filename).toFile();
            final var xFile = loadX(filename).toFile();
            final var xxFile = loadXx(filename).toFile();
            final var zin = file.delete();
            final var x = xFile.delete();
            final var xx = xxFile.delete();
            return zin && x && xx;
        } catch (Exception e) {
            log.warning(e.getMessage());
            return false;
        }
    }

    @Override
    public void deleteAll() {
        FileSystemUtils.deleteRecursively(rootLocation.toFile());
    }

    @Override
    public void init() {
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new StorageException("Could not initialize storage", e);
        }
    }
}
