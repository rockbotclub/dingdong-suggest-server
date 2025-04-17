package cc.rockbot.dds.service;

import cc.rockbot.dds.model.AdminDO;
import cc.rockbot.dds.repository.AdminRepository;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional
    public AdminDO createAdmin(AdminDO admin) {
        log.debug("Creating new admin: {}", admin);
        if (admin == null) {
            log.error("Admin object is null");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Admin cannot be null");
        }

        // Validate required fields
        if (!StringUtils.hasText(admin.getAdminWxid())) {
            log.error("Admin wxid is empty");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Admin wxid cannot be empty");
        }
        if (!StringUtils.hasText(admin.getAdminName())) {
            log.error("Admin name is empty");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Admin name cannot be empty");
        }
        if (!StringUtils.hasText(admin.getOrgId())) {
            log.error("Organization ID is empty");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Organization ID cannot be empty");
        }
        if (!StringUtils.hasText(admin.getAdminPhone())) {
            log.error("Admin phone is empty");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Admin phone cannot be empty");
        }
        if (!StringUtils.hasText(admin.getAdminPasswd())) {
            log.error("Admin password is empty");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Admin password cannot be empty");
        }

        try {
            if (admin.getAdminWxid() != null && adminRepository.existsByAdminWxid(admin.getAdminWxid())) {
                log.error("Admin already exists with wxid: {}", admin.getAdminWxid());
                throw new BusinessException(ErrorCode.ADMIN_ALREADY_EXISTS, "Admin already exists with wxid: " + admin.getAdminWxid());
            }
            AdminDO savedAdmin = adminRepository.save(admin);
            log.info("Admin created successfully with ID: {}", savedAdmin.getId());
            return savedAdmin;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error creating admin: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to create admin");
        }
    }

    public Optional<AdminDO> getAdminById(Long id) {
        log.debug("Getting admin by ID: {}", id);
        if (id == null) {
            log.error("Admin ID is null");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Admin ID cannot be null");
        }
        try {
            Optional<AdminDO> admin = adminRepository.findById(id);
            if (admin.isPresent()) {
                log.info("Found admin with ID: {}", id);
            } else {
                log.warn("Admin not found with ID: {}", id);
            }
            return admin;
        } catch (Exception e) {
            log.error("Error getting admin by ID {}: {}", id, e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to get admin");
        }
    }

    public AdminDO getAdminByWxid(String adminWxid) {
        log.debug("Getting admin by wxid: {}", adminWxid);
        if (!StringUtils.hasText(adminWxid)) {
            log.error("Admin wxid is null or empty");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Admin WxID cannot be null or empty");
        }
        try {
            AdminDO admin = adminRepository.findByAdminWxid(adminWxid);
            if (admin != null) {
                log.info("Found admin with wxid: {}", adminWxid);
            } else {
                log.warn("Admin not found with wxid: {}", adminWxid);
                throw new BusinessException(ErrorCode.ADMIN_NOT_FOUND, "Admin not found with wxid: " + adminWxid);
            }
            return admin;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error getting admin by wxid {}: {}", adminWxid, e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to get admin by wxid");
        }
    }

    public List<AdminDO> getAllAdmins() {
        log.debug("Getting all admins");
        try {
            List<AdminDO> admins = adminRepository.findAll();
            log.info("Found {} admins", admins.size());
            return admins;
        } catch (Exception e) {
            log.error("Error getting all admins: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to get all admins");
        }
    }

    @Transactional
    public AdminDO updateAdmin(AdminDO admin) {
        log.debug("Updating admin: {}", admin);
        if (admin == null) {
            log.error("Admin object is null");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Admin cannot be null");
        }
        if (admin.getId() == null) {
            log.error("Admin ID is null");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Admin ID cannot be null");
        }
        try {
            if (!adminRepository.existsById(admin.getId())) {
                log.error("Admin not found with id: {}", admin.getId());
                throw new BusinessException(ErrorCode.ADMIN_NOT_FOUND, "Admin not found with id: " + admin.getId());
            }
            AdminDO updatedAdmin = adminRepository.save(admin);
            log.info("Admin updated successfully with ID: {}", updatedAdmin.getId());
            return updatedAdmin;
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error updating admin: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to update admin");
        }
    }

    @Transactional
    public void deleteAdmin(Long id) {
        log.debug("Deleting admin with ID: {}", id);
        if (id == null) {
            log.error("Admin ID is null");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Admin ID cannot be null");
        }
        try {
            if (!adminRepository.existsById(id)) {
                log.error("Admin not found with id: {}", id);
                throw new BusinessException(ErrorCode.ADMIN_NOT_FOUND, "Admin not found with id: " + id);
            }
            adminRepository.deleteById(id);
            log.info("Admin deleted successfully with ID: {}", id);
        } catch (BusinessException e) {
            throw e;
        } catch (Exception e) {
            log.error("Error deleting admin: {}", e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to delete admin");
        }
    }

    public boolean existsByWxid(String adminWxid) {
        log.debug("Checking if admin exists by wxid: {}", adminWxid);
        if (!StringUtils.hasText(adminWxid)) {
            log.error("Admin wxid is null or empty");
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Admin WxID cannot be null or empty");
        }
        try {
            boolean exists = adminRepository.existsByAdminWxid(adminWxid);
            log.info("Admin exists with wxid {}: {}", adminWxid, exists);
            return exists;
        } catch (Exception e) {
            log.error("Error checking admin existence by wxid {}: {}", adminWxid, e.getMessage(), e);
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "Failed to check admin existence");
        }
    }
} 