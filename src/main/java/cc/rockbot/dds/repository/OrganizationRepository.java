package cc.rockbot.dds.repository;

import cc.rockbot.dds.model.OrganizationDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrganizationRepository extends JpaRepository<OrganizationDO, String> {
} 