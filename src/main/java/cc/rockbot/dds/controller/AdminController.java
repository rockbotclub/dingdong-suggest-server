package cc.rockbot.dds.controller;

import cc.rockbot.dds.entity.Admin;
import cc.rockbot.dds.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admins")
public class AdminController {
    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @PostMapping
    public ResponseEntity<Admin> createAdmin(@RequestBody Admin admin) {
        return ResponseEntity.ok(adminService.createAdmin(admin));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Admin> getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/wxid/{adminWxid}")
    public ResponseEntity<Admin> getAdminByWxid(@PathVariable String adminWxid) {
        Admin admin = adminService.getAdminByWxid(adminWxid);
        return admin != null ? ResponseEntity.ok(admin) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<Admin>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Admin> updateAdmin(@PathVariable Long id, @RequestBody Admin admin) {
        admin.setId(id);
        return ResponseEntity.ok(adminService.updateAdmin(admin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok().build();
    }
} 