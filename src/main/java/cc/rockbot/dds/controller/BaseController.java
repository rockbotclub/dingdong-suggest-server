package cc.rockbot.dds.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.RestController;

/**
 * 基础Controller类
 * 提供Swagger文档的基本配置
 */
@RestController
@Tag(name = "Base API", description = "所有API的基础接口")
public class BaseController {

    /**
     * 通用的API响应配置
     */
    @Operation(summary = "通用响应", description = "所有API的通用响应格式")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "请求成功"),
        @ApiResponse(responseCode = "400", description = "请求参数错误"),
        @ApiResponse(responseCode = "401", description = "未授权"),
        @ApiResponse(responseCode = "403", description = "禁止访问"),
        @ApiResponse(responseCode = "404", description = "资源不存在"),
        @ApiResponse(responseCode = "500", description = "服务器内部错误")
    })
    protected void commonApiResponses() {
        // 这个方法只是用来提供Swagger文档，不需要实现
    }
} 