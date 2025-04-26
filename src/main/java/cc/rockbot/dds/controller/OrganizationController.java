package cc.rockbot.dds.controller;

import cc.rockbot.dds.model.OrganizationDO;
import cc.rockbot.dds.service.OrganizationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cc.rockbot.dds.dto.ApiResponse;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/organizations")
public class OrganizationController extends BaseController {
    private final OrganizationService organizationService;

    @Autowired
    public OrganizationController(OrganizationService organizationService) {
        this.organizationService = organizationService;
    }

    @PostMapping
    public ApiResponse<OrganizationDO> createOrganization(@RequestBody OrganizationDO organization) {
        try {
            if (organization == null) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "组织信息不能为空");
            }
            return ApiResponse.success(organizationService.createOrganization(organization));
        } catch (BusinessException e) {
            log.warn("创建组织业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("创建组织系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<OrganizationDO> getOrganizationById(@PathVariable String id) {
        try {
            if (id == null || id.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "组织ID不能为空");
            }
            OrganizationDO organization = organizationService.getOrganizationById(id)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ORG_NOT_FOUND));
            return ApiResponse.success(organization);
        } catch (BusinessException e) {
            log.warn("获取组织业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("获取组织系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    @GetMapping("/orgid/{orgId}")
    public ApiResponse<OrganizationDO> getOrganizationByOrgId(@PathVariable String orgId) {
        try {
            if (orgId == null || orgId.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "组织ID不能为空");
            }
            OrganizationDO organization = organizationService.getOrganizationById(orgId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ORG_NOT_FOUND));
            return ApiResponse.success(organization);
        } catch (BusinessException e) {
            log.warn("获取组织业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("获取组织系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    @GetMapping
    public ApiResponse<List<OrganizationDO>> getAllOrganizations() {
        try {
            return ApiResponse.success(organizationService.getAllOrganizations());
        } catch (BusinessException e) {
            log.warn("获取所有组织业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("获取所有组织系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }
} 