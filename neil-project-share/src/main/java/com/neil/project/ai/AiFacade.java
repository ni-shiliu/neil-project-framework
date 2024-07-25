package com.neil.project.ai;

import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author nihao
 * @date 2024/7/25
 */
@Api(tags = "AI服务")
@FeignClient(
        name = "neil-project-ai",
        contextId = "neil-project-ai",
        url = "localhost:8093"
)
@RequestMapping("/v1/ai")
public interface AiFacade {
}
