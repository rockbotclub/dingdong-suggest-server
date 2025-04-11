package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.SuggestionListResponse;
import cc.rockbot.dds.dto.SuggestionRequest;
import cc.rockbot.dds.dto.SuggestionResponse;
import org.springframework.data.domain.PageRequest;

public interface SuggestionService {
    SuggestionListResponse getSuggestions(PageRequest pageRequest, String status, String organizationId);
    SuggestionResponse getSuggestion(Long suggestionId);
    SuggestionResponse createSuggestion(SuggestionRequest request);
    void updateSuggestionStatus(Long suggestionId, String status);
    void addFeedback(Long suggestionId, String content);
} 