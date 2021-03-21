package com.chs.basic.file.controller;

import com.chs.basic.file.domain.FileEntity;
import com.chs.basic.file.domain.FileType;
import com.chs.basic.file.repository.FileCategoryRepository;
import com.chs.basic.file.repository.FileRepository;
import com.chs.basic.file.storage.StorageFileNotFoundException;
import com.chs.basic.file.storage.StorageService;
import lombok.RequiredArgsConstructor;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.transaction.Transactional;
import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class FileUploadController {

    private static final ByteArrayOutputStream THUMB_OUTPUT = new ByteArrayOutputStream();
    private final StorageService storageService;
    private final FileRepository repository;
    private final FileCategoryRepository categoryRepository;

    @GetMapping("/files")
    public List<FileEntity> getAllFiles() {
        return repository.findAll();
    }

    @GetMapping("/files/{filename:.+}")
    @ResponseBody
    public byte[] serveFile(@PathVariable String filename) throws IOException {
        final var file = storageService.loadAsResource(filename);
        return file.getInputStream().readAllBytes();
    }

    @DeleteMapping(value = "/files/{filename:.+}")
    @Transactional
    public void deleteFile(@PathVariable String filename) throws IOException {
        repository.deleteByName(filename);
        storageService.deleteByName(filename);
    }

    @DeleteMapping(value = "/image/{filename:.+}")
    @Transactional
    public void deleteImage(@PathVariable String filename) throws IOException {
        repository.deleteByName(filename);
        storageService.deleteByName(filename);
    }

    @GetMapping("/small/image/{filename:.+}")
    @ResponseBody
    public byte[] serveXImage(@PathVariable String filename) throws IOException {
        final var file = storageService.loadXAsResource(filename);
        return file.getInputStream().readAllBytes();
    }

    @GetMapping("/medium/image/{filename:.+}")
    @ResponseBody
    public byte[] serveXxImage(@PathVariable String filename) throws IOException {
        final var file = storageService.loadXxAsResource(filename);
        return file.getInputStream().readAllBytes();
    }

    @GetMapping(
            value = {"/image/{filename:.+}", "/image/{filename:.+}/{width}/{height}"},
            produces = {
                    MediaType.IMAGE_JPEG_VALUE,
                    MediaType.IMAGE_GIF_VALUE,
                    MediaType.IMAGE_PNG_VALUE})
    @ResponseBody
    public byte[] serveImage(@PathVariable("filename") @NotNull final String filename,
                             @RequestParam(value = "width", required = false) final Integer qWidth,
                             @RequestParam(value = "height", required = false) final Integer qHeight,
                             @PathVariable(value = "width", required = false) final Integer pWidth,
                             @PathVariable(value = "height", required = false) final Integer pHeight) throws IOException {
        final var file = storageService.loadAsResource(filename);
        final var width = qWidth == null ? pWidth : qWidth;
        final var height = qHeight == null ? pHeight : qHeight;
        if (width == null || height == null) {
            return file.getInputStream().readAllBytes();
        }
        THUMB_OUTPUT.reset();
        Thumbnails.of(file.getInputStream())
                .size(width, height)
                .useOriginalFormat()
                .toOutputStream(THUMB_OUTPUT);
        return THUMB_OUTPUT.toByteArray();
    }

    @PostMapping("/upload")
    public FileEntity handleFileUpload(@RequestParam("file") MultipartFile file,
                                       @RequestParam(value = "category", required = false) final Long categoryId) {
        final var name = storageService.store(file);
        final var newFile = new FileEntity()
                .setName(name)
                .setExtension(StringUtils.getFilenameExtension(name))
                .setType()
                .setPath();
        if (newFile.getType() == FileType.IMAGE) {
            storageService.writeXOutput(name);
            storageService.writeXxOutput(name);
        }
        if (categoryId != null) {
            final var category = categoryRepository.findById(categoryId).orElseThrow();
            newFile.setCategory(category);
        }
        return repository.save(newFile);
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
