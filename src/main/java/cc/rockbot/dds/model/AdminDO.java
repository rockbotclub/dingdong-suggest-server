package cc.rockbot.dds.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "admins")
public class AdminDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gmt_create", nullable = false, updatable = false)
    private LocalDateTime gmtCreate;

    @Column(name = "gmt_modified", nullable = false)
    private LocalDateTime gmtModified;

    @NotBlank(message = "Admin wxid cannot be empty")
    @Size(max = 255, message = "Admin wxid must be less than 255 characters")
    @Column(name = "admin_wxid", nullable = false, unique = true)
    private String adminWxid;

    @NotBlank(message = "Admin name cannot be empty")
    @Size(max = 127, message = "Admin name must be less than 127 characters")
    @Column(name = "admin_name", nullable = false)
    private String adminName;

    @NotBlank(message = "Organization ID cannot be empty")
    @Size(max = 255, message = "Organization ID must be less than 255 characters")
    @Column(name = "org_id", nullable = false)
    private String orgId;

    @NotBlank(message = "Admin phone cannot be empty")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Invalid phone number format")
    @Size(max = 20, message = "Admin phone must be less than 20 characters")
    @Column(name = "admin_phone", nullable = false)
    private String adminPhone;

    @NotBlank(message = "Admin password cannot be empty")
    @Size(min = 6, max = 255, message = "Admin password must be between 6 and 255 characters")
    @Column(name = "admin_passwd", nullable = false)
    private String adminPasswd;
} 