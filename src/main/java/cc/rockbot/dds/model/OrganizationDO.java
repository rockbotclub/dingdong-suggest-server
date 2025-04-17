package cc.rockbot.dds.model;

import javax.persistence.*;
import javax.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "organizations")
public class OrganizationDO {
    @Id
    @NotBlank(message = "Organization ID cannot be empty")
    @Size(max = 255, message = "Organization ID must be less than 255 characters")
    @Column(name = "id", nullable = false)
    private String id;

    @Column(name = "gmt_create", nullable = false, updatable = false)
    private LocalDateTime gmtCreate;

    @Column(name = "gmt_modified", nullable = false)
    private LocalDateTime gmtModified;

    @NotBlank(message = "Organization name cannot be empty")
    @Size(max = 255, message = "Organization name must be less than 255 characters")
    @Column(name = "org_name", nullable = false)
    private String orgName;

    @Size(max = 255, message = "Address must be less than 255 characters")
    @Column(name = "address")
    private String address;
} 