package cc.rockbot.dds.model;

import javax.persistence.*;
import lombok.Data;
import java.time.LocalDateTime;



@Data
@Entity
@Table(name = "jwt_tokens")
public class JwtToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gmt_create", updatable = false)
    private LocalDateTime gmtCreate;

    @Column(name = "gmt_modified")
    private LocalDateTime gmtModified;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @Column(name = "wxid", nullable = false)
    private String wxid;

    @Column(name = "gmt_expired")
    private LocalDateTime gmtExpired;
} 