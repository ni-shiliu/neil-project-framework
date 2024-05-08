/**
 * @author nihao
 * @date 2024/5/8
 *
 * 若需要使用某些中间件，需要加入对应的配置
 * 例：SpringMailConfig
 * @Slf4j
 * @Configuration(proxyBeanMethods = false)
 * @ConditionalOnProperty(prefix = "spring.mail", value = "enable", havingValue = "true")
 * @ConfigurationProperties(prefix = "spring.mail")
 * @Data
 * @RequiredArgsConstructor
 * public class SpringEmailConfig {
 *
 * }
 * 根据注解@ConditionalOnProperty(prefix = "spring.mail", value = "enable", havingValue = "true")
 * 需要添加对应配置：spring.mail=true
 */
package com.neil.project.middleware;
