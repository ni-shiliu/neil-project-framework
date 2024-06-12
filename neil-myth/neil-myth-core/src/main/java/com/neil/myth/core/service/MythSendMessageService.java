package com.neil.myth.core.service;

import com.neil.myth.common.bean.entity.MythTransaction;

/**
 * @author nihao
 * @date 2024/6/12
 */
public interface MythSendMessageService {

    Boolean sendMessage(MythTransaction mythTransaction);

}
