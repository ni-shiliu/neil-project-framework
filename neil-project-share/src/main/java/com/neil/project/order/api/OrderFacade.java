package com.neil.project.order.api;

import com.neil.project.order.dto.OrderDTO;
import com.neil.project.order.dto.OrderQueryDTO;
import com.neil.project.order.dto.OrderSaveDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author nihao
 * @date 2024/5/6
 */
@Tag(name = "订单服务")
@FeignClient(
        name = "neil-project-order",
        contextId = "neil-project-order",
        url = "localhost:8090"
)
public interface OrderFacade {

    @RequestMapping(value = "/v1/order/getByOrderNo", method = RequestMethod.GET)
    OrderDTO getByOrderNo(@RequestParam("orderNo") String orderNo);

    @RequestMapping(value = "/v1/order/list", method = RequestMethod.POST)
    List<OrderDTO> list(@RequestBody OrderQueryDTO orderQueryDTO);

    @Operation(summary = "创建订单")
    @RequestMapping(value = "/v1/order/createOrder", method = RequestMethod.POST)
    OrderDTO createOrder(@RequestBody OrderSaveDTO orderSaveDTO);
}
