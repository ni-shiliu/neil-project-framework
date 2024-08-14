package com.neil.project.service;

import org.springframework.web.multipart.MultipartFile;

/**
 * @author nihao
 * @date 2024/8/13
 */
public interface AiService {
    void streamChat(String text);

    void streamMultimodal(MultipartFile image, String text);
}
