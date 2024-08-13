package com.neil.project.service.impl;

import com.neil.project.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;

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
    public void streamChat(String prompt) {
        Flux<String> flux = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("streamChat")
                        .queryParam("prompt", prompt)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);
        AtomicInteger wordsSize = new AtomicInteger();
        flux.subscribe(
                content -> {
                    wordsSize.getAndIncrement();
                    if (wordsSize.get() > 20) {
                        System.out.println();
                        wordsSize.set(0);
                    }
                    System.out.print(content + " ");
                }
        );
    }

}
