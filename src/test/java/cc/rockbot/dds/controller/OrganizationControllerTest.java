package cc.rockbot.dds.controller;

import cc.rockbot.dds.entity.Organization;
import cc.rockbot.dds.service.OrganizationService;
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

class OrganizationControllerTest {

    @Mock
    private OrganizationService organizationService;

    @InjectMocks
    private OrganizationController organizationController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(organizationController).build();
    }

    @Test
    void createOrganization_ShouldReturnCreatedOrganization() throws Exception {
        // Given
        Organization organization = new Organization();
        organization.setId("org1");
        when(organizationService.createOrganization(any(Organization.class))).thenReturn(organization);

        // When & Then
        mockMvc.perform(post("/api/organizations")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"org1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("org1"));
    }

    @Test
    void getOrganizationByOrgId_ShouldReturnOrganization() throws Exception {
        // Given
        Organization organization = new Organization();
        organization.setId("org1");
        when(organizationService.getOrganizationByOrgId("org1")).thenReturn(organization);

        // When & Then
        mockMvc.perform(get("/api/organizations/orgid/org1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("org1"));
    }

    @Test
    void getOrganizationByOrgId_WhenNotFound_ShouldReturn404() throws Exception {
        // Given
        when(organizationService.getOrganizationByOrgId("org1")).thenReturn(null);

        // When & Then
        mockMvc.perform(get("/api/organizations/orgid/org1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getAllOrganizations_ShouldReturnAllOrganizations() throws Exception {
        // Given
        Organization org1 = new Organization();
        org1.setId("org1");
        Organization org2 = new Organization();
        org2.setId("org2");
        List<Organization> organizations = Arrays.asList(org1, org2);
        when(organizationService.getAllOrganizations()).thenReturn(organizations);

        // When & Then
        mockMvc.perform(get("/api/organizations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value("org1"))
                .andExpect(jsonPath("$[1].id").value("org2"));
    }

    @Test
    void updateOrganization_ShouldReturnUpdatedOrganization() throws Exception {
        // Given
        Organization organization = new Organization();
        organization.setId("org1");
        when(organizationService.updateOrganization(any(Organization.class))).thenReturn(organization);

        // When & Then
        mockMvc.perform(put("/api/organizations/org1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":\"org1\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value("org1"));
    }

    @Test
    void deleteOrganization_ShouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/organizations/org1"))
                .andExpect(status().isOk());
    }
} 