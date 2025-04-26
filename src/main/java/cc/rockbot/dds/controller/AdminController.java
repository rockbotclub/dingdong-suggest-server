package cc.rockbot.dds.controller;

import cc.rockbot.dds.model.AdminDO;
import cc.rockbot.dds.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import cc.rockbot.dds.dto.ApiResponse;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admins")
public class AdminController extends BaseController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ApiResponse<AdminDO> createAdmin(@RequestBody AdminDO admin) {
        try {
            if (admin == null) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "管理员信息不能为空");
            }
            log.info("Creating new admin: {}", admin.getAdminName());
            AdminDO createdAdmin = adminService.createAdmin(admin);
            log.info("Admin created successfully with ID: {}", createdAdmin.getId());
            return ApiResponse.success(createdAdmin);
        } catch (BusinessException e) {
            log.warn("创建管理员业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("创建管理员系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    @GetMapping("/{id}")
    public ApiResponse<AdminDO> getAdminById(@PathVariable Long id) {
        try {
            if (id == null) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "管理员ID不能为空");
            }
            log.info("Fetching admin by ID: {}", id);
            AdminDO admin = adminService.getAdminById(id)
                    .orElseThrow(() -> new BusinessException(ErrorCode.ADMIN_NOT_FOUND));
            log.info("Admin found: {}", admin.getAdminName());
            return ApiResponse.success(admin);
        } catch (BusinessException e) {
            log.warn("获取管理员业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("获取管理员系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    @GetMapping("/wxid/{adminWxid}")
    public ApiResponse<AdminDO> getAdminByWxid(@PathVariable String adminWxid) {
        try {
            if (adminWxid == null || adminWxid.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "管理员微信ID不能为空");
            }
            log.info("Fetching admin by wxid: {}", adminWxid);
            AdminDO admin = adminService.getAdminByWxid(adminWxid);
            if (admin == null) {
                throw new BusinessException(ErrorCode.ADMIN_NOT_FOUND);
            }
            log.info("Admin found: {}", admin.getAdminName());
            return ApiResponse.success(admin);
        } catch (BusinessException e) {
            log.warn("获取管理员业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("获取管理员系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    @GetMapping
    public ApiResponse<List<AdminDO>> getAllAdmins() {
        try {
            log.info("Fetching all admins");
            List<AdminDO> admins = adminService.getAllAdmins();
            log.info("Found {} admins", admins.size());
            return ApiResponse.success(admins);
        } catch (BusinessException e) {
            log.warn("获取所有管理员业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("获取所有管理员系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }
} 