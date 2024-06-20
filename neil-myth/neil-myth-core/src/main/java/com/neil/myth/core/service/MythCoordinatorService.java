package com.neil.myth.core.service;

import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.common.exception.MythException;

import java.util.Date;
import java.util.List;

/**
 * @author nihao
 * @date 2024/6/11
 */
public interface MythCoordinatorService {

    void init(MythConfig mythConfig) throws MythException;

    void save(MythTransaction mythTransaction);

    MythTransaction findByTransId(String transId);

    List<MythTransaction> listAllByDelay(Date date);

    boolean remove(String transId);

    int update(MythTransaction mythTransaction) throws MythException;


    void updateFailTransaction(MythTransaction mythTransaction) throws MythException;

    void updateParticipant(MythTransaction mythTransaction) throws MythException;

    int updateStatus(MythTransaction mythTransaction) throws MythException;

}
