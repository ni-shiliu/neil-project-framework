package com.neil.project.adapter.controller;

import com.neil.project.common.BaseResult;
import com.neil.project.order.dto.OrderDTO;
import com.neil.project.service.OrderService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao
 * @date 2024/5/6
 */
@Api("订单模块")
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

}
