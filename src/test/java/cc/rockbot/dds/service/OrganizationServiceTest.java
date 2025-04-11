package cc.rockbot.dds.service;

import cc.rockbot.dds.model.OrganizationDO;
import cc.rockbot.dds.repository.OrganizationRepository;
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
 * 组织服务测试类
 * 用于测试OrganizationService的各种业务逻辑
 */
@ExtendWith(MockitoExtension.class)
class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationService organizationService;

    private OrganizationDO organization;

    /**
     * 在每个测试方法执行前初始化测试数据
     */
    @BeforeEach
    void setUp() {
        // 初始化组织对象
        organization = new OrganizationDO();
        organization.setId("test_org_id");
        organization.setOrgName("Test Organization");
        organization.setAddress("Test Address");
    }

    /**
     * 测试创建组织功能
     * 验证是否能成功创建组织并返回保存后的组织对象
     */
    @Test
    void testCreateOrganization() {
        when(organizationRepository.save(any(OrganizationDO.class))).thenReturn(organization);

        OrganizationDO createdOrganization = organizationService.createOrganization(organization);
        assertNotNull(createdOrganization);
        assertEquals("test_org_id", createdOrganization.getId());
        assertEquals("Test Organization", createdOrganization.getOrgName());

        verify(organizationRepository).save(any(OrganizationDO.class));
    }

    /**
     * 测试通过ID获取组织功能
     * 验证是否能成功获取指定ID的组织
     */
    @Test
    void testGetOrganizationById() {
        when(organizationRepository.findById("test_org_id")).thenReturn(Optional.of(organization));

        Optional<OrganizationDO> foundOrganization = organizationService.getOrganizationById("test_org_id");
        assertTrue(foundOrganization.isPresent());
        assertEquals("test_org_id", foundOrganization.get().getId());

        verify(organizationRepository).findById("test_org_id");
    }

    /**
     * 测试获取所有组织功能
     * 验证是否能成功获取所有组织列表
     */
    @Test
    void testGetAllOrganizations() {
        List<OrganizationDO> organizations = Arrays.asList(organization);
        when(organizationRepository.findAll()).thenReturn(organizations);

        List<OrganizationDO> foundOrganizations = organizationService.getAllOrganizations();
        assertNotNull(foundOrganizations);
        assertEquals(1, foundOrganizations.size());
        assertEquals("test_org_id", foundOrganizations.get(0).getId());

        verify(organizationRepository).findAll();
    }

    /**
     * 测试更新组织功能
     * 验证是否能成功更新指定ID的组织
     */
    @Test
    void testUpdateOrganization() {
        when(organizationRepository.existsById("test_org_id")).thenReturn(true);
        when(organizationRepository.save(any(OrganizationDO.class))).thenReturn(organization);

        OrganizationDO updatedOrganization = organizationService.updateOrganization(organization);
        assertNotNull(updatedOrganization);
        assertEquals("test_org_id", updatedOrganization.getId());
        assertEquals("Test Organization", updatedOrganization.getOrgName());

        verify(organizationRepository).save(any(OrganizationDO.class));
    }

    /**
     * 测试删除组织功能
     * 验证是否能成功删除指定ID的组织
     */
    @Test
    void testDeleteOrganization() {
        when(organizationRepository.existsById("test_org_id")).thenReturn(true);
        doNothing().when(organizationRepository).deleteById("test_org_id");

        organizationService.deleteOrganization("test_org_id");

        verify(organizationRepository).deleteById("test_org_id");
    }

    /**
     * 测试检查组织ID是否存在功能
     * 验证是否能正确检查组织ID是否存在
     */
    @Test
    void testExistsById() {
        when(organizationRepository.existsById("test_org_id")).thenReturn(true);

        boolean exists = organizationService.existsById("test_org_id");
        assertTrue(exists);

        verify(organizationRepository).existsById("test_org_id");
    }

    /**
     * 测试更新不存在的组织
     * 验证当组织不存在时是否能正确处理
     */
    @Test
    void testUpdateNonExistentOrganization() {
        OrganizationDO nonExistentOrg = new OrganizationDO();
        nonExistentOrg.setId("non_existent_id");
        when(organizationRepository.existsById("non_existent_id")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> 
            organizationService.updateOrganization(nonExistentOrg));
        verify(organizationRepository, never()).save(any(OrganizationDO.class));
    }

    /**
     * 测试删除不存在的组织
     * 验证当组织不存在时是否能正确处理
     */
    @Test
    void testDeleteNonExistentOrganization() {
        when(organizationRepository.existsById("non_existent_id")).thenReturn(false);

        assertThrows(RuntimeException.class, () -> 
            organizationService.deleteOrganization("non_existent_id"));
    }

    /**
     * 测试创建组织时传入null对象
     * 验证当传入null对象时是否能正确处理
     */
    @Test
    void createOrganization_WhenOrganizationIsNull_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> organizationService.createOrganization(null));
        verify(organizationRepository, never()).save(any(OrganizationDO.class));
    }

    /**
     * 测试创建组织时传入无效数据
     * 验证当组织数据不完整时是否能正确处理
     */
    @Test
    void createOrganization_WithInvalidData_ShouldThrowException() {
        // Given
        OrganizationDO invalidOrg = new OrganizationDO();
        invalidOrg.setId(""); // 空ID
        when(organizationRepository.save(any(OrganizationDO.class))).thenThrow(new IllegalArgumentException());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> organizationService.createOrganization(invalidOrg));
        verify(organizationRepository).save(invalidOrg);
    }

    /**
     * 测试获取所有组织时数据库为空的情况
     * 验证当没有组织数据时是否能返回空列表
     */
    @Test
    void getAllOrganizations_WhenNoOrganizations_ShouldReturnEmptyList() {
        // Given
        when(organizationRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<OrganizationDO> result = organizationService.getAllOrganizations();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(organizationRepository).findAll();
    }

    /**
     * 测试通过ID获取组织时传入空字符串
     * 验证当组织ID为空字符串时是否能正确处理
     */
    @Test
    void getOrganizationById_WhenIdIsEmpty_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> organizationService.getOrganizationById(""));
        verify(organizationRepository, never()).findById(anyString());
    }

    /**
     * 测试通过ID获取组织时传入null
     * 验证当组织ID为null时是否能正确处理
     */
    @Test
    void getOrganizationById_WhenIdIsNull_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> organizationService.getOrganizationById(null));
        verify(organizationRepository, never()).findById(anyString());
    }

    /**
     * 测试更新组织时传入null对象
     * 验证当传入null对象时是否能正确处理
     */
    @Test
    void updateOrganization_WhenOrganizationIsNull_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> organizationService.updateOrganization(null));
        verify(organizationRepository, never()).save(any(OrganizationDO.class));
    }

    /**
     * 测试更新组织时ID为null的情况
     * 验证当组织ID为null时是否能正确处理
     */
    @Test
    void updateOrganization_WhenIdIsNull_ShouldThrowException() {
        // Given
        OrganizationDO org = new OrganizationDO();
        org.setId(null);

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> organizationService.updateOrganization(org));
        verify(organizationRepository, never()).save(any(OrganizationDO.class));
    }

    /**
     * 测试删除组织时传入空字符串
     * 验证当组织ID为空字符串时是否能正确处理
     */
    @Test
    void deleteOrganization_WhenIdIsEmpty_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> organizationService.deleteOrganization(""));
        verify(organizationRepository, never()).deleteById(anyString());
    }

    /**
     * 测试删除组织时传入null
     * 验证当组织ID为null时是否能正确处理
     */
    @Test
    void deleteOrganization_WhenIdIsNull_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> organizationService.deleteOrganization(null));
        verify(organizationRepository, never()).deleteById(anyString());
    }

    /**
     * 测试检查组织ID是否存在时传入空字符串
     * 验证当组织ID为空字符串时是否能正确处理
     */
    @Test
    void existsById_WhenIdIsEmpty_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> organizationService.existsById(""));
        verify(organizationRepository, never()).existsById(anyString());
    }

    /**
     * 测试检查组织ID是否存在时传入null
     * 验证当组织ID为null时是否能正确处理
     */
    @Test
    void existsById_WhenIdIsNull_ShouldThrowException() {
        // When & Then
        assertThrows(IllegalArgumentException.class, () -> organizationService.existsById(null));
        verify(organizationRepository, never()).existsById(anyString());
    }
} 