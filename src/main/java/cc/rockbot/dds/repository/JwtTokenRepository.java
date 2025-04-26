package cc.rockbot.dds.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import cc.rockbot.dds.model.JwtToken;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByToken(String token);
    
    Optional<JwtToken> findByWxid(String wxid);
    
    @Modifying
    @Query("UPDATE JwtToken j SET j.gmtExpired = ?2 WHERE j.wxid = ?1")
    void updateExpiredTimeByWxid(String wxid, LocalDateTime expiredTime);
    
    void deleteByGmtExpiredBefore(LocalDateTime dateTime);
} 