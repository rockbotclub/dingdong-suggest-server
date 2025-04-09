package cc.rockbot.dds.repository;

import cc.rockbot.dds.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    User findByUserWxid(String userWxid);
    boolean existsByUserWxid(String userWxid);
    List<User> findByOrgId(String orgId);
} 