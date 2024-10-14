package com.neil.project.ai.facade;

import com.neil.project.ai.dto.AiChatDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import reactor.core.publisher.Flux;

/**
 * @author nihao
 * @date 2024/10/10
 */
@Tag(name = "AI服务")
@FeignClient(
        name = "neil-project-ai",
        contextId = "neil-project-ai",
        url = "localhost:8093"
)
public interface AiFacade {

    @Operation(summary = "streamChat")
    @PostMapping(value = "/v1/streamChat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> streamChat(@RequestBody @Valid AiChatDTO aiChatDTO);

}
