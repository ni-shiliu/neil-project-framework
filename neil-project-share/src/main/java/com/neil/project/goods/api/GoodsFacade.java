package com.neil.project.goods.api;

import com.neil.myth.annotation.Myth;
import com.neil.project.goods.dto.GoodsChangeDTO;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author nihao
 * @date 2024/6/18
 */
@Api(tags = "商品服务")
@FeignClient(
        name = "neil-project-goods",
        contextId = "neil-project-goods",
        url = "localhost:8092"
)
@RequestMapping("/v1/goods")
public interface GoodsFacade {

    @ApiOperation("变更库存")
    @PostMapping("changeInventory")
    @Myth(destination = "MYTH-GOODS", tags = "goods")
    void changeInventory(@RequestBody GoodsChangeDTO goodsChangeDTO);

}
