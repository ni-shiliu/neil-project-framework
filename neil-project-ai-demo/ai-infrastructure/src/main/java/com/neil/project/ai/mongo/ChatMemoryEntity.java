package com.neil.project.ai.mongo;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neil.project.utils.ObjectMapperUtil;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.SneakyThrows;
import org.springframework.ai.chat.messages.Message;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author nihao
 * @date 2024/10/12
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Document("chat_memory")
public class ChatMemoryEntity extends AuditMetadata implements Serializable {

    @Serial
    private static final long serialVersionUID = 7348072558666553804L;

    private static final ObjectMapper om = ObjectMapperUtil.getInstance();

    @Indexed
    private String conversationId;

    private String message;

    private String messageClass;

    // 存储消息
    @SneakyThrows
    public void setMessage(Message message) {
        this.message = om.writeValueAsString(message);
        this.messageClass = message.getClass().getName();
    }
}
