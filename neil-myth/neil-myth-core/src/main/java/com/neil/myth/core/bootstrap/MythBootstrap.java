package com.neil.myth.core.bootstrap;

import com.neil.myth.common.config.MythConfig;
import com.neil.myth.core.service.MythInitService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * @author nihao
 * @date 2024/6/7
 */
@Slf4j
@RequiredArgsConstructor
public class MythBootstrap implements ApplicationContextAware {

    private final MythInitService mythInitService;

    private final MythConfig mythConfig;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        log.info("neil-myth start init");
        try {
            this.init();
        } catch (Exception e) {
            log.error("Myth init fail: {}", e.getMessage());
            throw e;
        }
        log.info("neil-myth init success");
    }

    private void init() {
        mythInitService.initMyth(this.mythConfig);
    }
}
