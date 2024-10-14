package com.neil.project.service;

import com.neil.project.ai.dto.AiChatDTO;

/**
 * @author nihao
 * @date 2024/8/13
 */
public interface AiService {


    void streamChat(AiChatDTO aiChatDTO);

}
