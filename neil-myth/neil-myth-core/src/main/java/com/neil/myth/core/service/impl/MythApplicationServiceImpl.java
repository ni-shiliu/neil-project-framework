package com.neil.myth.core.service.impl;

import cn.hutool.core.util.RandomUtil;
import com.neil.myth.common.utils.SpringBeanUtil;
import com.neil.myth.core.service.MythApplicationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author nihao
 * @date 2024/6/11
 */
@Service
@RequiredArgsConstructor
public class MythApplicationServiceImpl implements MythApplicationService {

    private static final String DEFAULT_APPLICATION_NAME = "myth";


    @Override
    public String getApplicationName() {
        return Optional.ofNullable(SpringBeanUtil.getInstance().getApplicationName()).orElse(generateDefaultApplicationName());
    }

    private String generateDefaultApplicationName() {
        return DEFAULT_APPLICATION_NAME + RandomUtil.randomNumbers(10);
    }

}
