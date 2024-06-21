package com.neil.myth.core.repository;

import com.neil.myth.annotation.MythSPI;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.common.exception.MythException;
import com.neil.myth.common.serializer.Serializer;

import java.util.Date;
import java.util.List;

/**
 * @author nihao
 * @date 2024/6/11
 */
@MythSPI
public interface MythRepository {

    void setSerializer(Serializer serializer);

    int create(MythTransaction mythTransaction);

    void updateFailTransaction(MythTransaction mythTransaction) throws MythException;

    void updateParticipant(MythTransaction mythTransaction) throws MythException;

    int updateStatus(MythTransaction mythTransaction) throws MythException;

    MythTransaction findByTransId(String transId);

    List<MythTransaction> listAllByDelay(Date date);

    void init(String modelName, MythConfig mythConfig) throws MythException;

}
