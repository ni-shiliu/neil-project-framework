package com.neil.project.api;

import com.neil.project.exception.BizException;
import com.neil.project.exception.ErrorCode;
import com.neil.project.order.api.OrderFacade;
import com.neil.project.order.dto.OrderDTO;
import com.neil.project.order.dto.OrderQueryDTO;
import com.neil.project.order.dto.OrderSaveDTO;
import com.neil.project.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

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

    @Override
    public List<OrderDTO> list(@RequestBody OrderQueryDTO orderQueryDTO) {
        throw new BizException(ErrorCode.FILE_MAX_UPLOAD_SIZE_EXCEEDED);
    }

    @Override
    public OrderDTO createOrder(@RequestBody OrderSaveDTO orderSaveDTO) {
        return orderService.createOrder(orderSaveDTO);
    }

}
