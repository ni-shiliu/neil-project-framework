package com.neil.project.ai;

import lombok.Data;

import java.io.Serial;
import java.io.Serializable;

/**
 * @author nihao
 * @date 2024/8/13
 */
@Data
public class NeilPromptDTO implements Serializable {

        @Serial
        private static final long serialVersionUID = -7119941441737849012L;

        private byte[] image;

        private String text;

}
