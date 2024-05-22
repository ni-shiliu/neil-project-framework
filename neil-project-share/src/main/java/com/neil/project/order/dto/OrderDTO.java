package com.neil.project.order.dto;

import com.neil.project.common.BaseDTO;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author nihao
 * @date 2024/5/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderDTO extends BaseDTO {


    @ApiModelProperty("订单号")
    private String orderNo;

    @ApiModelProperty("用户id")
    private Long userId;

    @ApiModelProperty("用户名")
    private String userName;

}
