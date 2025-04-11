package cc.rockbot.dds.dto;

import lombok.Data;

import java.util.List;

@Data
public class SuggestionListResponse {
    private List<SuggestionListItem> list;
    private long total;

    @Data
    public static class SuggestionListItem {
        private Long id;
        private String title;
        private String status;
        private String date;
    }
} 