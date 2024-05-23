package com.neil.project.compont;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.neil.project.utils.IpUtil;
import lombok.extern.slf4j.Slf4j;
import lombok.val;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.*;

/**
 * @author nihao
 * @date 2024/5/23
 */
@Slf4j
public class RequestAndResponseLoggingFilter extends OncePerRequestFilter {

    private static final List<MediaType> VISIBLE_TYPES = Arrays.asList(
            MediaType.valueOf("text/*"),
            MediaType.APPLICATION_FORM_URLENCODED,
            MediaType.APPLICATION_JSON,
            MediaType.APPLICATION_XML,
            MediaType.valueOf("application/*+json"),
            MediaType.valueOf("application/*+xml")
    );

    private static final List<String> LOG_URL_PATTERN = Arrays.asList(
            "/v1/**",
            "/api/**"
    );

    private static final AntPathMatcher antPathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        response.setCharacterEncoding("UTF-8");

        if (isAsyncDispatch(request)) {
            filterChain.doFilter(request, response);
        } else {
            doFilterWrapped(wrapRequest(request), wrapResponse(response), filterChain);
        }
    }

    private static ContentCachingRequestWrapper wrapRequest(HttpServletRequest request) {
        return request instanceof ContentCachingRequestWrapper
                ? (ContentCachingRequestWrapper) request :
                new ContentCachingRequestWrapper(request);
    }

    private static ContentCachingResponseWrapper wrapResponse(HttpServletResponse response) {
        return response instanceof ContentCachingResponseWrapper
                ? (ContentCachingResponseWrapper) response
                : new ContentCachingResponseWrapper(response);
    }

    protected void doFilterWrapped(ContentCachingRequestWrapper request,
                                   ContentCachingResponseWrapper response,
                                   FilterChain filterChain) throws ServletException, IOException {
        try {
            beforeRequest(request, response);
            filterChain.doFilter(request, response);
        } finally {
            afterRequest(request, response);
            response.copyBodyToResponse();
        }
    }

    private void beforeRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (log.isInfoEnabled() && shouldLog(request)) {
            logRequestHeader(request, request.getRemoteAddr() + "|>");
        }
    }

    private boolean shouldLog(ContentCachingRequestWrapper request) {
        String path = ((HttpServletRequest) request).getRequestURI();
        for (String pattern : LOG_URL_PATTERN) {
            if (antPathMatcher.match(pattern, path)) {
                return true;
            }
        }

        return false;
    }

    private static void logRequestBody(ContentCachingRequestWrapper request, String prefix) {
        val content = request.getContentAsByteArray();
        if (content.length > 0) {
            logContent(" Param", content, request.getContentType(), request.getCharacterEncoding());
        }
    }

    private static void logContent(String type, byte[] content, String contentType, String contentEncoding) {
        val mediaType = MediaType.valueOf(contentType);
        val visible = VISIBLE_TYPES.stream().anyMatch(visibleType -> visibleType.includes(mediaType));
        StringBuilder sb = new StringBuilder();
        if (visible) {
            try {
                val contentString = new String(content, contentEncoding);
                sb.append(type).append("    :").append(contentString);
            } catch (UnsupportedEncodingException e) {
                sb.append(" ContentLength    :").append(content.length);
            }
        } else {
            sb.append(" ContentLength    :").append(content.length);
        }
        log.info(sb.toString());
    }

    private static void logRequestHeader(ContentCachingRequestWrapper request, String prefix) {
        val queryString = request.getQueryString();
        Map<String, String> headerMap = getHeader(request);
        StringBuilder sb = new StringBuilder();
        sb.append(" Prefix    :").append(prefix);
        sb.append(" RealIP    :").append(IpUtil.getIpAddress(request));
        sb.append(" Header    :").append(JSONUtil.toJsonStr(headerMap));
        sb.append(" URI       :").append(request.getRequestURI()).append(StrUtil.isBlank(queryString) ? "" : "?" + queryString);
        sb.append(" Method    :").append(request.getMethod());
        log.info(sb.toString());
    }

    public static Map<String, String> getHeader(HttpServletRequest request) {
        Enumeration<String> headerNames = request.getHeaderNames();
        Map<String, String> headerMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
        return headerMap;
    }

    protected void afterRequest(ContentCachingRequestWrapper request, ContentCachingResponseWrapper response) {
        if (log.isInfoEnabled() && shouldLog(request)) {
            logRequestBody(request, request.getRemoteAddr() + "|>");
            logResponse(response, request.getRemoteAddr() + "|<");
        }
    }

    private static void logResponse(ContentCachingResponseWrapper response, String prefix) {
        val status = response.getStatus();
        StringBuilder sb = new StringBuilder();
        sb.append(" Status    :").append(status);
        sb.append(" ReasonPhrase:").append(HttpStatus.valueOf(status).getReasonPhrase());
        log.info(sb.toString());
        val content = response.getContentAsByteArray();
        if (content.length > 0) {
            logContent(" Return", content, response.getContentType(), response.getCharacterEncoding());
        }
    }
}
