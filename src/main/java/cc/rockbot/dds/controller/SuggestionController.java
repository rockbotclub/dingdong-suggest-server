package cc.rockbot.dds.controller;

import cc.rockbot.dds.dto.SuggestionRequest;
import cc.rockbot.dds.dto.SuggestionResponse;
import cc.rockbot.dds.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suggestions")
@RequiredArgsConstructor
public class SuggestionController {

    private final SuggestionService suggestionService;

    @PostMapping
    public ResponseEntity<SuggestionResponse> createSuggestion(@RequestBody SuggestionRequest request) {
        return ResponseEntity.ok(suggestionService.createSuggestion(request));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SuggestionResponse> getSuggestion(@PathVariable Long id) {
        return ResponseEntity.ok(suggestionService.getSuggestionById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Void> updateSuggestion(@PathVariable Long id, @RequestBody SuggestionRequest request) {
        suggestionService.updateSuggestion(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuggestion(@PathVariable Long id) {
        suggestionService.deleteSuggestion(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<SuggestionResponse>> getAllSuggestions() {
        return ResponseEntity.ok(suggestionService.getAllSuggestions());
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        suggestionService.updateSuggestionStatus(id, status);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}/withdraw")
    public ResponseEntity<Void> withdrawSuggestion(@PathVariable Long id) {
        suggestionService.updateSuggestionStatus(id, "WITHDRAWN");
        return ResponseEntity.ok().build();
    }
} 