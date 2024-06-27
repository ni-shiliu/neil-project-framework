package com.neil.myth.common.bean.entity.mongodb;

import com.neil.myth.common.enums.MythRoleEnum;
import com.neil.myth.common.enums.MythStatusEnum;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author nihao
 * @date 2024/6/24
 */
@Data
@NoArgsConstructor
public class MongodbAdapter implements Serializable {

    private static final long serialVersionUID = 4449258384695030112L;

    @Id
    private String id;

    private String transId;

    private MythStatusEnum status;

    private Integer retryCount;

    private MythRoleEnum role;

    private LocalDateTime gmtCreated;

    private LocalDateTime gmtModified;

    private String targetClass;

    private String targetMethod;

    private String args;

    private String errorMsg;

    private byte[] invocation;

    private String participants;

}
