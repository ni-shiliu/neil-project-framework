package com.neil.project.service.impl;

import com.neil.project.ai.dto.AiChatDTO;
import com.neil.project.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author nihao
 * @date 2024/8/13
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class AiServiceImpl implements AiService {

    private final WebClient webClient;


    @Override
    public void streamChat(AiChatDTO aiChatDTO) {
        Flux<String> flux = this.webClient.post()
                .uri("/v1/streamChat")
                .body(Mono.just(aiChatDTO), AiChatDTO.class)
                .retrieve().bodyToFlux(String.class);

        AtomicInteger contentSize = new AtomicInteger();
        flux.subscribe(
                content -> {
                    contentSize.getAndAdd(content.length());
                    if (contentSize.get() > 150) {
                        System.out.println();
                        contentSize.set(0);
                    }
                    System.out.print(content + " ");
                }
        );
    }

}
