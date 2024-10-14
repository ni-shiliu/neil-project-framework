package com.neil.project.ai.mapper;

import com.neil.project.ai.enums.AiApplicationConfigTypeEnum;
import com.neil.project.ai.mapper.entity.AiApplicationConfigEntity;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

/**
 * @author nihao
 * @date 2024/10/10
 */
public interface AiApplicationConfigRepository extends JpaRepository<AiApplicationConfigEntity, Long> {

    Optional<List<AiApplicationConfigEntity>> findByApplicationNoAndType(@NotEmpty String applicationNo, AiApplicationConfigTypeEnum prompt);

}
