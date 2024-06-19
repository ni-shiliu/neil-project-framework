package com.neil.myth.common.bean.entity;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.neil.myth.common.enums.MythRoleEnum;
import com.neil.myth.common.enums.MythStatusEnum;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Data
public class MythTransaction implements Serializable {


    private static final long serialVersionUID = 8756520926024193343L;

    private Long id;

    private String transId;

    private MythStatusEnum status;

    private MythRoleEnum role;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    private String targetClass;

    private String targetMethod;

    private String args;

    private String errorMsg;

    private List<MythParticipant> participants;

    public MythTransaction() {
        this.transId = IdUtil.fastUUID();
        this.gmtCreated = LocalDateTime.now();
        this.gmtModified = LocalDateTime.now();
        participants = Lists.newCopyOnWriteArrayList();
    }

    public MythTransaction(final String transId) {
        this.transId = transId;
        this.gmtCreated = LocalDateTime.now();
        this.gmtModified = LocalDateTime.now();
        participants = Lists.newCopyOnWriteArrayList();
    }

    public void registerParticipant(final MythParticipant mythParticipant) {
        participants.add(mythParticipant);
    }

}
