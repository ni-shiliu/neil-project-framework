package com.neil.myth.core.repository;

import cn.hutool.core.lang.TypeReference;
import cn.hutool.json.JSONUtil;
import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.neil.myth.annotation.MythSPI;
import com.neil.myth.common.bean.entity.MythInvocation;
import com.neil.myth.common.bean.entity.MythParticipant;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.bean.entity.mongodb.MongodbAdapter;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.common.config.MythMongoDbConfig;
import com.neil.myth.common.constant.CommonConstant;
import com.neil.myth.common.exception.MythException;
import com.neil.myth.common.serializer.Serializer;
import com.neil.myth.common.utils.RepositoryPathUtil;
import org.springframework.beans.BeanUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.List;
import java.util.Objects;

/**
 * @author nihao
 * @date 2024/6/24
 */
@MythSPI("mongodb")
public class MongoRepository implements MythRepository {

    private Serializer serializer;

    private MongoTemplate template;

    private String collectionName;



    @Override
    public void setSerializer(Serializer serializer) {
        this.serializer = serializer;
    }

    @Override
    public int create(MythTransaction mythTransaction) {
        try {
            MongodbAdapter mongoBean = new MongodbAdapter();
            BeanUtils.copyProperties(mythTransaction, mongoBean);
            mongoBean.setParticipants(JSONUtil.toJsonStr(mythTransaction.getParticipants()));
            if (Objects.nonNull(mythTransaction.getInvocation())) {
                final byte[] invocation = serializer.serialize(mythTransaction.getInvocation());
                mongoBean.setInvocation(invocation);
            }
            template.save(mongoBean, collectionName);
            return CommonConstant.SUCCESS;
        } catch (MythException e) {
            e.printStackTrace();
            return CommonConstant.ERROR;
        }
    }

    @Override
    public void updateFailTransaction(MythTransaction mythTransaction) throws MythException {
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(mythTransaction.getTransId()));
        Update update = new Update();
        update.set("status", mythTransaction.getStatus());
        update.set("errorMsg", mythTransaction.getErrorMsg());
        update.set("gmtModified", LocalDateTime.now());
        update.set("retryCount", mythTransaction.getRetryCount());
        template.updateFirst(query, update, MongodbAdapter.class, collectionName);
    }

    @Override
    public void updateParticipant(MythTransaction mythTransaction) throws MythException {
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(mythTransaction.getTransId()));
        Update update = new Update();
        update.set("participants", JSONUtil.toJsonStr(mythTransaction.getParticipants()));
        template.updateFirst(query, update, MongodbAdapter.class, collectionName);
    }

    @Override
    public int updateStatus(MythTransaction mythTransaction) throws MythException {
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(mythTransaction.getTransId()));
        Update update = new Update();
        update.set("status", mythTransaction.getStatus());
        update.set("gmtModified", LocalDateTime.now());
        update.set("retryCount", mythTransaction.getRetryCount());
        template.updateFirst(query, update, MongodbAdapter.class, collectionName);
        return CommonConstant.SUCCESS;
    }

    @Override
    public MythTransaction findByTransId(String transId) {
        Query query = new Query();
        query.addCriteria(new Criteria("transId").is(transId));
        MongodbAdapter cache = template.findOne(query, MongodbAdapter.class, collectionName);
        if (Objects.isNull(cache)) {
            return null;
        }
        return buildByResultMap(cache);
    }

    private MythTransaction buildByResultMap(MongodbAdapter cache) {
        MythTransaction mythTransaction = new MythTransaction();
        BeanUtils.copyProperties(cache, mythTransaction);
        if (Objects.nonNull(cache.getInvocation())) {
            MythInvocation mythInvocation = serializer.deSerialize(cache.getInvocation(), MythInvocation.class);
            mythTransaction.setInvocation(mythInvocation);
        }
        mythTransaction.setParticipants(JSONUtil.toBean(cache.getParticipants(),
                new TypeReference<List<MythParticipant>>() {}, false));
        return mythTransaction;
    }

    @Override
    public List<MythTransaction> listAllByDelay(Date date) {
        return null;
    }

    @Override
    public void init(String modelName, MythConfig mythConfig) throws MythException {
        collectionName = RepositoryPathUtil.buildDbTableName(modelName);
        MythMongoDbConfig mongoDbConfig = mythConfig.getMythMongoDbConfig();
        try {
            MongoClient mongoClient = MongoClients.create(mongoDbConfig.getMongoUrl());
            template = new MongoTemplate(mongoClient, mongoDbConfig.getMongoDbName());
        } catch (Exception e) {
            e.printStackTrace();
            throw new MythException(e);
        }
    }

}
