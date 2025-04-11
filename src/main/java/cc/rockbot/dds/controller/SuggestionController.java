package cc.rockbot.dds.controller;

import cc.rockbot.dds.dto.SuggestionRequest;
import cc.rockbot.dds.dto.SuggestionResponse;
import cc.rockbot.dds.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/suggestions")
@RequiredArgsConstructor
public class SuggestionController {

    private final SuggestionService suggestionService;

    @PostMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuggestionResponse> createSuggestion(
            @RequestBody SuggestionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        request.setUserWxid(userDetails.getUsername());
        return ResponseEntity.ok(suggestionService.createSuggestion(request));
    }

    @GetMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<SuggestionResponse> getSuggestion(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        SuggestionResponse response = suggestionService.getSuggestionById(id);
        // 检查是否是建议的创建者或管理员
        if (!response.getUserWxid().equals(userDetails.getUsername()) && 
            !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Unauthorized access");
        }
        return ResponseEntity.ok(response);
    }

    @PutMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> updateSuggestion(
            @PathVariable Long id,
            @RequestBody SuggestionRequest request,
            @AuthenticationPrincipal UserDetails userDetails) {
        SuggestionResponse existing = suggestionService.getSuggestionById(id);
        // 检查是否是建议的创建者或管理员
        if (!existing.getUserWxid().equals(userDetails.getUsername()) && 
            !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Unauthorized access");
        }
        suggestionService.updateSuggestion(id, request);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<Void> deleteSuggestion(
            @PathVariable Long id,
            @AuthenticationPrincipal UserDetails userDetails) {
        SuggestionResponse existing = suggestionService.getSuggestionById(id);
        // 检查是否是建议的创建者或管理员
        if (!existing.getUserWxid().equals(userDetails.getUsername()) && 
            !userDetails.getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new RuntimeException("Unauthorized access");
        }
        suggestionService.deleteSuggestion(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<SuggestionResponse>> getAllSuggestions(
            @AuthenticationPrincipal UserDetails userDetails) {
        return ResponseEntity.ok(suggestionService.getAllSuggestions());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> updateStatus(
            @PathVariable Long id,
            @RequestParam String status,
            @AuthenticationPrincipal UserDetails userDetails) {
        suggestionService.updateSuggestionStatus(id, status);
        return ResponseEntity.ok().build();
    }
} 