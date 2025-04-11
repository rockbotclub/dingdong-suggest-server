package cc.rockbot.dds.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class SuggestionResponse {
    private Long id;
    private String title;
    private String problem;
    private String analysis;
    private List<String> suggestions;
    private String expectedEffect;
    private List<String> images;
    private String submitterName;
    private String submitterDepartment;
    private String submitterPhone;
    private String submitterWxid;
    private String organizationId;
    private String organizationName;
    private String status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
    private List<Feedback> feedbacks;

    @Data
    public static class Feedback {
        private String content;
        private LocalDateTime createTime;
        private String author;
    }
} 