package com.neil.myth.common.utils;

import cn.hutool.core.lang.Assert;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author nihao
 * @date 2024/7/31
 */
public class SpringBeanUtil {

    private static final SpringBeanUtil INSTANCE = new SpringBeanUtil();

    private ConfigurableApplicationContext cfgContext;

    private SpringBeanUtil() {
        if (INSTANCE != null) {
            throw new Error("error");
        }
    }

    public static SpringBeanUtil getInstance() {
        return INSTANCE;
    }

    /**
     * acquire spring bean.
     *
     * @param type type
     * @param <T>  class
     * @return bean
     */
    public <T> T getBean(final Class<T> type) {
        Assert.notNull(type);
        return cfgContext.getBean(type);
    }

    public void registerBean(final String beanName, final Object obj) {
        Assert.notNull(beanName);
        Assert.notNull(obj);
        cfgContext.getBeanFactory().registerSingleton(beanName, obj);
    }

    public void setCfgContext(final ConfigurableApplicationContext cfgContext) {
        this.cfgContext = cfgContext;
    }

    public String getApplicationName() {
        return getProperty("spring.application.name");
    }

    private String getProperty(String key) {
        return null == cfgContext ? null : cfgContext.getEnvironment().getProperty(key);
    }
}
