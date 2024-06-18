package com.neil.project.adapter.controller;

import com.neil.project.common.BaseResult;
import com.neil.project.order.dto.OrderDTO;
import com.neil.project.order.dto.OrderQueryDTO;
import com.neil.project.order.dto.OrderSaveDTO;
import com.neil.project.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author nihao
 * @date 2024/5/6
 */
@Api(tags = "订单模块")
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pc/order/")
public class OrderController {

    private final OrderService orderService;

    @ApiOperation("订单号查询订单")
    @GetMapping("getByOrderNo")
    public BaseResult<OrderDTO> getByOrderNo(@RequestParam("orderNo") String orderNo) {
        OrderDTO orderDTO = orderService.getByOrderNo(orderNo);
        return BaseResult.success(orderDTO);
    }

    @ApiOperation("订单列表")
    @PostMapping("list")
    public BaseResult<List<OrderDTO>> list(@RequestBody OrderQueryDTO orderQueryDTO) {
        List<OrderDTO> orderDTOList = orderService.list(orderQueryDTO);
        return BaseResult.success(orderDTOList);
    }

    @ApiOperation("创建订单")
    @PostMapping("createOrder")
    public BaseResult<OrderDTO> createOrder(@RequestBody OrderSaveDTO orderSaveDTO) {
        OrderDTO orderDTO = orderService.createOrder(orderSaveDTO);
        return BaseResult.success(orderDTO);
    }
}
