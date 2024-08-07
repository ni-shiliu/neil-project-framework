package com.neil.myth.core.service.impl;

import cn.hutool.core.util.StrUtil;
import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.common.exception.MythException;
import com.neil.myth.common.utils.SpringBeanUtil;
import com.neil.myth.core.repository.MythRepository;
import com.neil.myth.core.service.MythApplicationService;
import com.neil.myth.core.service.MythCoordinatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author nihao
 * @date 2024/6/14
 */
@Service
@RequiredArgsConstructor
public class MythCoordinatorServiceImpl implements MythCoordinatorService {

    private final MythApplicationService mythApplicationService;

    private MythRepository mythRepository;

    @Override
    public void init(MythConfig mythConfig) throws MythException {
        mythRepository = SpringBeanUtil.getInstance().getBean(MythRepository.class);
        final String repositorySuffix = getRepositorySuffix(mythConfig.getRepositorySuffix());
        mythRepository.init(repositorySuffix, mythConfig);
    }

    @Override
    public void save(MythTransaction mythTransaction) {
        mythRepository.create(mythTransaction);
    }

    @Override
    public MythTransaction findByTransId(String transId) {
        return mythRepository.findByTransId(transId);
    }

    @Override
    public List<MythTransaction> listAllByDelay(Date date) {
        return null;
    }

    @Override
    public boolean remove(String transId) {
        return false;
    }

    @Override
    public int update(MythTransaction mythTransaction) throws MythException {
        return 0;
    }

    @Override
    public void updateFailTransaction(MythTransaction mythTransaction) throws MythException {
        mythRepository.updateFailTransaction(mythTransaction);
    }

    @Override
    public void updateParticipant(MythTransaction mythTransaction) throws MythException {
        mythRepository.updateParticipant(mythTransaction);
    }

    @Override
    public int updateStatus(MythTransaction mythTransaction) throws MythException {
        return mythRepository.updateStatus(mythTransaction);
    }

    private String getRepositorySuffix(String repositorySuffix) {
        return StrUtil.isNotBlank(repositorySuffix)
                ? repositorySuffix
                : mythApplicationService.getApplicationName();
    }
}
