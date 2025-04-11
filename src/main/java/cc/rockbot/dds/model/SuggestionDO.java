package cc.rockbot.dds.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "suggestions")
public class SuggestionDO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "gmt_create", nullable = false, updatable = false)
    private LocalDateTime gmtCreate;

    @Column(name = "gmt_modified", nullable = false)
    private LocalDateTime gmtModified;

    @NotBlank(message = "Title cannot be empty")
    @Size(max = 255, message = "Title must be less than 255 characters")
    @Column(name = "title", nullable = false)
    private String title;

    @NotBlank(message = "Problem description cannot be empty")
    @Column(name = "problem_description", nullable = false, columnDefinition = "TEXT")
    private String problemDescription;

    @Column(name = "problem_analysis", columnDefinition = "TEXT")
    private String problemAnalysis;

    @NotBlank(message = "Suggestion cannot be empty")
    @Column(name = "suggestion", nullable = false, columnDefinition = "TEXT")
    private String suggestion;

    @Column(name = "expected_outcome", columnDefinition = "TEXT")
    private String expectedOutcome;

    @Column(name = "image_urls", columnDefinition = "JSON")
    private String imageUrls;

    @NotBlank(message = "User wxid cannot be empty")
    @Size(max = 255, message = "User wxid must be less than 255 characters")
    @Column(name = "user_wxid", nullable = false)
    private String userWxid;

    @NotNull(message = "Status cannot be null")
    @Column(name = "status", nullable = false)
    private Integer status;

    @NotBlank(message = "Organization ID cannot be empty")
    @Size(max = 255, message = "Organization ID must be less than 255 characters")
    @Column(name = "org_id", nullable = false)
    private String orgId;
} 