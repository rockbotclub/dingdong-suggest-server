package cc.rockbot.dds.service;

import cc.rockbot.dds.entity.User;
import cc.rockbot.dds.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createUser_ShouldReturnSavedUser() {
        // Given
        User user = new User();
        user.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = userService.createUser(user);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).save(user);
    }

    @Test
    void getUserById_ShouldReturnUser() {
        // Given
        User user = new User();
        user.setId(1L);
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));

        // When
        Optional<User> result = userService.getUserById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(1L, result.get().getId());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserById_WhenNotFound_ShouldReturnEmpty() {
        // Given
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        // When
        Optional<User> result = userService.getUserById(1L);

        // Then
        assertFalse(result.isPresent());
        verify(userRepository).findById(1L);
    }

    @Test
    void getUserByWxid_ShouldReturnUser() {
        // Given
        User user = new User();
        user.setId(1L);
        when(userRepository.findByUserWxid("test_wxid")).thenReturn(user);

        // When
        User result = userService.getUserByWxid("test_wxid");

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).findByUserWxid("test_wxid");
    }

    @Test
    void getUsersByOrgId_ShouldReturnUsers() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findByOrgId("org1")).thenReturn(users);

        // When
        List<User> result = userService.getUsersByOrgId("org1");

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(userRepository).findByOrgId("org1");
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() {
        // Given
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        List<User> users = Arrays.asList(user1, user2);
        when(userRepository.findAll()).thenReturn(users);

        // When
        List<User> result = userService.getAllUsers();

        // Then
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getId());
        assertEquals(2L, result.get(1).getId());
        verify(userRepository).findAll();
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() {
        // Given
        User user = new User();
        user.setId(1L);
        when(userRepository.save(any(User.class))).thenReturn(user);

        // When
        User result = userService.updateUser(user);

        // Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(userRepository).save(user);
    }

    @Test
    void deleteUser_ShouldCallRepository() {
        // When
        userService.deleteUser(1L);

        // Then
        verify(userRepository).deleteById(1L);
    }

    @Test
    void existsByWxid_ShouldReturnTrue() {
        // Given
        when(userRepository.existsByUserWxid("test_wxid")).thenReturn(true);

        // When
        boolean result = userService.existsByWxid("test_wxid");

        // Then
        assertTrue(result);
        verify(userRepository).existsByUserWxid("test_wxid");
    }

    @Test
    void existsByWxid_ShouldReturnFalse() {
        // Given
        when(userRepository.existsByUserWxid("test_wxid")).thenReturn(false);

        // When
        boolean result = userService.existsByWxid("test_wxid");

        // Then
        assertFalse(result);
        verify(userRepository).existsByUserWxid("test_wxid");
    }
} 