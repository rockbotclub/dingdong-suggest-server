package cc.rockbot.dds.repository;

import cc.rockbot.dds.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<Organization, String> {
    Organization findByOrgId(String orgId);
    boolean existsByOrgId(String orgId);
} 