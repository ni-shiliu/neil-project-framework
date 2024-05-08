package com.neil.project.middleware.dto;

import lombok.Data;

import java.io.File;

@Data
public class SpringMailSendAttachmentDTO {

    private String attachmentFilename;

    private File file;
}
