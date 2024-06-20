package com.neil.myth.common.bean.mq;

import com.neil.myth.common.bean.entity.MythInvocation;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MessageEntity implements Serializable {


    private static final long serialVersionUID = -5559013750264598612L;

    private String transId;

    private MythInvocation mythInvocation;
}
