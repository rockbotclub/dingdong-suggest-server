package cc.rockbot.dds.controller;

import cc.rockbot.dds.model.AdminDO;
import cc.rockbot.dds.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<AdminDO> createAdmin(@RequestBody AdminDO admin) {
        log.info("Creating new admin: {}", admin.getAdminName());
        AdminDO createdAdmin = adminService.createAdmin(admin);
        log.info("Admin created successfully with ID: {}", createdAdmin.getId());
        return ResponseEntity.ok(createdAdmin);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDO> getAdminById(@PathVariable Long id) {
        log.info("Fetching admin by ID: {}", id);
        return adminService.getAdminById(id)
                .map(admin -> {
                    log.info("Admin found: {}", admin.getAdminName());
                    return ResponseEntity.ok(admin);
                })
                .orElseGet(() -> {
                    log.warn("Admin not found with ID: {}", id);
                    return ResponseEntity.notFound().build();
                });
    }

    @GetMapping("/wxid/{adminWxid}")
    public ResponseEntity<AdminDO> getAdminByWxid(@PathVariable String adminWxid) {
        log.info("Fetching admin by wxid: {}", adminWxid);
        AdminDO admin = adminService.getAdminByWxid(adminWxid);
        if (admin != null) {
            log.info("Admin found: {}", admin.getAdminName());
            return ResponseEntity.ok(admin);
        } else {
            log.warn("Admin not found with wxid: {}", adminWxid);
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    public ResponseEntity<List<AdminDO>> getAllAdmins() {
        log.info("Fetching all admins");
        try {
            List<AdminDO> admins = adminService.getAllAdmins();
            log.info("Found {} admins", admins.size());
            return ResponseEntity.ok(admins);
        } catch (Exception e) {
            log.error("Error while fetching all admins", e);
            throw e;
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDO> updateAdmin(@PathVariable Long id, @RequestBody AdminDO admin) {
        log.info("Updating admin with ID: {}", id);
        admin.setId(id);
        AdminDO updatedAdmin = adminService.updateAdmin(admin);
        log.info("Admin updated successfully: {}", updatedAdmin.getAdminName());
        return ResponseEntity.ok(updatedAdmin);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        log.info("Deleting admin with ID: {}", id);
        adminService.deleteAdmin(id);
        log.info("Admin deleted successfully with ID: {}", id);
        return ResponseEntity.ok().build();
    }
} 