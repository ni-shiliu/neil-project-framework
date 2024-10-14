package com.neil.project.ai.service.impl;

import cn.hutool.core.collection.CollUtil;
import com.neil.project.ai.dto.AiChatDTO;
import com.neil.project.ai.enums.AiApplicationConfigTypeEnum;
import com.neil.project.ai.mapper.AiApplicationConfigRepository;
import com.neil.project.ai.mapper.entity.AiApplicationConfigEntity;
import com.neil.project.ai.service.AiService;
import com.neil.project.exception.BizException;
import com.neil.project.exception.ErrorCode;
import jakarta.validation.constraints.NotNull;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

/**
 * @author nihao
 * @date 2024/10/10
 */
@Service
public class AiServiceImpl implements AiService {

    private final ChatClient chatClient;

    private final AiApplicationConfigRepository aiApplicationConfigRepository;

    private static final String CHAT_MEMORY_CONVERSATION_ID_KEY_PREFIX = "chat_memory_conversion_history_";


    public AiServiceImpl(ChatClient.Builder chatClientBuilder,
                         ChatMemory chatMemory,
                         AiApplicationConfigRepository aiApplicationConfigRepository) {
        this.chatClient = chatClientBuilder
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory))
                .build();
        this.aiApplicationConfigRepository = aiApplicationConfigRepository;
    }


    @Override
    public Flux<String> streamChat(AiChatDTO aiChatDTO) {
        Optional<List<AiApplicationConfigEntity>> applicationNoOptional =
                aiApplicationConfigRepository.findByApplicationNoAndType(aiChatDTO.getApplicationNo(), AiApplicationConfigTypeEnum.PROMPT);
        if (CollUtil.isEmpty(applicationNoOptional.get())) {
            throw new BizException(ErrorCode.AI_CONFIG_ERROR);
        }

        List<AiApplicationConfigEntity> aiApplicationConfigEntities = applicationNoOptional.get();
        StringBuilder systemPrompt = new StringBuilder();
        for (AiApplicationConfigEntity aiApplicationConfigEntity : aiApplicationConfigEntities) {
            systemPrompt.append(aiApplicationConfigEntity.getContent()).append("\n");
        }
        return chatClient.prompt()
                .system(systemPrompt.toString())
                .user(aiChatDTO.getInput())
                .advisors(a -> a
                        // 指定会话 ID 用于记忆
                        .param(CHAT_MEMORY_CONVERSATION_ID_KEY, generateConversationHistoryKey(aiChatDTO.getUserId()))
                        // 从记忆中检索 5 条历史记录
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 5))
                .stream().content();
    }

    private String generateConversationHistoryKey(@NotNull Long userId) {
        return CHAT_MEMORY_CONVERSATION_ID_KEY_PREFIX + LocalDate.now() + "_" + userId;
    }

}
