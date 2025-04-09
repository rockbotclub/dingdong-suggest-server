package cc.rockbot.dds.service;

import cc.rockbot.dds.entity.Suggestion;
import cc.rockbot.dds.repository.SuggestionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SuggestionServiceTest {

    @Mock
    private SuggestionRepository suggestionRepository;

    @InjectMocks
    private SuggestionService suggestionService;

    private Suggestion suggestion;

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
    }

    @Test
    void createSuggestion_ShouldReturnSavedSuggestion() {
        when(suggestionRepository.save(any(Suggestion.class))).thenReturn(suggestion);

        Suggestion savedSuggestion = suggestionService.createSuggestion(suggestion);

        assertNotNull(savedSuggestion);
        assertEquals(suggestion.getId(), savedSuggestion.getId());
        assertEquals(suggestion.getTitle(), savedSuggestion.getTitle());
        verify(suggestionRepository, times(1)).save(suggestion);
    }

    @Test
    void getSuggestionById_ShouldReturnSuggestion() {
        when(suggestionRepository.findById(1L)).thenReturn(Optional.of(suggestion));

        Optional<Suggestion> foundSuggestion = suggestionService.getSuggestionById(1L);

        assertTrue(foundSuggestion.isPresent());
        assertEquals(suggestion.getId(), foundSuggestion.get().getId());
        verify(suggestionRepository, times(1)).findById(1L);
    }

    @Test
    void getSuggestionById_WhenNotFound_ShouldReturnEmpty() {
        when(suggestionRepository.findById(1L)).thenReturn(Optional.empty());

        Optional<Suggestion> foundSuggestion = suggestionService.getSuggestionById(1L);

        assertFalse(foundSuggestion.isPresent());
        verify(suggestionRepository, times(1)).findById(1L);
    }

    @Test
    void getSuggestionsByUserWxid_ShouldReturnSuggestions() {
        List<Suggestion> suggestions = Arrays.asList(suggestion);
        when(suggestionRepository.findByUserWxid("test_wxid")).thenReturn(suggestions);

        List<Suggestion> foundSuggestions = suggestionService.getSuggestionsByUserWxid("test_wxid");

        assertNotNull(foundSuggestions);
        assertEquals(1, foundSuggestions.size());
        verify(suggestionRepository, times(1)).findByUserWxid("test_wxid");
    }

    @Test
    void getSuggestionsByOrgId_ShouldReturnSuggestions() {
        List<Suggestion> suggestions = Arrays.asList(suggestion);
        when(suggestionRepository.findByOrgId("test_org")).thenReturn(suggestions);

        List<Suggestion> foundSuggestions = suggestionService.getSuggestionsByOrgId("test_org");

        assertNotNull(foundSuggestions);
        assertEquals(1, foundSuggestions.size());
        verify(suggestionRepository, times(1)).findByOrgId("test_org");
    }

    @Test
    void getSuggestionsByStatus_ShouldReturnSuggestions() {
        List<Suggestion> suggestions = Arrays.asList(suggestion);
        when(suggestionRepository.findByStatus(0)).thenReturn(suggestions);

        List<Suggestion> foundSuggestions = suggestionService.getSuggestionsByStatus(0);

        assertNotNull(foundSuggestions);
        assertEquals(1, foundSuggestions.size());
        verify(suggestionRepository, times(1)).findByStatus(0);
    }

    @Test
    void getAllSuggestions_ShouldReturnAllSuggestions() {
        List<Suggestion> suggestions = Arrays.asList(suggestion);
        when(suggestionRepository.findAll()).thenReturn(suggestions);

        List<Suggestion> foundSuggestions = suggestionService.getAllSuggestions();

        assertNotNull(foundSuggestions);
        assertEquals(1, foundSuggestions.size());
        verify(suggestionRepository, times(1)).findAll();
    }

    @Test
    void updateSuggestion_ShouldReturnUpdatedSuggestion() {
        when(suggestionRepository.save(any(Suggestion.class))).thenReturn(suggestion);

        Suggestion updatedSuggestion = suggestionService.updateSuggestion(suggestion);

        assertNotNull(updatedSuggestion);
        assertEquals(suggestion.getId(), updatedSuggestion.getId());
        verify(suggestionRepository, times(1)).save(suggestion);
    }

    @Test
    void deleteSuggestion_ShouldCallRepository() {
        doNothing().when(suggestionRepository).deleteById(1L);

        suggestionService.deleteSuggestion(1L);

        verify(suggestionRepository, times(1)).deleteById(1L);
    }

    @Test
    void updateSuggestionStatus_ShouldReturnUpdatedSuggestion() {
        when(suggestionRepository.findById(1L)).thenReturn(Optional.of(suggestion));
        when(suggestionRepository.save(any(Suggestion.class))).thenReturn(suggestion);

        Suggestion updatedSuggestion = suggestionService.updateSuggestionStatus(1L, 1);

        assertNotNull(updatedSuggestion);
        assertEquals(1, updatedSuggestion.getStatus());
        verify(suggestionRepository, times(1)).findById(1L);
        verify(suggestionRepository, times(1)).save(suggestion);
    }

    @Test
    void updateSuggestionStatus_WhenNotFound_ShouldThrowException() {
        when(suggestionRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> 
            suggestionService.updateSuggestionStatus(1L, 1)
        );
        verify(suggestionRepository, times(1)).findById(1L);
        verify(suggestionRepository, never()).save(any(Suggestion.class));
    }
} 