package cc.rockbot.dds.controller;

import cc.rockbot.dds.entity.Suggestion;
import cc.rockbot.dds.service.SuggestionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class SuggestionControllerTest {

    @Mock
    private SuggestionService suggestionService;

    @InjectMocks
    private SuggestionController suggestionController;

    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(suggestionController).build();
    }

    @Test
    void createSuggestion_ShouldReturnCreatedSuggestion() throws Exception {
        // Given
        Suggestion suggestion = new Suggestion();
        suggestion.setId(1L);
        when(suggestionService.createSuggestion(any(Suggestion.class))).thenReturn(suggestion);

        // When & Then
        mockMvc.perform(post("/api/suggestions")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getSuggestionById_ShouldReturnSuggestion() throws Exception {
        // Given
        Suggestion suggestion = new Suggestion();
        suggestion.setId(1L);
        when(suggestionService.getSuggestionById(1L)).thenReturn(Optional.of(suggestion));

        // When & Then
        mockMvc.perform(get("/api/suggestions/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void getSuggestionById_WhenNotFound_ShouldReturn404() throws Exception {
        // Given
        when(suggestionService.getSuggestionById(1L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/suggestions/1"))
                .andExpect(status().isNotFound());
    }

    @Test
    void getSuggestionsByUserWxid_ShouldReturnSuggestions() throws Exception {
        // Given
        Suggestion suggestion1 = new Suggestion();
        suggestion1.setId(1L);
        Suggestion suggestion2 = new Suggestion();
        suggestion2.setId(2L);
        List<Suggestion> suggestions = Arrays.asList(suggestion1, suggestion2);
        when(suggestionService.getSuggestionsByUserWxid("wxid1")).thenReturn(suggestions);

        // When & Then
        mockMvc.perform(get("/api/suggestions/user/wxid1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getSuggestionsByOrgId_ShouldReturnSuggestions() throws Exception {
        // Given
        Suggestion suggestion1 = new Suggestion();
        suggestion1.setId(1L);
        Suggestion suggestion2 = new Suggestion();
        suggestion2.setId(2L);
        List<Suggestion> suggestions = Arrays.asList(suggestion1, suggestion2);
        when(suggestionService.getSuggestionsByOrgId("org1")).thenReturn(suggestions);

        // When & Then
        mockMvc.perform(get("/api/suggestions/org/org1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getSuggestionsByStatus_ShouldReturnSuggestions() throws Exception {
        // Given
        Suggestion suggestion1 = new Suggestion();
        suggestion1.setId(1L);
        Suggestion suggestion2 = new Suggestion();
        suggestion2.setId(2L);
        List<Suggestion> suggestions = Arrays.asList(suggestion1, suggestion2);
        when(suggestionService.getSuggestionsByStatus(0)).thenReturn(suggestions);

        // When & Then
        mockMvc.perform(get("/api/suggestions/status/0"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void getAllSuggestions_ShouldReturnAllSuggestions() throws Exception {
        // Given
        Suggestion suggestion1 = new Suggestion();
        suggestion1.setId(1L);
        Suggestion suggestion2 = new Suggestion();
        suggestion2.setId(2L);
        List<Suggestion> suggestions = Arrays.asList(suggestion1, suggestion2);
        when(suggestionService.getAllSuggestions()).thenReturn(suggestions);

        // When & Then
        mockMvc.perform(get("/api/suggestions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[1].id").value(2));
    }

    @Test
    void updateSuggestion_ShouldReturnUpdatedSuggestion() throws Exception {
        // Given
        Suggestion suggestion = new Suggestion();
        suggestion.setId(1L);
        when(suggestionService.updateSuggestion(any(Suggestion.class))).thenReturn(suggestion);

        // When & Then
        mockMvc.perform(put("/api/suggestions/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    void deleteSuggestion_ShouldReturnOk() throws Exception {
        // When & Then
        mockMvc.perform(delete("/api/suggestions/1"))
                .andExpect(status().isOk());
    }
} 