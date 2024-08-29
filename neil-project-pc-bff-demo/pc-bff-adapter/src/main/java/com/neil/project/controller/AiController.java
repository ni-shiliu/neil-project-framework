package com.neil.project.controller;

import com.neil.project.service.AiService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    @Operation(summary = "同步聊天")
    @GetMapping(value = "/chat")
    public String chat(@RequestParam("text") String text) {
        return aiService.chat(text);
    }

    @Operation(summary = "流式聊天")
    @GetMapping(value = "/streamChat")
    public void streamChat(@RequestParam("text") String text) {
        aiService.streamChat(text);
    }

    @Operation(summary = "多模态流式聊天")
    @PostMapping(value = "/streamMultimodal")
    public void streamMultimodal(
            @RequestParam(value = "image", required = false) MultipartFile image,
            @RequestParam("text") String text) {
        aiService.streamMultimodal(image, text);
    }
}
