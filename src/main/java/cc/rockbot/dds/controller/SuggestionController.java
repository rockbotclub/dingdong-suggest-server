package cc.rockbot.dds.controller;

import cc.rockbot.dds.dto.SuggestionListResponse;
import cc.rockbot.dds.dto.SuggestionRequest;
import cc.rockbot.dds.dto.SuggestionResponse;
import cc.rockbot.dds.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/suggestions")
public class SuggestionController {
    private final SuggestionService suggestionService;

    @Autowired
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @GetMapping
    public ResponseEntity<SuggestionListResponse> getSuggestions(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int pageSize,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String organizationId) {
        return ResponseEntity.ok(suggestionService.getSuggestions(
                PageRequest.of(page - 1, pageSize), status, organizationId));
    }

    @GetMapping("/{suggestionId}")
    public ResponseEntity<SuggestionResponse> getSuggestion(@PathVariable Long suggestionId) {
        return ResponseEntity.ok(suggestionService.getSuggestion(suggestionId));
    }

    @PostMapping
    public ResponseEntity<SuggestionResponse> createSuggestion(@RequestBody SuggestionRequest request) {
        return ResponseEntity.ok(suggestionService.createSuggestion(request));
    }

    @PostMapping("/{suggestionId}/status")
    public ResponseEntity<?> updateSuggestionStatus(
            @PathVariable Long suggestionId,
            @RequestParam String status) {
        suggestionService.updateSuggestionStatus(suggestionId, status);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/{suggestionId}/feedback")
    public ResponseEntity<?> addFeedback(
            @PathVariable Long suggestionId,
            @RequestParam String content) {
        suggestionService.addFeedback(suggestionId, content);
        return ResponseEntity.ok().build();
    }
} 