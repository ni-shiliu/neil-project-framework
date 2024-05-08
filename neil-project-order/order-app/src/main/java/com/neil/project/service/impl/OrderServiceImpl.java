package com.neil.project.service.impl;

import com.neil.project.gateway.UserGateway;
import com.neil.project.mapper.OrderMapper;
import com.neil.project.mapper.model.OrderDO;
import com.neil.project.order.dto.OrderDTO;
import com.neil.project.service.OrderService;
import com.neil.project.user.dto.UserDTO;
import com.neil.project.utils.CommonAssembler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * @author nihao
 * @date 2024/5/8
 */
@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final UserGateway userGateway;

    private final OrderMapper orderMapper;

    @Override
    public OrderDTO getByOrderNo(String orderNo) {
        OrderDO orderDO = orderMapper.getByOrderNo();
        OrderDTO orderDTO = CommonAssembler.toDto(orderDO, OrderDTO.class);
        UserDTO userDTO = userGateway.getUserById(orderDTO.getUserId());
        orderDTO.setUserName(userDTO.getUserName());
        return orderDTO;
    }

}
