package com.chs.basic.file.domain;

import com.chs.persistence.domain.VersionEntity;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Set;

@Entity
@Getter
@Setter
@Accessors(chain = true)
@EntityListeners(AuditingEntityListener.class)
public class FileEntity extends VersionEntity {

    private static final Set<String> IMAGE_TYPE = Set.of("png", "jpg", "gif", "jpeg", "bmp");
    private static final Set<String> VIDEO_TYPE = Set.of("mp4", "kvm", "flv", "avi");
    private static final Set<String> SOUND_TYPE = Set.of("wav", "mp3", "amr");
    private static final Set<String> DOCUMENT_TYPE = Set.of("doc", "docx", "xls", "xlsx", "ppt", "pptx", "txt", "pdf");
    private static final Map<Set<String>, FileType> MAP = Map.of(
            IMAGE_TYPE, FileType.IMAGE,
            VIDEO_TYPE, FileType.VIDEO,
            SOUND_TYPE, FileType.SOUND,
            DOCUMENT_TYPE, FileType.DOCUMENT
    );

    @CreatedDate
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    private String path;

    @Column(unique = true)
    private String name;

    private String extension;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private FileCategory category;

    @Enumerated(EnumType.STRING)
    private FileType type;

    public FileEntity setType() {
        if (this.extension == null) return this;
        MAP.forEach((k, v) -> {
            if (k.contains(extension.toLowerCase())) {
                this.type = v;
            }
        });
        return this;
    }

    public FileEntity setPath() {
        if (this.type == FileType.IMAGE) {
            this.path = "/image/" + this.name;
        } else {
            this.path = "/files/" + this.name;
        }
        return this;
    }
}
