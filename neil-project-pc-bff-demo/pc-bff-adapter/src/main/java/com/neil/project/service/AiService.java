package com.neil.project.service;

import com.neil.project.ai.dto.AiChatDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author nihao
 * @date 2024/8/13
 */
public interface AiService {

    String chat(String text);

    void streamChat(AiChatDTO aiChatDTO);

    void streamMultimodal(MultipartFile image, String text);
}
