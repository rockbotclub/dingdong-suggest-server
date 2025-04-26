package cc.rockbot.dds.controller;

import cc.rockbot.dds.dto.SuggestionRequest;
import cc.rockbot.dds.dto.SuggestionResponse;
import cc.rockbot.dds.dto.SuggestionLiteDTO;
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

    
    /**
     * 根据JWT token获取用户的所有建议列表
     * 
     * @param jwtToken JWT token
     * @param orgId 组织ID
     * @param year 年份
     * @return 建议列表
     */
    @GetMapping
    public ResponseEntity<List<SuggestionLiteDTO>> getAllSuggestions(@RequestParam String jwtToken
                                                                    , @RequestParam String orgId
                                                                    , @RequestParam String year) {
        return ResponseEntity.ok(suggestionService.getAllSuggestions(jwtToken, orgId, year));
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