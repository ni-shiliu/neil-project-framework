package com.neil.project.ai.service;

import com.neil.project.ai.dto.AiChatDTO;
import reactor.core.publisher.Flux;

/**
 * @author nihao
 * @date 2024/10/10
 */
public interface AiService {

    Flux<String> streamChat(AiChatDTO aiChatDTO);
}
