package com.neil.project.order.api;

import com.neil.project.order.dto.OrderDTO;
import com.neil.project.order.dto.OrderQueryDTO;
import com.neil.project.order.dto.OrderSaveDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
@RequestMapping("/v1/order")
public interface OrderFacade {

    @GetMapping("getByOrderNo")
    OrderDTO getByOrderNo(@RequestParam("orderNo") String orderNo);

    @PostMapping("list")
    List<OrderDTO> list(@RequestBody OrderQueryDTO orderQueryDTO);

    @ApiOperation("创建订单")
    @PostMapping("createOrder")
    OrderDTO createOrder(@RequestBody OrderSaveDTO orderSaveDTO);
}
