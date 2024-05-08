package com.neil.project.order.dto;

import com.neil.project.common.BaseDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author nihao
 * @date 2024/5/6
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OrderDTO extends BaseDTO {

    private String orderNo;

    private Long userId;

    private String userName;

}
