package com.neil.project.ai.api;

import com.neil.project.ai.AiFacade;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

/**
 * @author nihao
 * @date 2024/8/1
 */
@Slf4j
@Tag(name = "AI")
@RestController
public class ChatController implements AiFacade {

    private final ChatClient chatClient;


    private final StreamingChatModel streamingChatModel;
    public ChatController(ChatClient.Builder chatClientBuilder, StreamingChatModel streamingChatModel) {
        this.chatClient = chatClientBuilder.build();
        this.streamingChatModel = streamingChatModel;
    }

    @Override
    @Operation(summary = "chat")
    @GetMapping("/chat")
    public String generation(String userInput) {
        return this.chatClient.prompt()
                .user(userInput)
                .call()
                .content();
    }

    @Override
    @GetMapping(value = "/streamChat", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> streamChat(@RequestParam("prompt") String prompt) {
        return streamingChatModel.stream(prompt);
    }

}
