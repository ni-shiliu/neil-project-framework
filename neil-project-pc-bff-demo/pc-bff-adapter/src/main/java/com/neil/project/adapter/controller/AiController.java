package com.neil.project.adapter.controller;

import com.neil.project.service.AiService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

/**
 * @author nihao
 * @date 2024/8/13
 */
@Tag(name = "AI模块")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pc/ai/")
public class AiController {

    private final AiService aiService;


    @GetMapping(value = "/streamChat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public void streamChat(@RequestParam("prompt") String prompt) {
        aiService.streamChat(prompt);
    }
}
