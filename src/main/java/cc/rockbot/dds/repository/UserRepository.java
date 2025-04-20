package cc.rockbot.dds.repository;

import cc.rockbot.dds.model.UserDO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserDO, Long> {
    /**
     * 根据用户微信id查询用户
     * 由于一个微信id可以存在于多个组织里边，所以可能对应多个用户，需要返回一个列表
     * @param wxid
     * @return
     */
    List<UserDO> findByWxid(String wxid);
    boolean existsByWxid(String wxid);
    List<UserDO> findByOrgId(String orgId);
    UserDO findByUserPhone(String userPhone);
} 