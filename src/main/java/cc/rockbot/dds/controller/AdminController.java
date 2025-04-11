package cc.rockbot.dds.controller;

import cc.rockbot.dds.model.AdminDO;
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
    public ResponseEntity<AdminDO> createAdmin(@RequestBody AdminDO admin) {
        return ResponseEntity.ok(adminService.createAdmin(admin));
    }

    @GetMapping("/{id}")
    public ResponseEntity<AdminDO> getAdminById(@PathVariable Long id) {
        return adminService.getAdminById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/wxid/{adminWxid}")
    public ResponseEntity<AdminDO> getAdminByWxid(@PathVariable String adminWxid) {
        AdminDO admin = adminService.getAdminByWxid(adminWxid);
        return admin != null ? ResponseEntity.ok(admin) : ResponseEntity.notFound().build();
    }

    @GetMapping
    public ResponseEntity<List<AdminDO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @PutMapping("/{id}")
    public ResponseEntity<AdminDO> updateAdmin(@PathVariable Long id, @RequestBody AdminDO admin) {
        admin.setId(id);
        return ResponseEntity.ok(adminService.updateAdmin(admin));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAdmin(@PathVariable Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.ok().build();
    }
} 