package cc.rockbot.dds.service;

import cc.rockbot.dds.entity.Admin;
import cc.rockbot.dds.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    private Admin admin;

    @BeforeEach
    void setUp() {
        admin = new Admin();
        admin.setId(1L);
        admin.setAdminWxid("test_wxid");
        admin.setAdminName("Test Admin");
        admin.setOrgId("test_org");
        admin.setAdminPhone("1234567890");
        admin.setAdminPasswd("password");
    }

    @Test
    void createAdmin_ShouldReturnSavedAdmin() {
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        Admin savedAdmin = adminService.createAdmin(admin);

        assertNotNull(savedAdmin);
        assertEquals(admin.getId(), savedAdmin.getId());
        assertEquals(admin.getAdminWxid(), savedAdmin.getAdminWxid());
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void getAdminById_ShouldReturnAdmin() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        Optional<Admin> foundAdmin = adminService.getAdminById(1L);

        assertTrue(foundAdmin.isPresent());
        assertEquals(admin.getId(), foundAdmin.get().getId());
        verify(adminRepository).findById(1L);
    }

    @Test
    void getAdminByWxid_ShouldReturnAdmin() {
        when(adminRepository.findByAdminWxid("test_wxid")).thenReturn(admin);

        Admin foundAdmin = adminService.getAdminByWxid("test_wxid");

        assertNotNull(foundAdmin);
        assertEquals(admin.getAdminWxid(), foundAdmin.getAdminWxid());
        verify(adminRepository).findByAdminWxid("test_wxid");
    }

    @Test
    void getAllAdmins_ShouldReturnAllAdmins() {
        List<Admin> admins = Arrays.asList(admin);
        when(adminRepository.findAll()).thenReturn(admins);

        List<Admin> foundAdmins = adminService.getAllAdmins();

        assertNotNull(foundAdmins);
        assertEquals(1, foundAdmins.size());
        verify(adminRepository).findAll();
    }

    @Test
    void updateAdmin_ShouldReturnUpdatedAdmin() {
        when(adminRepository.save(any(Admin.class))).thenReturn(admin);

        Admin updatedAdmin = adminService.updateAdmin(admin);

        assertNotNull(updatedAdmin);
        assertEquals(admin.getId(), updatedAdmin.getId());
        verify(adminRepository).save(any(Admin.class));
    }

    @Test
    void deleteAdmin_ShouldCallRepository() {
        doNothing().when(adminRepository).deleteById(1L);

        adminService.deleteAdmin(1L);

        verify(adminRepository).deleteById(1L);
    }

    @Test
    void existsByWxid_ShouldReturnTrue() {
        when(adminRepository.existsByAdminWxid("test_wxid")).thenReturn(true);

        boolean exists = adminService.existsByWxid("test_wxid");

        assertTrue(exists);
        verify(adminRepository).existsByAdminWxid("test_wxid");
    }
} 