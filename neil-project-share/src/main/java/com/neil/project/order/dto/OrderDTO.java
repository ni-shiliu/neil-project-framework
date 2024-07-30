package com.neil.project.order.dto;

import com.neil.project.common.BaseDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * @author nihao
 * @date 2024/5/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
@Accessors(chain = true)
public class OrderDTO extends BaseDTO {


    @Schema(description = "订单号")
    private String orderNo;

    @Schema(description = "用户id")
    private Long userId;

    @Schema(description = "用户名")
    private String userName;

}
