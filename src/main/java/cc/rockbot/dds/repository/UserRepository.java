package cc.rockbot.dds.repository;

import cc.rockbot.dds.model.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserDO, Long> {
    UserDO findByWxid(String wxid);
    boolean existsByWxid(String wxid);
    List<UserDO> findByOrgId(String orgId);
} 