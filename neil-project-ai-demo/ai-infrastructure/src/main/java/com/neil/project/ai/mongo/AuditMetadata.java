package com.neil.project.ai.mongo;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.*;
import org.springframework.data.domain.Persistable;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;

/**
 * @author nihao
 * @date 2024/10/12
 */
@Data
public class AuditMetadata implements Persistable<String> {

    @Id
    private String id;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime lastModifiedDate;

    @CreatedBy
    private String createdBy;

    @LastModifiedBy
    private String lastModifiedBy;

    /**
     * 是否删除
     */
    private boolean isDeleted = false;

    @Override
    @JsonIgnore
    public boolean isNew() {
        return !StringUtils.hasText(id);
    }
}
