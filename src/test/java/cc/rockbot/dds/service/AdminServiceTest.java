package cc.rockbot.dds.service;

import cc.rockbot.dds.model.AdminDO;
import cc.rockbot.dds.repository.AdminRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

/**
 * 管理员服务测试类
 * 用于测试AdminService的各种业务逻辑
 */
@ExtendWith(MockitoExtension.class)
class AdminServiceTest {

    @Mock
    private AdminRepository adminRepository;

    @InjectMocks
    private AdminService adminService;

    private AdminDO admin;

    /**
     * 在每个测试方法执行前初始化测试数据
     */
    @BeforeEach
    void setUp() {
        // 初始化管理员对象
        admin = new AdminDO();
        admin.setId(1L);
        admin.setAdminWxid("test_wxid");
        admin.setAdminName("Test Admin");
        admin.setOrgId("test_org");
        admin.setAdminPhone("1234567890");
        admin.setAdminPasswd("password");
    }

    /**
     * 测试创建管理员功能
     * 验证是否能成功创建管理员并返回保存后的管理员对象
     */
    @Test
    void createAdmin_ShouldReturnSavedAdmin() {
        when(adminRepository.save(any(AdminDO.class))).thenReturn(admin);

        AdminDO savedAdmin = adminService.createAdmin(admin);

        assertNotNull(savedAdmin);
        assertEquals(admin.getId(), savedAdmin.getId());
        assertEquals(admin.getAdminWxid(), savedAdmin.getAdminWxid());
        verify(adminRepository).save(any(AdminDO.class));
    }

    /**
     * 测试通过ID获取管理员功能
     * 验证是否能成功获取指定ID的管理员
     */
    @Test
    void getAdminById_ShouldReturnAdmin() {
        when(adminRepository.findById(1L)).thenReturn(Optional.of(admin));

        Optional<AdminDO> foundAdmin = adminService.getAdminById(1L);

        assertTrue(foundAdmin.isPresent());
        assertEquals(admin.getId(), foundAdmin.get().getId());
        verify(adminRepository).findById(1L);
    }

    /**
     * 测试通过微信ID获取管理员功能
     * 验证是否能成功获取指定微信ID的管理员
     */
    @Test
    void getAdminByWxid_ShouldReturnAdmin() {
        when(adminRepository.findByAdminWxid("test_wxid")).thenReturn(admin);

        AdminDO foundAdmin = adminService.getAdminByWxid("test_wxid");

        assertNotNull(foundAdmin);
        assertEquals(admin.getAdminWxid(), foundAdmin.getAdminWxid());
        verify(adminRepository).findByAdminWxid("test_wxid");
    }

    /**
     * 测试获取所有管理员功能
     * 验证是否能成功获取所有管理员列表
     */
    @Test
    void getAllAdmins_ShouldReturnAllAdmins() {
        List<AdminDO> admins = Arrays.asList(admin);
        when(adminRepository.findAll()).thenReturn(admins);

        List<AdminDO> foundAdmins = adminService.getAllAdmins();

        assertNotNull(foundAdmins);
        assertEquals(1, foundAdmins.size());
        assertEquals(admin.getId(), foundAdmins.get(0).getId());
        verify(adminRepository).findAll();
    }

    /**
     * 测试更新管理员功能
     * 验证是否能成功更新指定ID的管理员
     */
    @Test
    void updateAdmin_ShouldUpdateAdmin() {
        when(adminRepository.existsById(1L)).thenReturn(true);
        when(adminRepository.save(any(AdminDO.class))).thenReturn(admin);

        AdminDO updatedAdmin = adminService.updateAdmin(admin);

        assertNotNull(updatedAdmin);
        assertEquals(admin.getId(), updatedAdmin.getId());
        assertEquals(admin.getAdminWxid(), updatedAdmin.getAdminWxid());
        verify(adminRepository).save(any(AdminDO.class));
    }

    /**
     * 测试删除管理员功能
     * 验证是否能成功删除指定ID的管理员
     */
    @Test
    void deleteAdmin_ShouldDeleteAdmin() {
        when(adminRepository.existsById(1L)).thenReturn(true);
        doNothing().when(adminRepository).deleteById(1L);

        adminService.deleteAdmin(1L);

        verify(adminRepository).deleteById(1L);
    }

    /**
     * 测试检查微信ID是否存在功能
     * 验证是否能正确检查微信ID是否存在
     */
    @Test
    void existsByWxid_ShouldReturnTrue() {
        when(adminRepository.existsByAdminWxid("test_wxid")).thenReturn(true);

        boolean exists = adminService.existsByWxid("test_wxid");
        assertTrue(exists);

        verify(adminRepository).existsByAdminWxid("test_wxid");
    }

    /**
     * 测试更新不存在的管理员
     * 验证当管理员不存在时是否能正确处理
     */
    @Test
    void updateAdmin_WhenNotFound_ShouldThrowException() {
        when(adminRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> adminService.updateAdmin(admin));
        verify(adminRepository, never()).save(any(AdminDO.class));
    }

    /**
     * 测试创建管理员时传入null对象
     * 验证当传入null对象时是否能正确处理
     */
    @Test
    void createAdmin_WhenAdminIsNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> adminService.createAdmin(null));
        verify(adminRepository, never()).save(any(AdminDO.class));
    }

    /**
     * 测试创建管理员时传入无效数据
     * 验证当管理员数据不完整时是否能正确处理
     */
    @Test
    void createAdmin_WithInvalidData_ShouldThrowException() {
        AdminDO invalidAdmin = new AdminDO();
        invalidAdmin.setAdminWxid(""); // 空微信ID
        when(adminRepository.save(any(AdminDO.class))).thenThrow(new IllegalArgumentException());

        assertThrows(IllegalArgumentException.class, () -> adminService.createAdmin(invalidAdmin));
        verify(adminRepository).save(invalidAdmin);
    }

    /**
     * 测试获取所有管理员时数据库为空的情况
     * 验证当没有管理员数据时是否能返回空列表
     */
    @Test
    void getAllAdmins_WhenNoAdmins_ShouldReturnEmptyList() {
        when(adminRepository.findAll()).thenReturn(Collections.emptyList());

        List<AdminDO> result = adminService.getAllAdmins();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(adminRepository).findAll();
    }

    /**
     * 测试通过ID获取管理员时传入null
     * 验证当管理员ID为null时是否能正确处理
     */
    @Test
    void getAdminById_WhenIdIsNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> adminService.getAdminById(null));
        verify(adminRepository, never()).findById(anyLong());
    }

    /**
     * 测试通过微信ID获取管理员时传入空字符串
     * 验证当微信ID为空字符串时是否能正确处理
     */
    @Test
    void getAdminByWxid_WhenWxidIsEmpty_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> adminService.getAdminByWxid(""));
        verify(adminRepository, never()).findByAdminWxid(anyString());
    }

    /**
     * 测试通过微信ID获取管理员时传入null
     * 验证当微信ID为null时是否能正确处理
     */
    @Test
    void getAdminByWxid_WhenWxidIsNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> adminService.getAdminByWxid(null));
        verify(adminRepository, never()).findByAdminWxid(anyString());
    }

    /**
     * 测试更新管理员时传入null对象
     * 验证当传入null对象时是否能正确处理
     */
    @Test
    void updateAdmin_WhenAdminIsNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> adminService.updateAdmin(null));
        verify(adminRepository, never()).save(any(AdminDO.class));
    }

    /**
     * 测试更新管理员时ID为null的情况
     * 验证当管理员ID为null时是否能正确处理
     */
    @Test
    void updateAdmin_WhenIdIsNull_ShouldThrowException() {
        AdminDO admin = new AdminDO();
        admin.setId(null);

        assertThrows(IllegalArgumentException.class, () -> adminService.updateAdmin(admin));
        verify(adminRepository, never()).save(any(AdminDO.class));
    }

    /**
     * 测试删除管理员时传入null
     * 验证当管理员ID为null时是否能正确处理
     */
    @Test
    void deleteAdmin_WhenIdIsNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> adminService.deleteAdmin(null));
        verify(adminRepository, never()).deleteById(anyLong());
    }

    /**
     * 测试检查微信ID是否存在时传入空字符串
     * 验证当微信ID为空字符串时是否能正确处理
     */
    @Test
    void existsByWxid_WhenWxidIsEmpty_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> adminService.existsByWxid(""));
        verify(adminRepository, never()).existsByAdminWxid(anyString());
    }

    /**
     * 测试检查微信ID是否存在时传入null
     * 验证当微信ID为null时是否能正确处理
     */
    @Test
    void existsByWxid_WhenWxidIsNull_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> adminService.existsByWxid(null));
        verify(adminRepository, never()).existsByAdminWxid(anyString());
    }
} 