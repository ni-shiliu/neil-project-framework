package com.neil.project;

import com.neil.project.common.BaseResult;
import com.neil.project.order.api.OrderFacade;
import com.neil.project.order.dto.OrderDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao
 * @date 2024/5/6
 */
@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/pc/order/")
public class OrderController {

    private final OrderFacade orderFacade;

    @GetMapping("getByOrderNo")
    public BaseResult<OrderDTO> getByOrderNo(@RequestParam("orderNo") String orderNo) {
        OrderDTO orderDTO = orderFacade.getByOrderNo(orderNo);
        return BaseResult.success(orderDTO);
    }

}
