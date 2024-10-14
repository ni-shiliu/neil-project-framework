package com.neil.project.ai.api;

import com.neil.project.ai.dto.AiChatDTO;
import com.neil.project.ai.facade.AiFacade;
import com.neil.project.ai.service.AiService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author nihao
 * @date 2024/10/10
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class AiController implements AiFacade {

    private final AiService aiService;


    @Override
    public Flux<String> streamChat(@RequestBody @Valid AiChatDTO aiChatDTO) {
        return aiService.streamChat(aiChatDTO);

    }

}
