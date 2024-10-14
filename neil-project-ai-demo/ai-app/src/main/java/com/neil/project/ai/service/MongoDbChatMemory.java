package com.neil.project.ai.service;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.collect.Lists;
import com.neil.project.ai.mongo.ChatMemoryEntity;
import com.neil.project.ai.mongo.ChatMemoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author nihao
 * @date 2024/10/12
 */
@Component
@RequiredArgsConstructor
public class MongoDbChatMemory {

    private final ChatMemoryRepository chatMemoryRepository;

    public void asyncAddChatMemory(String conversationId, List<Message> messages) {
        if (CollUtil.isEmpty(messages) || StrUtil.isBlank(conversationId)) {
            return;
        }
        List<ChatMemoryEntity> chatMemoryEntities = Lists.newArrayList();
        for (Message message : messages) {
            ChatMemoryEntity chatMemory = new ChatMemoryEntity();
            chatMemory.setConversationId(conversationId);
            chatMemory.setMessage(message);
            chatMemoryEntities.add(chatMemory);
        }
        chatMemoryRepository.saveAll(chatMemoryEntities);
    }
}

