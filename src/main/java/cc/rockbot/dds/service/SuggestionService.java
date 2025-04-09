package cc.rockbot.dds.service;

import cc.rockbot.dds.entity.Suggestion;
import cc.rockbot.dds.repository.SuggestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SuggestionService {
    private final SuggestionRepository suggestionRepository;

    @Autowired
    public SuggestionService(SuggestionRepository suggestionRepository) {
        this.suggestionRepository = suggestionRepository;
    }

    @Transactional
    public Suggestion createSuggestion(Suggestion suggestion) {
        return suggestionRepository.save(suggestion);
    }

    public Optional<Suggestion> getSuggestionById(Long id) {
        return suggestionRepository.findById(id);
    }

    public List<Suggestion> getSuggestionsByUserWxid(String userWxid) {
        return suggestionRepository.findByUserWxid(userWxid);
    }

    public List<Suggestion> getSuggestionsByOrgId(String orgId) {
        return suggestionRepository.findByOrgId(orgId);
    }

    public List<Suggestion> getSuggestionsByStatus(Integer status) {
        return suggestionRepository.findByStatus(status);
    }

    public List<Suggestion> getAllSuggestions() {
        return suggestionRepository.findAll();
    }

    @Transactional
    public Suggestion updateSuggestion(Suggestion suggestion) {
        return suggestionRepository.save(suggestion);
    }

    @Transactional
    public void deleteSuggestion(Long id) {
        suggestionRepository.deleteById(id);
    }

    @Transactional
    public Suggestion updateSuggestionStatus(Long id, Integer status) {
        Suggestion suggestion = suggestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suggestion not found with id: " + id));
        suggestion.setStatus(status);
        return suggestionRepository.save(suggestion);
    }
} 