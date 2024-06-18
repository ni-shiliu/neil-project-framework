package com.neil.project.gateway.impl;

import com.neil.project.gateway.GoodsGateway;
import com.neil.project.goods.api.GoodsFacade;
import com.neil.project.goods.dto.GoodsChangeDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author nihao
 * @date 2024/6/18
 */
@Component
@RequiredArgsConstructor
public class GoodsGatewayImpl implements GoodsGateway {

    private final GoodsFacade goodsFacade;

    @Override
    public void changeInventory(GoodsChangeDTO goodsChangeDTO) {
        goodsFacade.changeInventory(goodsChangeDTO);
    }
}
