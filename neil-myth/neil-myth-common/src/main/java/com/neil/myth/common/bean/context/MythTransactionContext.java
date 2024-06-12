package com.neil.myth.common.bean.context;

import com.neil.myth.common.enums.MythRoleEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Data
public class MythTransactionContext implements Serializable {

    private static final long serialVersionUID = 7804330646310570303L;

    private String transId;

    private MythRoleEnum mythRole;

}
