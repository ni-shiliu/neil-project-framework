package com.neil.project.exception;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neil.project.common.BaseResult;
import com.neil.project.config.JacksonConfig;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

/**
 * @author nihao
 * @date 2024/5/23
 */
@Slf4j
public class BizExceptionDecoder implements ErrorDecoder {

    private final ErrorDecoder defaultDecoder = new ErrorDecoder.Default();

    private final ObjectMapper om = new JacksonConfig().getObjectMapper();

    @Override
    public Exception decode(String methodKey, Response response) {
        int min5xx = 500;
        int max5xx = 599;
        if (response.status() >= min5xx && response.status() <= max5xx) {
            try {
                if (response.body() != null) {
                    String body = IOUtils.toString(response.body().asReader(StandardCharsets.UTF_8));
                    BaseResult baseResult = om.readValue(body, BaseResult.class);
                    return new BizException(baseResult.getCode(), baseResult.getErrorMessage());
                }
            } catch (IOException ex) {
                //don't need to catch
            }
        }

        return defaultDecoder.decode(methodKey, response);
    }
}
