package cc.rockbot.dds.service;

import cc.rockbot.dds.model.OrganizationDO;
import cc.rockbot.dds.repository.OrganizationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

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
    public OrganizationDO createOrganization(OrganizationDO organization) {
        if (organization == null) {
            throw new IllegalArgumentException("Organization cannot be null");
        }
        return organizationRepository.save(organization);
    }

    public Optional<OrganizationDO> getOrganizationById(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Organization ID cannot be null or empty");
        }
        return organizationRepository.findById(id);
    }

    public List<OrganizationDO> getAllOrganizations() {
        return organizationRepository.findAll();
    }

    @Transactional
    public OrganizationDO updateOrganization(OrganizationDO organization) {
        if (organization == null) {
            throw new IllegalArgumentException("Organization cannot be null");
        }
        if (!StringUtils.hasText(organization.getId())) {
            throw new IllegalArgumentException("Organization ID cannot be null");
        }
        if (!organizationRepository.existsById(organization.getId())) {
            throw new RuntimeException("Organization not found");
        }
        return organizationRepository.save(organization);
    }

    @Transactional
    public void deleteOrganization(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Organization ID cannot be null or empty");
        }
        if (!organizationRepository.existsById(id)) {
            throw new RuntimeException("Organization not found");
        }
        organizationRepository.deleteById(id);
    }

    public boolean existsById(String id) {
        if (!StringUtils.hasText(id)) {
            throw new IllegalArgumentException("Organization ID cannot be null or empty");
        }
        return organizationRepository.existsById(id);
    }
} 