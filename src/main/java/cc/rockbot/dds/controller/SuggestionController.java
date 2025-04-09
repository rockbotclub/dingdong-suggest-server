package cc.rockbot.dds.controller;

import cc.rockbot.dds.entity.Suggestion;
import cc.rockbot.dds.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
public class SuggestionController {
    private final SuggestionService suggestionService;

    @Autowired
    public SuggestionController(SuggestionService suggestionService) {
        this.suggestionService = suggestionService;
    }

    @PostMapping
    public ResponseEntity<Suggestion> createSuggestion(@RequestBody Suggestion suggestion) {
        return ResponseEntity.ok(suggestionService.createSuggestion(suggestion));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Suggestion> getSuggestionById(@PathVariable Long id) {
        return suggestionService.getSuggestionById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/user/{userWxid}")
    public ResponseEntity<List<Suggestion>> getSuggestionsByUserWxid(@PathVariable String userWxid) {
        return ResponseEntity.ok(suggestionService.getSuggestionsByUserWxid(userWxid));
    }

    @GetMapping("/org/{orgId}")
    public ResponseEntity<List<Suggestion>> getSuggestionsByOrgId(@PathVariable String orgId) {
        return ResponseEntity.ok(suggestionService.getSuggestionsByOrgId(orgId));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<List<Suggestion>> getSuggestionsByStatus(@PathVariable Integer status) {
        return ResponseEntity.ok(suggestionService.getSuggestionsByStatus(status));
    }

    @GetMapping
    public ResponseEntity<List<Suggestion>> getAllSuggestions() {
        return ResponseEntity.ok(suggestionService.getAllSuggestions());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Suggestion> updateSuggestion(@PathVariable Long id, @RequestBody Suggestion suggestion) {
        suggestion.setId(id);
        return ResponseEntity.ok(suggestionService.updateSuggestion(suggestion));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSuggestion(@PathVariable Long id) {
        suggestionService.deleteSuggestion(id);
        return ResponseEntity.ok().build();
    }
} 