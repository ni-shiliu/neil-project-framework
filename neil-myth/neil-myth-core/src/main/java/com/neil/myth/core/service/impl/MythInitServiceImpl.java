package com.neil.myth.core.service.impl;

import cn.hutool.extra.spring.SpringUtil;
import com.neil.myth.common.config.MythConfig;
import com.neil.myth.common.loader.MythSPIClassLoader;
import com.neil.myth.common.serializer.Serializer;
import com.neil.myth.core.repository.MythRepository;
import com.neil.myth.core.service.MythCoordinatorService;
import com.neil.myth.core.service.MythInitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author nihao
 * @date 2024/6/7
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class MythInitServiceImpl implements MythInitService {


    private final MythCoordinatorService mythCoordinatorService;

    @Override
    public void initMyth(MythConfig mythConfig) {
        loadSpiRepositorySupport(mythConfig);
        mythCoordinatorService.init(mythConfig);
    }

    private void loadSpiRepositorySupport(MythConfig mythConfig) {
        // spi serializer and register
        final Serializer serializer = MythSPIClassLoader.getMythSPIClassLoader(Serializer.class)
                .getActivateMythSPIClazz(mythConfig.getSerializer());
        SpringUtil.registerBean(Serializer.class.getName(), serializer);

        // spi repository and register
        final MythRepository mythRepository = MythSPIClassLoader.getMythSPIClassLoader(MythRepository.class)
                .getActivateMythSPIClazz(mythConfig.getRepositorySupport());
        SpringUtil.registerBean(MythRepository.class.getName(), mythRepository);
    }

}
