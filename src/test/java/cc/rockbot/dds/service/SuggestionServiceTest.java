package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.SuggestionListResponse;
import cc.rockbot.dds.dto.SuggestionRequest;
import cc.rockbot.dds.dto.SuggestionResponse;
import cc.rockbot.dds.entity.Feedback;
import cc.rockbot.dds.entity.Suggestion;
import cc.rockbot.dds.repository.FeedbackRepository;
import cc.rockbot.dds.repository.SuggestionRepository;
import cc.rockbot.dds.service.impl.SuggestionServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuggestionServiceTest {

    @Mock
    private SuggestionRepository suggestionRepository;

    @Mock
    private FeedbackRepository feedbackRepository;

    @InjectMocks
    private SuggestionServiceImpl suggestionService;

    private Suggestion suggestion;
    private SuggestionRequest suggestionRequest;
    private Feedback feedback;

    @BeforeEach
    void setUp() {
        suggestion = new Suggestion();
        suggestion.setId(1L);
        suggestion.setGmtCreate(LocalDateTime.now());
        suggestion.setGmtModified(LocalDateTime.now());
        suggestion.setTitle("Test Suggestion");
        suggestion.setProblemDescription("Test Problem");
        suggestion.setProblemAnalysis("Test Analysis");
        suggestion.setSuggestion("Test Solution");
        suggestion.setExpectedOutcome("Test Effect");
        suggestion.setImageUrls(Arrays.asList("http://example.com/image1.jpg", "http://example.com/image2.jpg"));
        suggestion.setUserWxid("test_wxid");
        suggestion.setStatus(0);
        suggestion.setOrgId("test_org");

        suggestionRequest = new SuggestionRequest();
        suggestionRequest.setTitle("Test Suggestion");
        suggestionRequest.setProblem("Test Problem");
        suggestionRequest.setAnalysis("Test Analysis");
        suggestionRequest.setSuggestions(Arrays.asList("Test Solution"));
        suggestionRequest.setExpectedEffect("Test Effect");
        suggestionRequest.setImages(Arrays.asList("http://example.com/image1.jpg", "http://example.com/image2.jpg"));
        suggestionRequest.setSubmitterWxid("test_wxid");
        suggestionRequest.setOrganizationId("test_org");

        feedback = new Feedback();
        feedback.setId(1L);
        feedback.setContent("Test Feedback");
        feedback.setAuthor("System");
        feedback.setSuggestion(suggestion);
        feedback.setGmtCreate(LocalDateTime.now());
        feedback.setGmtModified(LocalDateTime.now());
    }

    @Test
    void getSuggestions_ShouldReturnPaginatedSuggestions() {
        Page<Suggestion> page = new PageImpl<>(Collections.singletonList(suggestion));
        when(suggestionRepository.findAll(any(PageRequest.class))).thenReturn(page);

        SuggestionListResponse response = suggestionService.getSuggestions(PageRequest.of(0, 10), null, null);

        assertNotNull(response);
        assertEquals(1, response.getList().size());
        assertEquals(1, response.getTotal());
        verify(suggestionRepository, times(1)).findAll(any(PageRequest.class));
    }

    @Test
    void getSuggestions_WithStatus_ShouldReturnFilteredSuggestions() {
        Page<Suggestion> page = new PageImpl<>(Collections.singletonList(suggestion));
        when(suggestionRepository.findByStatus(anyInt(), any(PageRequest.class))).thenReturn(page);

        SuggestionListResponse response = suggestionService.getSuggestions(PageRequest.of(0, 10), "0", null);

        assertNotNull(response);
        assertEquals(1, response.getList().size());
        assertEquals(1, response.getTotal());
        verify(suggestionRepository, times(1)).findByStatus(anyInt(), any(PageRequest.class));
    }

    @Test
    void getSuggestion_ShouldReturnSuggestion() {
        when(suggestionRepository.findById(1L)).thenReturn(Optional.of(suggestion));
        when(feedbackRepository.findBySuggestionId(1L)).thenReturn(Collections.emptyList());

        SuggestionResponse response = suggestionService.getSuggestion(1L);

        assertNotNull(response);
        assertEquals(suggestion.getId(), response.getId());
        assertEquals(suggestion.getTitle(), response.getTitle());
        verify(suggestionRepository, times(1)).findById(1L);
    }

    @Test
    void getSuggestion_WhenNotFound_ShouldThrowException() {
        when(suggestionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            suggestionService.getSuggestion(1L)
        );
        verify(suggestionRepository, times(1)).findById(1L);
    }

    @Test
    void createSuggestion_ShouldReturnSavedSuggestion() {
        when(suggestionRepository.save(any(Suggestion.class))).thenReturn(suggestion);

        SuggestionResponse response = suggestionService.createSuggestion(suggestionRequest);

        assertNotNull(response);
        assertEquals(suggestion.getId(), response.getId());
        assertEquals(suggestion.getTitle(), response.getTitle());
        verify(suggestionRepository, times(1)).save(any(Suggestion.class));
    }

    @Test
    void updateSuggestionStatus_ShouldUpdateStatus() {
        when(suggestionRepository.findById(1L)).thenReturn(Optional.of(suggestion));
        when(suggestionRepository.save(any(Suggestion.class))).thenReturn(suggestion);

        suggestionService.updateSuggestionStatus(1L, "1");

        assertEquals(1, suggestion.getStatus());
        verify(suggestionRepository, times(1)).findById(1L);
        verify(suggestionRepository, times(1)).save(any(Suggestion.class));
    }

    @Test
    void updateSuggestionStatus_WhenNotFound_ShouldThrowException() {
        when(suggestionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            suggestionService.updateSuggestionStatus(1L, "1")
        );
        verify(suggestionRepository, times(1)).findById(1L);
        verify(suggestionRepository, never()).save(any(Suggestion.class));
    }

    @Test
    void addFeedback_ShouldSaveFeedback() {
        when(suggestionRepository.findById(1L)).thenReturn(Optional.of(suggestion));
        when(feedbackRepository.save(any(Feedback.class))).thenReturn(feedback);

        suggestionService.addFeedback(1L, "Test Feedback");

        verify(suggestionRepository, times(1)).findById(1L);
        verify(feedbackRepository, times(1)).save(any(Feedback.class));
    }

    @Test
    void addFeedback_WhenSuggestionNotFound_ShouldThrowException() {
        when(suggestionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            suggestionService.addFeedback(1L, "Test Feedback")
        );
        verify(suggestionRepository, times(1)).findById(1L);
        verify(feedbackRepository, never()).save(any(Feedback.class));
    }
} 