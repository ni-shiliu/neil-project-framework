package com.neil.project.order.api;

import com.neil.project.order.dto.OrderDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author nihao
 * @date 2024/5/6
 */
@FeignClient(
        name = "neil-project-order",
        contextId = "neil-project-order",
        url = "${neil.project.order.url}"
)
public interface OrderFacade {

    @GetMapping("/v1/order/getByOrderNo")
    OrderDTO getByOrderNo(@RequestParam("orderNo") String orderNo);


}
