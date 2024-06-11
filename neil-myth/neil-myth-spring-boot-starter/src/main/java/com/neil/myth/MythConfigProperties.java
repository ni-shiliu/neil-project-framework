package com.neil.myth;

import com.neil.myth.common.config.MythConfig;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Myth 配置类
 *
 * @author nihao
 * @date 2024/6/7
 */
@Component
@ConfigurationProperties(prefix = "neil.myth")
public class MythConfigProperties extends MythConfig {

}
