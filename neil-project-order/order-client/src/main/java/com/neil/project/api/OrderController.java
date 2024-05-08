package com.neil.project.api;

import com.neil.project.order.api.OrderFacade;
import com.neil.project.order.dto.OrderDTO;
import com.neil.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao
 * @date 2024/5/6
 */
@RestController
@RequiredArgsConstructor
public class OrderController implements OrderFacade {


    private final OrderService orderService;

    @Override
    public OrderDTO getByOrderNo(@RequestParam("orderNo") String orderNo) {
        return orderService.getByOrderNo(orderNo);
    }

}
