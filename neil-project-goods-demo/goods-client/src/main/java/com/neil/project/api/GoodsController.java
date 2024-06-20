package com.neil.project.api;

import cn.hutool.json.JSONUtil;
import com.neil.myth.annotation.MythConsumer;
import com.neil.project.goods.api.GoodsFacade;
import com.neil.project.goods.dto.GoodsChangeDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author nihao
 * @date 2024/6/18
 */
@Slf4j
@RestController
@RequiredArgsConstructor
public class GoodsController implements GoodsFacade {

    @Override
    @MythConsumer
    public void changeInventory(@RequestBody GoodsChangeDTO goodsChangeDTO) {
        log.info("changeInventory success param: {}", JSONUtil.toJsonStr(goodsChangeDTO));
    }

}
