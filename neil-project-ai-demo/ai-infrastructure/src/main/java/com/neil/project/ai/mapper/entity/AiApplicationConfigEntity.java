package com.neil.project.ai.mapper.entity;

import com.neil.project.ai.enums.AiApplicationConfigTypeEnum;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author nihao
 * @date 2024/10/10
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Entity
@Table(name = "ai_application_config")
public class AiApplicationConfigEntity extends BaseEntity {

    private String applicationNo;

    @Enumerated(EnumType.STRING)
    private AiApplicationConfigTypeEnum type;

    private String content;
}
