package com.neil.project.goods.api;

import com.neil.myth.annotation.Myth;
import com.neil.project.goods.dto.GoodsChangeDTO;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * @author nihao
 * @date 2024/6/18
 */
@Tag(name = "商品服务")
@FeignClient(
        name = "neil-project-goods",
        contextId = "neil-project-goods",
        url = "localhost:8092"
)
public interface GoodsFacade {

    @Operation(summary = "变更库存")
    @RequestMapping(value = "/v1/goods/changeInventory", method = RequestMethod.POST)
    @Myth(destination = "MYTH-GOODS")
    void changeInventory(@RequestBody GoodsChangeDTO goodsChangeDTO);

}
