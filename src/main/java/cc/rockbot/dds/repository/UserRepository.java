package cc.rockbot.dds.repository;

import cc.rockbot.dds.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByWxid(String wxid);
    boolean existsByWxid(String wxid);
    List<User> findByOrgId(String orgId);
} 