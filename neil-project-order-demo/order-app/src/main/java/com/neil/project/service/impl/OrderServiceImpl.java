package com.neil.project.service.impl;

import com.neil.myth.annotation.Myth;
import com.neil.project.gateway.GoodsGateway;
import com.neil.project.gateway.UserGateway;
import com.neil.project.goods.dto.GoodsChangeDTO;
import com.neil.project.order.dto.OrderDTO;
import com.neil.project.order.dto.OrderSaveDTO;
import com.neil.project.service.OrderService;
import com.neil.project.user.dto.UserSaveDTO;
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
//
//    private final OrderMapper orderMapper;

    private final GoodsGateway goodsGateway;


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

    @Override
    @Myth
    public OrderDTO createOrder(OrderSaveDTO orderSaveDTO) {
        goodsGateway.changeInventory(new GoodsChangeDTO());
        userGateway.registerUser(new UserSaveDTO().setUserName("neil"));
        return null;
    }

}
