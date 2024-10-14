package com.neil.project.ai.service;

import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author nihao
 * @date 2024/10/14
 */
@Component
@RequiredArgsConstructor
public class MemoryAndMongoChatMemory extends InMemoryChatMemory {

    private final MongoDbChatMemory mongoDbChatMemory;


    @Override
    public void add(String conversationId, List<Message> messages) {
        super.add(conversationId, messages);
        mongoDbChatMemory.asyncAddChatMemory(conversationId, messages);
    }

}
