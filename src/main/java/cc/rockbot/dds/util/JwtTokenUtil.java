package cc.rockbot.dds.util;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {
    private static final Key SECRET = Keys.secretKeyFor(SignatureAlgorithm.HS512);
    private static final long EXPIRATION_TIME = 86400000; // 24 hours

    /**
     * 生成JWT token
     * @param userId
     * @return
     */
    public static String generateToken(Long userId) {
        return Jwts.builder()
                .setSubject(userId.toString())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(SECRET)
                .compact();
    }

    /**
     * 从JWT token中获取用户信息
     * todo: 能从JWT Token反查出来用户信息吗？还是说它要以kv的形式存在redis里边？
     * @param jwtToken
     * @return
     */
    public static String getWxidFromToken(String jwtToken) {
        return Jwts.parser()
                .setSigningKey(SECRET)
                .parseClaimsJws(jwtToken)
                .getBody()
                .getSubject();
    }
} 