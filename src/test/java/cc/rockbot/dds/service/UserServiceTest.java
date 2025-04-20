package cc.rockbot.dds.service;

import cc.rockbot.dds.model.UserDO;
import cc.rockbot.dds.repository.UserRepository;
import cc.rockbot.dds.service.impl.UserServiceImpl;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
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
 * 用户服务测试类
 * 用于测试UserService的各种业务逻辑
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private UserDO user;

    /**
     * 在每个测试方法执行前初始化测试环境
     * 注意：不需要手动初始化mock对象，因为@ExtendWith(MockitoExtension.class)已经处理了
     */
    @BeforeEach
    void setUp() {
        user = new UserDO();
        user.setId(1L);
        user.setWxid("test_wxid");
        user.setUserName("Test User");
        user.setUserOrg("Test Org");
        user.setUserPhone("13800138000");
        user.setStatus(1);
        user.setOrgId("test_org");
    }

    /**
     * 测试创建用户功能
     * 验证是否能成功创建用户并返回保存后的用户对象
     */
    @Test
    void createUser_WhenUserIsValid_ShouldReturnCreatedUser() {
        // Given
        when(userRepository.save(any(UserDO.class))).thenReturn(user);

        // When
        UserDO createdUser = userService.createUser(user);

        // Then
        assertNotNull(createdUser);
        assertEquals(1L, createdUser.getId());
        assertEquals("test_wxid", createdUser.getWxid());
        verify(userRepository).save(user);
    }

    /**
     * 测试通过ID获取用户功能
     * 验证是否能成功获取指定ID的用户
     */
    @Test
    void getUserById_WhenIdExists_ShouldReturnUser() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        Optional<UserDO> foundUser = userService.getUserById(1L);

        // Then
        assertTrue(foundUser.isPresent());
        assertEquals(1L, foundUser.get().getId());
        verify(userRepository).findById(1L);
    }

    /**
     * 测试获取不存在的用户时的处理
     * 验证当用户不存在时是否返回空的Optional对象
     */
    @Test
    void getUserById_WhenNotFound_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<UserDO> result = userService.getUserById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findById(1L);
    }

    /**
     * 测试获取所有用户功能
     * 验证是否能成功获取所有用户列表
     */
    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        List<UserDO> users = Arrays.asList(user);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<UserDO> foundUsers = userService.getAllUsers();

        // Then
        assertNotNull(foundUsers);
        assertEquals(1, foundUsers.size());
        verify(userRepository).findAll();
    }

    /**
     * 测试更新用户功能
     * 验证是否能成功更新指定ID的用户
     */
    @Test
    void updateUser_ShouldUpdateUser() {
        // Given
        UserDO existingUser = new UserDO();
        existingUser.setId(1L);
        when(userRepository.existsById(1L)).thenReturn(true);
        when(userRepository.save(any(UserDO.class))).thenReturn(existingUser);

        // When
        UserDO result = userService.updateUser(existingUser);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).existsById(1L);
        verify(userRepository).save(existingUser);
    }

    /**
     * 测试更新不存在的用户
     * 验证当用户不存在时是否能正确处理
     */
    @Test
    void updateUser_WhenNotFound_ShouldThrowException() {
        // Given
        UserDO user = new UserDO();
        user.setId(1L);
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.updateUser(user));
        verify(userRepository, never()).save(any(UserDO.class));
    }

    /**
     * 测试删除用户功能
     * 验证是否能成功删除指定ID的用户
     */
    @Test
    void deleteUser_ShouldDeleteUser() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(true);
        doNothing().when(userRepository).deleteById(1L);

        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).deleteById(1L);
    }

    /**
     * 测试删除不存在的用户
     * 验证当用户不存在时是否能正确处理
     */
    @Test
    void deleteUser_WhenNotFound_ShouldThrowException() {
        // Given
        when(userRepository.existsById(1L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> userService.deleteUser(1L));
        verify(userRepository, never()).deleteById(1L);
    }

    /**
     * 测试通过微信ID获取用户功能
     * 验证是否能成功获取指定微信ID的用户
     */
    @Test
    void getUserByWxid_ShouldReturnUser() {
        // Given
        UserDO user = new UserDO();
        user.setWxid("test_wxid");
        when(userRepository.findByWxid("test_wxid")).thenReturn(Collections.singletonList(user));

        // When
        UserDO result = userService.getUserByWxid("test_wxid");

        // Then
        assertNotNull(result);
        assertEquals("test_wxid", result.getWxid());
        verify(userRepository).findByWxid("test_wxid");
    }

    /**
     * 测试通过微信ID获取用户功能，当用户不存在时
     * 验证是否能正确处理用户不存在的情况
     */
    @Test
    void getUserByWxid_WhenNotFound_ShouldThrowException() {
        // Given
        when(userRepository.findByWxid("test_wxid")).thenReturn(Collections.emptyList());

        // When & Then
        BusinessException exception = assertThrows(BusinessException.class, () -> userService.getUserByWxid("test_wxid"));
        assertEquals(ErrorCode.USER_NOT_FOUND.getCode(), exception.getErrorCode().getCode());
        verify(userRepository).findByWxid("test_wxid");
    }

    /**
     * 测试检查微信ID是否存在功能
     * 验证是否能正确检查微信ID是否存在
     */
    @Test
    void existsByWxid_ShouldReturnTrue() {
        // Given
        when(userRepository.existsByWxid("test_wxid")).thenReturn(true);

        // When
        boolean result = userService.existsByWxid("test_wxid");

        // Then
        assertTrue(result);
        verify(userRepository).existsByWxid("test_wxid");
    }

    /**
     * 测试通过组织ID获取用户功能
     * 验证是否能成功获取指定组织ID的所有用户
     */
    @Test
    void getUsersByOrgId_ShouldReturnUsers() {
        // Given
        UserDO user1 = new UserDO();
        user1.setOrgId("test_org");
        UserDO user2 = new UserDO();
        user2.setOrgId("test_org");
        List<UserDO> users = Arrays.asList(user1, user2);
        when(userRepository.findByOrgId("test_org")).thenReturn(users);

        // When
        List<UserDO> result = userService.getUsersByOrgId("test_org");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(userRepository).findByOrgId("test_org");
    }

    /**
     * 测试创建用户时传入null对象
     * 验证当传入null对象时是否能正确处理
     */
    @Test
    void createUser_WhenUserIsNull_ShouldThrowException() {
        // When & Then
        assertThrows(BusinessException.class, () -> userService.createUser(null));
        verify(userRepository, never()).save(any(UserDO.class));
    }

    /**
     * 测试创建用户时传入无效数据
     * 验证当用户数据不完整时是否能正确处理
     */
    @Test
    void createUser_WithInvalidData_ShouldThrowException() {
        // Given
        UserDO user = new UserDO();
        user.setWxid(""); // 空微信ID
        when(userRepository.save(any(UserDO.class))).thenThrow(new IllegalArgumentException());

        // When & Then
        assertThrows(IllegalArgumentException.class, () -> userService.createUser(user));
        verify(userRepository).save(user);
    }

    /**
     * 测试获取所有用户时数据库为空的情况
     * 验证当没有用户数据时是否能返回空列表
     */
    @Test
    void getAllUsers_WhenNoUsers_ShouldReturnEmptyList() {
        // Given
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        // When
        List<UserDO> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findAll();
    }

    /**
     * 测试更新用户时传入null对象
     * 验证当传入null对象时是否能正确处理
     */
    @Test
    void updateUser_WhenUserIsNull_ShouldThrowException() {
        // When & Then
        assertThrows(BusinessException.class, () -> userService.updateUser(null));
        verify(userRepository, never()).save(any(UserDO.class));
    }

    /**
     * 测试更新用户时ID为null的情况
     * 验证当用户ID为null时是否能正确处理
     */
    @Test
    void updateUser_WhenUserIdIsNull_ShouldThrowException() {
        // Given
        UserDO user = new UserDO();
        user.setId(null);

        // When & Then
        assertThrows(BusinessException.class, () -> userService.updateUser(user));
        verify(userRepository, never()).save(any(UserDO.class));
    }

    /**
     * 测试通过微信ID获取用户时传入空字符串
     * 验证当微信ID为空字符串时是否能正确处理
     */
    @Test
    void getUserByWxid_WhenWxidIsEmpty_ShouldThrowException() {
        // When & Then
        assertThrows(BusinessException.class, () -> userService.getUserByWxid(""));
        verify(userRepository, never()).findByWxid(anyString());
    }

    /**
     * 测试通过微信ID获取用户时传入null
     * 验证当微信ID为null时是否能正确处理
     */
    @Test
    void getUserByWxid_WhenWxidIsNull_ShouldThrowException() {
        // When & Then
        assertThrows(BusinessException.class, () -> userService.getUserByWxid(null));
        verify(userRepository, never()).findByWxid(anyString());
    }

    /**
     * 测试通过组织ID获取用户时传入空字符串
     * 验证当组织ID为空字符串时是否能正确处理
     */
    @Test
    void getUsersByOrgId_WhenOrgIdIsEmpty_ShouldThrowException() {
        // When & Then
        assertThrows(BusinessException.class, () -> userService.getUsersByOrgId(""));
        verify(userRepository, never()).findByOrgId(anyString());
    }

    /**
     * 测试通过组织ID获取用户时传入null
     * 验证当组织ID为null时是否能正确处理
     */
    @Test
    void getUsersByOrgId_WhenOrgIdIsNull_ShouldThrowException() {
        // When & Then
        assertThrows(BusinessException.class, () -> userService.getUsersByOrgId(null));
        verify(userRepository, never()).findByOrgId(anyString());
    }

    /**
     * 测试通过组织ID获取用户时没有匹配的用户
     * 验证当没有匹配的用户时是否能返回空列表
     */
    @Test
    void getUsersByOrgId_WhenNoUsersFound_ShouldReturnEmptyList() {
        // Given
        when(userRepository.findByOrgId("test_org")).thenReturn(Collections.emptyList());

        // When
        List<UserDO> result = userService.getUsersByOrgId("test_org");

        // Then
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(userRepository).findByOrgId("test_org");
    }
} 