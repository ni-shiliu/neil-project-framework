package com.neil.project.service.impl;

import com.neil.project.order.dto.OrderDTO;
import com.neil.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author nihao
 * @date 2024/5/8
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

//    private final UserGateway userGateway;
//
//    private final OrderMapper orderMapper;

    @Override
    public OrderDTO getByOrderNo(String orderNo) {
//        OrderDO orderDO = orderMapper.getByOrderNo();
//        OrderDTO orderDTO = CommonAssembler.toDto(orderDO, OrderDTO.class);
//        UserDTO userDTO = userGateway.getUserById(orderDTO.getUserId());
//        orderDTO.setUserName(userDTO.getUserName());
        return new OrderDTO()
                .setUserId(1L)
                .setOrderNo("aa")
                .setUserId(1L)
                .setUserName("neil");
    }

}
