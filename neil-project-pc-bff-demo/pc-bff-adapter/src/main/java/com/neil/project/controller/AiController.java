package com.neil.project.controller;

import com.neil.project.ai.dto.AiChatDTO;
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


    @Operation(summary = "流式聊天")
    @PostMapping(value = "/streamChat")
    public void streamChat(@RequestBody AiChatDTO aiChatDTO) {
        aiService.streamChat(aiChatDTO);
    }

}
