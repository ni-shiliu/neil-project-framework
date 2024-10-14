package com.neil.project.ai.mongo;

import org.redisson.api.RList;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * @author nihao
 * @date 2024/10/12
 */
public interface ChatMemoryRepository extends MongoRepository<ChatMemoryEntity, String> {

    // 根据 conversationId 获取最近的 N 条记录
    @Query(value = "{ 'conversationId': ?0 }", sort = "{ '_id': -1 }")
    Optional<List<ChatMemoryEntity>> findTopNByConversationId(String conversationId, Pageable pageable);

}

