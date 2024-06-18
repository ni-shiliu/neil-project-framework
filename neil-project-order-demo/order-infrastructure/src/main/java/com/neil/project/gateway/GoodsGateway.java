package com.neil.project.gateway;

import com.neil.project.goods.dto.GoodsChangeDTO;

/**
 * @author nihao
 * @date 2024/6/18
 */
public interface GoodsGateway {
    void changeInventory(GoodsChangeDTO goodsChangeDTO);
}
