package com.neil.project.service.impl;

import com.neil.project.ai.NeilPromptDTO;
import com.neil.project.service.AiService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.IOException;
import java.util.Objects;
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
    public void streamChat(String text) {
        Flux<String> flux = webClient.get()
                .uri(uriBuilder -> uriBuilder.path("streamChat")
                        .queryParam("text", text)
                        .build())
                .accept(MediaType.TEXT_EVENT_STREAM)
                .retrieve()
                .bodyToFlux(String.class);

        AtomicInteger contentSize = new AtomicInteger();
        flux.subscribe(
                content -> {
                    contentSize.getAndAdd(content.length());
                    if (contentSize.get() > 100) {
                        System.out.println();
                        contentSize.set(0);
                    }
                    System.out.print(content + " ");
                }
        );
    }

    @Override
    public void streamMultimodal(MultipartFile image, String text) {
        NeilPromptDTO neilPromptDTO = new NeilPromptDTO();
        neilPromptDTO.setText(text);
        if (Objects.nonNull(image)) {
            try {
                neilPromptDTO.setImage(image.getBytes());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        Flux<String> flux = this.webClient.post()
                .uri("streamMultimodal")
                .body(Mono.just(neilPromptDTO), NeilPromptDTO.class)
                .retrieve().bodyToFlux(String.class);


        AtomicInteger contentSize = new AtomicInteger();
        flux.subscribe(
                content -> {
                    contentSize.getAndAdd(content.length());
                    if (contentSize.get() > 100) {
                        System.out.println();
                        contentSize.set(0);
                    }
                    System.out.print(content + " ");
                }
        );
    }

}
