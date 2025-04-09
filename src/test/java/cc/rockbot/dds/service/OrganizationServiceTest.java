package cc.rockbot.dds.service;

import cc.rockbot.dds.entity.Organization;
import cc.rockbot.dds.repository.OrganizationRepository;
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
public class OrganizationServiceTest {

    @Mock
    private OrganizationRepository organizationRepository;

    @InjectMocks
    private OrganizationService organizationService;

    private Organization organization;

    @BeforeEach
    void setUp() {
        organization = new Organization();
        organization.setId("test_org_id");
        organization.setOrgName("Test Organization");
        organization.setAddress("Test Address");
    }

    @Test
    void testCreateOrganization() {
        when(organizationRepository.save(any(Organization.class))).thenReturn(organization);

        Organization createdOrganization = organizationService.createOrganization(organization);
        assertNotNull(createdOrganization);
        assertEquals("test_org_id", createdOrganization.getId());
        assertEquals("Test Organization", createdOrganization.getOrgName());

        verify(organizationRepository).save(any(Organization.class));
    }

    @Test
    void testGetOrganizationById() {
        when(organizationRepository.findById("test_org_id")).thenReturn(Optional.of(organization));

        Optional<Organization> foundOrganization = organizationService.getOrganizationById("test_org_id");
        assertTrue(foundOrganization.isPresent());
        assertEquals("test_org_id", foundOrganization.get().getId());

        verify(organizationRepository).findById("test_org_id");
    }

    @Test
    void testGetOrganizationByOrgId() {
        when(organizationRepository.findByOrgId("test_org_id")).thenReturn(organization);

        Organization foundOrganization = organizationService.getOrganizationByOrgId("test_org_id");
        assertNotNull(foundOrganization);
        assertEquals("test_org_id", foundOrganization.getId());

        verify(organizationRepository).findByOrgId("test_org_id");
    }

    @Test
    void testExistsByOrgId() {
        when(organizationRepository.existsByOrgId("test_org_id")).thenReturn(true);

        boolean exists = organizationService.existsByOrgId("test_org_id");
        assertTrue(exists);

        verify(organizationRepository).existsByOrgId("test_org_id");
    }

    @Test
    void testDeleteOrganization() {
        doNothing().when(organizationRepository).deleteById("test_org_id");

        organizationService.deleteOrganization("test_org_id");

        verify(organizationRepository).deleteById("test_org_id");
    }

    @Test
    void testExistsById() {
        when(organizationRepository.existsById("test_org_id")).thenReturn(true);

        boolean exists = organizationService.existsById("test_org_id");
        assertTrue(exists);

        verify(organizationRepository).existsById("test_org_id");
    }
} 