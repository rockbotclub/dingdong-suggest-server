package cc.rockbot.dds.controller;

import cc.rockbot.dds.entity.User;
import cc.rockbot.dds.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class UserControllerTest {

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    void createUser_ShouldReturnCreatedUser() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        when(userService.createUser(any(User.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(post("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserById_ShouldReturnUser() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        when(userService.getUserById(1L)).thenReturn(Optional.of(user));

        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserById_WhenNotFound_ShouldReturn404() throws Exception {
        // Given
        when(userService.getUserById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/users/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUserByWxid_ShouldReturnUser() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        when(userService.getUserByWxid("test_wxid")).thenReturn(user);

        // When & Then
        mockMvc.perform(get("/api/users/wxid/test_wxid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getUserByWxid_WhenNotFound_ShouldReturn404() throws Exception {
        // Given
        when(userService.getUserByWxid("test_wxid")).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/users/wxid/test_wxid"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getUsersByOrgId_ShouldReturnUsers() throws Exception {
        // Given
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        when(userService.getUsersByOrgId("org1")).thenReturn(Arrays.asList(user1, user2));

        // When & Then
        mockMvc.perform(get("/api/users/org/org1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getAllUsers_ShouldReturnAllUsers() throws Exception {
        // Given
        User user1 = new User();
        user1.setId(1L);
        User user2 = new User();
        user2.setId(2L);
        when(userService.getAllUsers()).thenReturn(Arrays.asList(user1, user2));

        // When & Then
        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void updateUser_ShouldReturnUpdatedUser() throws Exception {
        // Given
        User user = new User();
        user.setId(1L);
        when(userService.updateUser(any(User.class))).thenReturn(user);

        // When & Then
        mockMvc.perform(put("/api/users/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteUser_ShouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/users/1"))
                .andExpect(status().isOk());
    }
} 