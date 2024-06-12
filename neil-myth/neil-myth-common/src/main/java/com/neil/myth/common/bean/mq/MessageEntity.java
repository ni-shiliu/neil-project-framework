package com.neil.myth.common.bean.mq;

import com.neil.myth.common.bean.entity.MythInvocation;
import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Data
@AllArgsConstructor
public class MessageEntity {


    private String transId;

    private MythInvocation mythInvocation;
}
