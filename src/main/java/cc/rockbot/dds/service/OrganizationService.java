package cc.rockbot.dds.service;

import cc.rockbot.dds.entity.Organization;
import cc.rockbot.dds.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class OrganizationService {
    private final OrganizationRepository organizationRepository;

    @Autowired
    public OrganizationService(OrganizationRepository organizationRepository) {
        this.organizationRepository = organizationRepository;
    }

    @Transactional
    public Organization createOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    public Optional<Organization> getOrganizationById(String id) {
        return organizationRepository.findById(id);
    }

    public Organization getOrganizationByOrgId(String orgId) {
        return organizationRepository.findByOrgId(orgId);
    }

    public List<Organization> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    @Transactional
    public Organization updateOrganization(Organization organization) {
        return organizationRepository.save(organization);
    }

    @Transactional
    public void deleteOrganization(String id) {
        organizationRepository.deleteById(id);
    }

    public boolean existsByOrgId(String orgId) {
        return organizationRepository.existsByOrgId(orgId);
    }

    public boolean existsById(String id) {
        return organizationRepository.existsById(id);
    }
} 