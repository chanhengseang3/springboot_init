package com.loan24.basic.file.controller;

import com.loan24.basic.file.domain.FileEntity;
import com.loan24.basic.file.domain.FileType;
import com.loan24.basic.file.repository.FileCategoryRepository;
import com.loan24.basic.file.repository.FileRepository;
import com.loan24.basic.file.storage.StorageFileNotFoundException;
import com.loan24.basic.file.storage.StorageService;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
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
public class FileUploadController {

    private final StorageService storageService;
    private final ByteArrayOutputStream thumbOutput = new ByteArrayOutputStream();

    @Value("${server.ip}")
    private String serverIp;
    @Value("${server.port}")
    private String serverPort;

    @Autowired
    private FileRepository repository;

    @Autowired
    private FileCategoryRepository categoryRepository;

    public FileUploadController(StorageService storageService) {
        this.storageService = storageService;
    }

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

    @GetMapping("/x/image/{filename:.+}")
    @ResponseBody
    public byte[] serveXImage(@PathVariable String filename) throws IOException {
        final var file = storageService.loadXAsResource(filename);
        return file.getInputStream().readAllBytes();
    }

    @GetMapping("/xx/image/{filename:.+}")
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
        thumbOutput.reset();
        Thumbnails.of(file.getInputStream())
                .size(width, height)
                .useOriginalFormat()
                .toOutputStream(thumbOutput);
        return thumbOutput.toByteArray();
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
        final var nf = repository.save(newFile);
        nf.setSmallImagePath(serverIp + ":" + serverPort + "/x/image/" + newFile.getName());
        nf.setMediumImagePath(serverIp + ":" + serverPort + "/xx/image/" + newFile.getName());
        nf.setLargeImagePath(serverIp + ":" + serverPort + "/image/" + newFile.getName());
        return nf;
    }

    @ExceptionHandler(StorageFileNotFoundException.class)
    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
        return ResponseEntity.notFound().build();
    }

}
