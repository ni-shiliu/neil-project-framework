package com.neil.myth.common.bean.entity;

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
public class MythParticipant implements Serializable {

    private static final long serialVersionUID = -210590704127687690L;

    private String transId;

    private String destination;

    private MythInvocation mythInvocation;
}
