package cc.rockbot.dds.util;

import cc.rockbot.dds.model.JwtToken;
import cc.rockbot.dds.repository.JwtTokenRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.time.LocalDateTime;
import java.util.Date;
import java.util.Optional;
import org.apache.commons.lang3.StringUtils;

@Component
public class JwtTokenService {
    private static final Key SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    @Autowired
    private JwtTokenRepository jwtTokenRepository;

    /**
     * 生成JWT token
     * 如果token不存在，则生成一个
     * 如果token存在，则返回token
     * @param wxid 微信ID
     * @return JWT token
     */
    public String generateTokenIfNotExist(String wxid) {
        Optional<JwtToken> existingToken = jwtTokenRepository.findByWxid(wxid);
        
        if (existingToken.isPresent()) {
            JwtToken token = existingToken.get();
            if (token.getGmtExpired() != null && token.getGmtExpired().isAfter(LocalDateTime.now())) {
                return token.getToken();
            }
        }

        String token = Jwts.builder()
                .setSubject(wxid)
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET)
                .compact();

        LocalDateTime expiredTime = LocalDateTime.now().plusHours(24);
        
        if (existingToken.isPresent()) {
            jwtTokenRepository.updateExpiredTimeByWxid(wxid, expiredTime);
        } else {
            JwtToken newToken = new JwtToken();
            newToken.setToken(token);
            newToken.setWxid(wxid);
            newToken.setGmtExpired(expiredTime);
            newToken.setGmtCreate(LocalDateTime.now());
            newToken.setGmtModified(LocalDateTime.now());
            jwtTokenRepository.save(newToken);
        }

        return token;
    }

    /**
     * 根据JWT token查询wxId
     * 从数据里查询啊
     * @param jwtToken JWT token
     * @return 微信ID
     */
    public String getWxidFromToken(String jwtToken) {
        Optional<JwtToken> existingToken = jwtTokenRepository.findByToken(jwtToken);
        if (existingToken.isPresent()) {
            return existingToken.get().getWxid();
        }
        return null;
    }

    /**
     * 清理过期的token
     */
    public void cleanExpiredTokens() {
        jwtTokenRepository.deleteByGmtExpiredBefore(LocalDateTime.now());
    }

    /**
     * 检查token是否有效
     * @param jwtToken JWT token
     * @return 是否有效
     */
    public boolean isTokenValid(String jwtToken) {
        // 判断jwt token是否为空
        if (StringUtils.isBlank(jwtToken)) {
            return false;
        }
        // 判断jwt token是否存在
        return jwtTokenRepository.findByToken(jwtToken).isPresent();
    }
} 
