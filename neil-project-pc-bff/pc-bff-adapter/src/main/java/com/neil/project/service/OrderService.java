package com.neil.project.service;

import com.neil.project.order.dto.OrderDTO;

/**
 * @author nihao
 * @date 2024/5/16
 */
public interface OrderService {
    OrderDTO getByOrderNo(String orderNo);
}
