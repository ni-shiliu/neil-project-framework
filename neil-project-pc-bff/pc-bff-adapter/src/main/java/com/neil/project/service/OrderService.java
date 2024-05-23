package com.neil.project.service;

import com.neil.project.order.dto.OrderDTO;
import com.neil.project.order.dto.OrderQueryDTO;

import java.util.List;

/**
 * @author nihao
 * @date 2024/5/16
 */
public interface OrderService {
    OrderDTO getByOrderNo(String orderNo);

    List<OrderDTO> list(OrderQueryDTO orderQueryDTO);
}
