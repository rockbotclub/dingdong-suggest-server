package cc.rockbot.dds.config;

import cc.rockbot.dds.dto.ApiResponse;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class GlobalResponseHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // 排除Swagger相关的端点
        String className = returnType.getContainingClass().getName();
        return !className.contains("springdoc") && !className.contains("swagger");
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
            Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request,
            ServerHttpResponse response) {
        // 如果已经是 ApiResponse 类型，直接返回
        if (body instanceof ApiResponse) {
            return body;
        }
        // 如果是 void 类型，返回成功响应
        if (body == null) {
            return ApiResponse.success(null);
        }
        // 其他情况包装成 ApiResponse
        return ApiResponse.success(body);
    }
} 