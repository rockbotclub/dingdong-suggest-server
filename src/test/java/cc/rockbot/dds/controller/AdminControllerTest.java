package cc.rockbot.dds.controller;

import cc.rockbot.dds.entity.Admin;
import cc.rockbot.dds.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @InjectMocks
    private AdminController adminController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(adminController).build();
    }

    @Test
    void createAdmin_ShouldReturnCreatedAdmin() throws Exception {
        // Given
        Admin admin = new Admin();
        admin.setId(1L);
        when(adminService.createAdmin(any(Admin.class))).thenReturn(admin);

        // When & Then
        mockMvc.perform(post("/api/admins")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAdminById_ShouldReturnAdmin() throws Exception {
        // Given
        Admin admin = new Admin();
        admin.setId(1L);
        when(adminService.getAdminById(1L)).thenReturn(Optional.of(admin));

        // When & Then
        mockMvc.perform(get("/api/admins/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAdminById_WhenNotFound_ShouldReturn404() throws Exception {
        // Given
        when(adminService.getAdminById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/admins/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAdminByWxid_ShouldReturnAdmin() throws Exception {
        // Given
        Admin admin = new Admin();
        admin.setId(1L);
        when(adminService.getAdminByWxid("test_wxid")).thenReturn(admin);

        // When & Then
        mockMvc.perform(get("/api/admins/wxid/test_wxid"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getAdminByWxid_WhenNotFound_ShouldReturn404() throws Exception {
        // Given
        when(adminService.getAdminByWxid("test_wxid")).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/admins/wxid/test_wxid"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllAdmins_ShouldReturnAllAdmins() throws Exception {
        // Given
        Admin admin1 = new Admin();
        admin1.setId(1L);
        Admin admin2 = new Admin();
        admin2.setId(2L);
        List<Admin> admins = Arrays.asList(admin1, admin2);
        when(adminService.getAllAdmins()).thenReturn(admins);

        // When & Then
        mockMvc.perform(get("/api/admins"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void updateAdmin_ShouldReturnUpdatedAdmin() throws Exception {
        // Given
        Admin admin = new Admin();
        admin.setId(1L);
        when(adminService.updateAdmin(any(Admin.class))).thenReturn(admin);

        // When & Then
        mockMvc.perform(put("/api/admins/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteAdmin_ShouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/admins/1"))
                .andExpect(status().isOk());
    }
} 