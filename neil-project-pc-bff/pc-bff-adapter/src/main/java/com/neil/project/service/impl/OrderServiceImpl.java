package com.neil.project.service.impl;

import com.neil.project.order.api.OrderFacade;
import com.neil.project.order.dto.OrderDTO;
import com.neil.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author nihao
 * @date 2024/5/16
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderFacade orderFacade;

    @Override
    public OrderDTO getByOrderNo(String orderNo) {
        return orderFacade.getByOrderNo(orderNo);
    }
}
