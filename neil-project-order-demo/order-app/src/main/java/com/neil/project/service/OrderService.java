package com.neil.project.service;

import com.neil.project.order.dto.OrderDTO;
import com.neil.project.order.dto.OrderSaveDTO;

/**
 * @author nihao
 * @date 2024/5/8
 */
public interface OrderService {
    OrderDTO getByOrderNo(String orderNo);

    OrderDTO createOrder(OrderSaveDTO orderSaveDTO);
}
