package cc.rockbot.dds.service;

import cc.rockbot.dds.model.AdminDO;
import cc.rockbot.dds.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Optional;

@Service
public class AdminService {
    private final AdminRepository adminRepository;

    @Autowired
    public AdminService(AdminRepository adminRepository) {
        this.adminRepository = adminRepository;
    }

    @Transactional
    public AdminDO createAdmin(AdminDO admin) {
        if (admin == null) {
            throw new IllegalArgumentException("Admin cannot be null");
        }
        return adminRepository.save(admin);
    }

    public Optional<AdminDO> getAdminById(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Admin ID cannot be null");
        }
        return adminRepository.findById(id);
    }

    public AdminDO getAdminByWxid(String adminWxid) {
        if (!StringUtils.hasText(adminWxid)) {
            throw new IllegalArgumentException("Admin WxID cannot be null or empty");
        }
        return adminRepository.findByAdminWxid(adminWxid);
    }

    public List<AdminDO> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Transactional
    public AdminDO updateAdmin(AdminDO admin) {
        if (admin == null) {
            throw new IllegalArgumentException("Admin cannot be null");
        }
        if (admin.getId() == null) {
            throw new IllegalArgumentException("Admin ID cannot be null");
        }
        if (!adminRepository.existsById(admin.getId())) {
            throw new RuntimeException("Admin not found with id: " + admin.getId());
        }
        return adminRepository.save(admin);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        if (id == null) {
            throw new IllegalArgumentException("Admin ID cannot be null");
        }
        if (!adminRepository.existsById(id)) {
            throw new RuntimeException("Admin not found with id: " + id);
        }
        adminRepository.deleteById(id);
    }

    public boolean existsByWxid(String adminWxid) {
        if (!StringUtils.hasText(adminWxid)) {
            throw new IllegalArgumentException("Admin WxID cannot be null or empty");
        }
        return adminRepository.existsByAdminWxid(adminWxid);
    }
} 