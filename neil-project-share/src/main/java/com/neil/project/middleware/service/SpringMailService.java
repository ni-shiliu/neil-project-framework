package com.neil.project.middleware.service;

import cn.hutool.core.collection.CollUtil;
import com.neil.project.middleware.dto.SpringMailSendAttachmentDTO;
import com.neil.project.middleware.dto.SpringSendMailDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.Objects;

/**
 * @author nihao
 * @date 2024/5/8≤
 */
@Slf4j
public class SpringMailService {

    private final JavaMailSender javaMailSender;

    public SpringMailService(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    public void doSend(SpringSendMailDTO springSendMailDTO) {
        try {
            MimeMessage mimeMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage, true);

            mimeMessageHelper.setFrom(springSendMailDTO.getFrom());
            mimeMessageHelper.setTo(springSendMailDTO.getTo());
            mimeMessageHelper.setSubject(springSendMailDTO.getSubject());
            mimeMessageHelper.setText(Objects.isNull(springSendMailDTO.getText()) ? Strings.EMPTY : springSendMailDTO.getText());
            if (CollUtil.isNotEmpty(springSendMailDTO.getSpringMailSendAttachmentDTOList())) {
                for (SpringMailSendAttachmentDTO springMailSendAttachmentDTO : springSendMailDTO.getSpringMailSendAttachmentDTOList()) {
                    mimeMessageHelper.addAttachment(springMailSendAttachmentDTO.getAttachmentFilename(), springMailSendAttachmentDTO.getFile());
                }
            }
            javaMailSender.send(mimeMessage);

        } catch (MessagingException e) {
            log.error("邮件发送失败：{}", e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
