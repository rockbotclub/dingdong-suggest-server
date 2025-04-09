package cc.rockbot.dds.service;

import cc.rockbot.dds.entity.Admin;
import cc.rockbot.dds.repository.AdminRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public Admin createAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    public Optional<Admin> getAdminById(Long id) {
        return adminRepository.findById(id);
    }

    public Admin getAdminByWxid(String adminWxid) {
        return adminRepository.findByAdminWxid(adminWxid);
    }

    public List<Admin> getAllAdmins() {
        return adminRepository.findAll();
    }

    @Transactional
    public Admin updateAdmin(Admin admin) {
        return adminRepository.save(admin);
    }

    @Transactional
    public void deleteAdmin(Long id) {
        adminRepository.deleteById(id);
    }

    public boolean existsByWxid(String adminWxid) {
        return adminRepository.existsByAdminWxid(adminWxid);
    }
} 