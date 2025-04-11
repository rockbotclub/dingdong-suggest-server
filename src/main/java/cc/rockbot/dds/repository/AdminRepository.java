package cc.rockbot.dds.repository;

import cc.rockbot.dds.model.AdminDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminRepository extends JpaRepository<AdminDO, Long> {
    AdminDO findByAdminWxid(String adminWxid);
    boolean existsByAdminWxid(String adminWxid);
} 