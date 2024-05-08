package com.neil.project.service;

import com.neil.project.order.dto.OrderDTO;

/**
 * @author nihao
 * @date 2024/5/8
 */
public interface OrderService {
    OrderDTO getByOrderNo(String orderNo);

}
