package com.neil.project.security;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.neil.project.exception.BizException;
import com.neil.project.exception.ErrorCode;
import com.neil.project.security.token.JwtApplicationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.logging.log4j.util.Strings;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import static com.neil.project.common.Constants.*;

/**
 * @author nihao
 * @date 2024/8/22
 */
@Slf4j
@Getter
@RequiredArgsConstructor
public class SignAuthenticationFilter extends OncePerRequestFilter {

    private final String secret;


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        RequestWrapper requestWrapper = new RequestWrapper(request);

        JwtApplicationToken authentication = (JwtApplicationToken) SecurityContextHolder.getContext().getAuthentication();
        if (Objects.nonNull(authentication)) {
            doCheckSignature(requestWrapper);
        }
        filterChain.doFilter(requestWrapper, response);

    }

    private void doCheckSignature(RequestWrapper requestWrapper) {
        String sign = requestWrapper.getHeader(SIGN);
        String timestampStr = requestWrapper.getHeader(TIMESTAMP);
        String nonce = requestWrapper.getHeader(NONCE);

        if (Strings.isBlank(sign) || Strings.isBlank(timestampStr) || Strings.isBlank(nonce)) {
            throw new BizException(ErrorCode.MISSING_SERVLET_REQUEST_PARAMETER);
        }
        Long timestamp = Long.valueOf(timestampStr);
        long nowTimestamp = System.currentTimeMillis();
        if (nowTimestamp - timestamp >= 1000000) {
//             超过10s, 请求失效
            throw new BizException(ErrorCode.REQUEST_EXPIRED);
        }
        // 防重放攻击 将nonce放到缓存，并设置失效时间
        // todo: implement

        String signature = null;
        try {
            String method = requestWrapper.getMethod();
            if (GET.equalsIgnoreCase(method)) {
                String queryString = requestWrapper.getQueryString();
                String decodeQueryString = URLUtil.decode(queryString);
                String sortedQueryString = processQueryString(decodeQueryString);
                signature = generateSignature(sortedQueryString, timestamp, nonce);
            } else if (POST.equalsIgnoreCase(method)) {
                String body = getRequestBody(requestWrapper);
                signature = generateSignature(body, timestamp, nonce);
            } else if (PUT.equalsIgnoreCase(method)
                    || DELETE.equalsIgnoreCase(method)) {
                // todo: implement
            } else {
                throw new BizException(ErrorCode.HTTP_METHOD_NOT_ALLOWED);
            }
        } catch (Exception e) {
            log.error("生成sign失败", e);
        }
        if (!sign.equalsIgnoreCase(signature)) {
            throw new BizException(ErrorCode.INVALID_SIGN);
        }
    }

    private String processQueryString(String queryString) {
        if (Strings.isBlank(queryString)) {
            return queryString;
        }
        // 将查询字符串解析为Map
        Map<String, String> map = Arrays.stream(queryString.split("&"))
                .map(param -> param.split("="))
                .filter(param -> param.length == 2 && StrUtil.isNotBlank(param[1]))
                .collect(Collectors.toMap(param -> param[0], param -> param[1], (v1, v2) -> v1));
        return assembleSortParam(map);
    }

    private String getRequestBody(HttpServletRequest request) throws IOException {
        // 包装请求，缓存请求体
        // 读取请求体并转换为字符串
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = request.getReader()) {
            String line;
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
            }
        }
        String requestBody = stringBuilder.toString();
        // 解析请求体为Map<String, String>
        ObjectMapper objectMapper = new ObjectMapper();
        if (Strings.isBlank(requestBody)) {
            return null;
        }
        Map<String, String> map = objectMapper.readValue(requestBody, new TypeReference<>() {
        });

        return assembleSortParam(map);
    }

    private static String assembleSortParam(Map<String, String> map) {
        // 按键排序并构建返回的字符串
        return map.entrySet().stream()
                .filter(entry -> StrUtil.isNotBlank(entry.getValue()))
                .sorted(Map.Entry.comparingByKey())
                .map(entry -> entry.getKey() + "=" + entry.getValue())
                .collect(Collectors.joining("&"));
    }

    private String generateSignature(String queryString, Long timestamp, String nonce) throws NoSuchAlgorithmException, InvalidKeyException {
        if (Strings.isBlank(queryString)) {
            queryString = "nonce=" + nonce + "&timestamp=" + timestamp;
        } else {
            queryString += "&nonce=" + nonce + "&timestamp=" + timestamp;
        }
        Mac mac = Mac.getInstance(HMAC_ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secret.getBytes(), HMAC_ALGORITHM);
        mac.init(secretKeySpec);
        byte[] hmacBytes = mac.doFinal(queryString.getBytes());
        return Base64.getEncoder().encodeToString(hmacBytes);
    }
}
