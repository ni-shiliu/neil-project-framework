package com.neil.project.ai;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

/**
 * @author nihao
 * @date 2024/7/25
 */
@Tag(name = "AI服务")
@FeignClient(
        name = "neil-project-ai",
        contextId = "neil-project-ai",
        url = "localhost:8093"
)
public interface AiFacade {
    @Operation(summary = "chat")
    @GetMapping("/chat")
    String generation(String userInput);

    @GetMapping(value = "/streamChat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> streamChat(@RequestParam("prompt") String prompt);
}
