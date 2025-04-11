package cc.rockbot.dds.controller;

import cc.rockbot.dds.dto.SuggestionListResponse;
import cc.rockbot.dds.dto.SuggestionRequest;
import cc.rockbot.dds.dto.SuggestionResponse;
import cc.rockbot.dds.service.SuggestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SuggestionControllerTest {

    @Mock
    private SuggestionService suggestionService;

    @InjectMocks
    private SuggestionController suggestionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(suggestionController).build();
    }

    @Test
    void getSuggestions_ShouldReturnSuggestions() throws Exception {
        // Given
        SuggestionListResponse response = new SuggestionListResponse();
        response.setList(Collections.emptyList());
        response.setTotal(0L);
        when(suggestionService.getSuggestions(any(), any(), any())).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/suggestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.list").isArray())
                .andExpect(jsonPath("$.total").value(0));
    }

    @Test
    void getSuggestion_ShouldReturnSuggestion() throws Exception {
        // Given
        SuggestionResponse response = new SuggestionResponse();
        response.setId(1L);
        when(suggestionService.getSuggestion(1L)).thenReturn(response);

        // When & Then
        mockMvc.perform(get("/api/v1/suggestions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void createSuggestion_ShouldReturnCreatedSuggestion() throws Exception {
        // Given
        SuggestionResponse response = new SuggestionResponse();
        response.setId(1L);
        when(suggestionService.createSuggestion(any(SuggestionRequest.class))).thenReturn(response);

        // When & Then
        mockMvc.perform(post("/api/v1/suggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\":\"Test Suggestion\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void updateSuggestionStatus_ShouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/suggestions/1/status")
                .param("status", "1"))
                .andExpect(status().isOk());
    }

    @Test
    void addFeedback_ShouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/v1/suggestions/1/feedback")
                .param("content", "Test feedback"))
                .andExpect(status().isOk());
    }
} 