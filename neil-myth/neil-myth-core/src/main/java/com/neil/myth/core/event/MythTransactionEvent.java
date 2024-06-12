package com.neil.myth.core.event;

import com.neil.myth.common.bean.entity.MythTransaction;
import com.neil.myth.common.enums.EventTypeEnum;
import lombok.Data;

import java.io.Serializable;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Data
public class MythTransactionEvent implements Serializable {

    private static final long serialVersionUID = 7139088388182520779L;

    private MythTransaction mythTransaction;

    private EventTypeEnum type;

    /**
     * help gc.
     */
    public void clear() {
        mythTransaction = null;
    }
}
