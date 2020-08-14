package com.loan24.persistence.dto;

import com.loan24.persistence.domain.AssignEntity;
import com.loan24.persistence.domain.AssignFor;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

import static com.loan24.persistence.dto.LocalDateFormat.DATE_FORMAT;

@Data
public class AssignedDto {
    private Long userId;
    private String userName;
    @JsonFormat(pattern = DATE_FORMAT)
    private LocalDateTime createdAt;
    private AssignFor assignFor;

    public static AssignedDto fromEntity(final AssignEntity assignEntity) {
        var dto = new AssignedDto();
        dto.setAssignFor(assignEntity.getAssignFor());
        dto.setCreatedAt(assignEntity.getCreatedAt());
        dto.setUserId(assignEntity.getAppUser().getId());
        dto.setUserName(assignEntity.getAppUser().getUserName());
        return dto;
    }
}
