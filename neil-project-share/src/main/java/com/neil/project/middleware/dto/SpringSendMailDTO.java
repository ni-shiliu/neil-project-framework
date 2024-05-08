package com.neil.project.middleware.dto;

import lombok.Data;

import java.util.List;

/**
 * @author nihao
 * @date 2024/5/8
 */
@Data
public class SpringSendMailDTO {

    private String from;
    private String[] to;
    private String subject;
    private String text;
    private List<SpringMailSendAttachmentDTO> springMailSendAttachmentDTOList;

}

