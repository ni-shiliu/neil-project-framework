package com.neil.project.order.api;

import com.neil.project.order.dto.OrderDTO;
import io.swagger.annotations.Api;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author nihao
 * @date 2024/5/6
 */
@Api(tags = "订单服务")
@FeignClient(
        name = "neil-project-order",
        contextId = "neil-project-order",
        url = "localhost:8090"
)
@RequestMapping("/v1/order/")
public interface OrderFacade {

    @GetMapping("getByOrderNo")
    OrderDTO getByOrderNo(@RequestParam("orderNo") String orderNo);


}
