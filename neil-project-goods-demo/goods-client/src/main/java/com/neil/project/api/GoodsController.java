package com.neil.project.api;

import com.neil.project.goods.dto.GoodsChangeDTO;
import com.neil.project.goods.api.GoodsFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao
 * @date 2024/6/18
 */
@RestController
@RequiredArgsConstructor
public class GoodsController implements GoodsFacade {

    @Override
    public void changeInventory(@RequestBody GoodsChangeDTO goodsChangeDTO) {

    }
}
