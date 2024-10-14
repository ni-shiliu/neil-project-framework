package com.neil.project.ai.mapper.entity;

import com.neil.project.config.converter.BooleanToStringConverter;
import jakarta.persistence.*;
import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

/**
 * @author nihao
 * @date 2024/10/10
 */
@Data
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @CreatedBy
    private Long creatorId;

    @CreatedDate
    @Column(name = "gmt_created")
    private LocalDateTime gmtCreated;

    @LastModifiedBy
    private Long modifierId;

    @LastModifiedDate
    private LocalDateTime gmtModified;

    @Convert(converter = BooleanToStringConverter.class)
    private Boolean isDeleted;
}
