package com.neil.project.ai.api;

import com.google.common.collect.Lists;
import com.neil.project.ai.AiFacade;
import com.neil.project.ai.NeilPromptDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestBody;
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
    public String chat(@RequestParam("text") String text) {
        Prompt prompt = new Prompt(new UserMessage(text));
        return this.chatClient.prompt(prompt).call().content();
    }

    @Operation(summary = "streamChat")
    @Override
    public Flux<String> streamChat(@RequestParam("text") String text) {
        return streamingChatModel.stream(text);
    }

    @Operation(summary = "streamMultimodal")
    @Override
    public Flux<String> streamMultimodal(@RequestBody NeilPromptDTO neilPromptDTO) {
        UserMessage userMessage;
        if (null == neilPromptDTO.getImage()) {
            userMessage = new UserMessage(neilPromptDTO.getText());
        } else {
            userMessage = new UserMessage(neilPromptDTO.getText(),
                    Lists.newArrayList(new Media(MimeTypeUtils.IMAGE_PNG, neilPromptDTO.getImage())));
        }
        Prompt prompt = new Prompt(userMessage);
        var ollamaApi = new OllamaApi();
        var chatModel = new OllamaChatModel(ollamaApi,
                OllamaOptions.create()
                        .withModel("llava")
                        .withTemperature(0.9f));
        return chatModel.stream(prompt).map(result -> result.getResult().getOutput().getContent());
    }

}
