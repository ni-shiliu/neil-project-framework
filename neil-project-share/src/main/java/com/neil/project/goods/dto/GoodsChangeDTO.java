package com.neil.project.goods.dto;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author nihao
 * @date 2024/6/18
 */
@Data
@Accessors(chain = true)
public class GoodsChangeDTO {

    private String goodsId;
}
