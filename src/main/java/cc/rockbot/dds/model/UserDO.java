package cc.rockbot.dds.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "users")
public class UserDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gmt_create", nullable = false, updatable = false)
    private LocalDateTime gmtCreate;

    @Column(name = "gmt_modified", nullable = false)
    private LocalDateTime gmtModified;

    @NotBlank(message = "Wxid cannot be empty")
    @Size(max = 255, message = "Wxid must be less than 255 characters")
    @Column(name = "wxid", nullable = false, unique = true)
    private String wxid;

    @NotBlank(message = "User name cannot be empty")
    @Size(max = 127, message = "User name must be less than 127 characters")
    @Column(name = "user_name", nullable = false)
    private String userName;

    @NotBlank(message = "User organization cannot be empty")
    @Size(max = 255, message = "User organization must be less than 255 characters")
    @Column(name = "user_org", nullable = false)
    private String userOrg;

    @NotBlank(message = "User phone cannot be empty")
    @Pattern(regexp = "^1[3-9]\\d{9}$", message = "Invalid phone number format")
    @Size(max = 20, message = "User phone must be less than 20 characters")
    @Column(name = "user_phone", nullable = false)
    private String userPhone;

    @NotNull(message = "Status cannot be null")
    @Min(value = 0, message = "Status must be 0 or 1")
    @Max(value = 1, message = "Status must be 0 or 1")
    @Column(name = "status", nullable = false)
    private Integer status;

    @NotBlank(message = "Organization ID cannot be empty")
    @Size(max = 255, message = "Organization ID must be less than 255 characters")
    @Column(name = "org_id", nullable = false)
    private String orgId;
} 