package com.neil.project.ai.facade;

import com.neil.project.ai.dto.NeilPromptDTO;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import reactor.core.publisher.Flux;

/**
 * @author nihao
 * @date 2024/7/25
 */
@Tag(name = "AI-demo")
@FeignClient(
        name = "neil-project-ai",
        contextId = "neil-project-ai-demo",
        url = "localhost:8093"
)
public interface AiDemoFacade {

    @GetMapping("/v1/demo/chat")
    String chat(@RequestParam("text") String text);

    @GetMapping(value = "/v1/demo/streamChat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> streamChat(@RequestParam("text") String text);

    @PostMapping(value = "/v1/demo/streamMultimodal", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    Flux<String> streamMultimodal(@RequestBody NeilPromptDTO neilPromptDTO);

    @GetMapping(value = "/v1/demo/embedChat")
    String embedChat(@RequestParam("text") String text);

    @GetMapping(value = "/v1/demo/saveEmbedData")
    void saveEmbedData(@RequestParam("text") String text);
}

