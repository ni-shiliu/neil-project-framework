package com.neil.myth.core.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.core.repository.MythRepository;
import com.neil.myth.core.service.MythApplicationService;
import com.neil.myth.core.service.MythRepositoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Service
@RequiredArgsConstructor
public class MythRepositoryServiceImpl implements MythRepositoryService {

    private final MythApplicationService mythApplicationService;

    private MythRepository mythRepository;

    @Override
    public void init(MythConfig mythConfig) {
        mythRepository = SpringUtil.getBean(MythRepository.class);
        final String repositorySuffix = getRepositorySuffix(mythConfig.getRepositorySuffix());
        mythRepository.init(repositorySuffix, mythConfig);
    }

    private String getRepositorySuffix(String repositorySuffix) {
        return StrUtil.isNotBlank(repositorySuffix)
                ? repositorySuffix
                : mythApplicationService.getApplicationName();
    }
}
