package com.neil.project.ai.api;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Lists;
import com.neil.project.ai.facade.AiDemoFacade;
import com.neil.project.ai.dto.NeilPromptDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.StreamingChatModel;
import org.springframework.ai.chat.prompt.AssistantPromptTemplate;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.Media;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaApi;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.List;

/**
 * @author nihao
 * @date 2024/8/1
 */
@Slf4j
@Tag(name = "AI-DEMO")
@RestController
public class ChatController implements AiDemoFacade {

    private final ChatClient chatClient;

    private final StreamingChatModel streamingChatModel;

    private final EmbeddingModel embeddingModel;

    private final VectorStore vectorStore;

    private List<Message> chantHistory = Lists.newArrayList();

    public ChatController(ChatClient.Builder chatClientBuilder,
                          StreamingChatModel streamingChatModel,
                          EmbeddingModel embeddingModel,
                          VectorStore vectorStore) {
        this.chatClient = chatClientBuilder.build();
        this.streamingChatModel = streamingChatModel;
        this.embeddingModel = embeddingModel;
        this.vectorStore = vectorStore;
    }

    @Override
    @Operation(summary = "chat")
    public String chat(@RequestParam("text") String text) {
        UserMessage userMessage = new UserMessage(text);
        String systemPrompt = "你是一名拥有10年经验的优秀宠物医师，你需要以问答的方式，为每一位宠物主人提供对应病症的问诊结果和诊疗意见。\n" +
                "\n" +
                "为了帮助用户获得最佳诊疗结果，请遵循以下指引：\n" +
                "\n" +
                "详细了解宠物的症状和病史。\n" +
                "根据症状给出初步诊断。\n" +
                "根据诊断提供具体的诊疗建议，包括用药、护理和进一步检查的建议。\n" +
                "请参照以下示例进行回答：\n" +
                "宠物主人：我的猫最近不愿意吃东西，感觉很疲惫，还呕吐了几次。请问可能是什么问题？\n" +
                "宠物医生：根据您的描述，您的猫可能患有胃肠炎或其他消化系统问题。建议您带它去做一次详细的检查，并暂时喂它一些易消化的食物。同时，可以尝试给它服用一些抗呕吐药物。如果症状持续或加重，请尽快带它去看兽医。\n" +
                "\n" +
                "现在，请问您的宠物出现了什么症状？";
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        Message systemMessage = systemPromptTemplate.createMessage();
        List<Message> messages = Lists.newArrayList();
        if (CollUtil.isNotEmpty(this.chantHistory)) {
            messages.add(systemMessage);
            messages.addAll(chantHistory);
            messages.add(userMessage);
        } else {
            messages.add(systemMessage);
            messages.add(userMessage);
        }
        Prompt prompt = new Prompt(messages);
        String content = this.chatClient.prompt(prompt).call().content();
        this.chantHistory.add(userMessage);
        AssistantPromptTemplate assistantPromptTemplate = new AssistantPromptTemplate(content);
        Message assistantMessage = assistantPromptTemplate.createMessage();
        this.chantHistory.add(assistantMessage);
        System.out.println(content);
        return content;
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

    @Operation(summary = "embedChat")
    @Override
    public String embedChat(@RequestParam("text") String text) {
        List<Message> userMessages = Lists.newArrayList(new UserMessage(text));
        List<Document> documents = vectorStore.similaritySearch(
                SearchRequest.query(text).withTopK(4));
        for (Document document : documents) {
            UserMessage userMessage = new UserMessage(document.getContent());
            userMessages.add(userMessage);
        }

//        String systemPrompt = "";
//        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
//        Message systemMessage = systemPromptTemplate.createMessage();
//        userMessages.add(systemMessage);
        Prompt prompt = new Prompt(userMessages);
        return this.chatClient.prompt(prompt).call().content();
    }

    @Override
    public void saveEmbedData(@RequestParam("text") String text) {
        List<Document> documents = List.of(
                new Document(text));
        for (Document document : documents) {
            float[] embed = embeddingModel.embed(document);
            document.setEmbedding(embed);
        }
        vectorStore.add(documents);
    }

}
