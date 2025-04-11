package cc.rockbot.dds.service.impl;

import cc.rockbot.dds.dto.SuggestionListResponse;
import cc.rockbot.dds.dto.SuggestionRequest;
import cc.rockbot.dds.dto.SuggestionResponse;
import cc.rockbot.dds.entity.Feedback;
import cc.rockbot.dds.entity.Suggestion;
import cc.rockbot.dds.repository.FeedbackRepository;
import cc.rockbot.dds.repository.SuggestionRepository;
import cc.rockbot.dds.service.SuggestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SuggestionServiceImpl implements SuggestionService {
    private final SuggestionRepository suggestionRepository;
    private final FeedbackRepository feedbackRepository;

    @Autowired
    public SuggestionServiceImpl(SuggestionRepository suggestionRepository,
                               FeedbackRepository feedbackRepository) {
        this.suggestionRepository = suggestionRepository;
        this.feedbackRepository = feedbackRepository;
    }

    @Override
    public SuggestionListResponse getSuggestions(PageRequest pageRequest, String status, String organizationId) {
        Page<Suggestion> page;
        if (status != null && organizationId != null) {
            page = suggestionRepository.findByStatusAndOrgId(Integer.parseInt(status), organizationId, pageRequest);
        } else if (status != null) {
            page = suggestionRepository.findByStatus(Integer.parseInt(status), pageRequest);
        } else if (organizationId != null) {
            page = suggestionRepository.findByOrgId(organizationId, pageRequest);
        } else {
            page = suggestionRepository.findAll(pageRequest);
        }

        SuggestionListResponse response = new SuggestionListResponse();
        response.setList(page.getContent().stream()
                .map(this::convertToListItem)
                .collect(Collectors.toList()));
        response.setTotal(page.getTotalElements());
        return response;
    }

    @Override
    public SuggestionResponse getSuggestion(Long suggestionId) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new RuntimeException("Suggestion not found with id: " + suggestionId));
        return convertToResponse(suggestion);
    }

    @Override
    @Transactional
    public SuggestionResponse createSuggestion(SuggestionRequest request) {
        Suggestion suggestion = new Suggestion();
        suggestion.setTitle(request.getTitle());
        suggestion.setProblemDescription(request.getProblem());
        suggestion.setProblemAnalysis(request.getAnalysis());
        suggestion.setSuggestion(String.join("\n", request.getSuggestions()));
        suggestion.setExpectedOutcome(request.getExpectedEffect());
        suggestion.setImageUrls(request.getImages());
        suggestion.setUserWxid(request.getSubmitterWxid());
        suggestion.setStatus(0); // Initial status
        suggestion.setOrgId(request.getOrganizationId());
        suggestion.setGmtCreate(LocalDateTime.now());
        suggestion.setGmtModified(LocalDateTime.now());

        Suggestion saved = suggestionRepository.save(suggestion);
        return convertToResponse(saved);
    }

    @Override
    @Transactional
    public void updateSuggestionStatus(Long suggestionId, String status) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new RuntimeException("Suggestion not found with id: " + suggestionId));
        suggestion.setStatus(Integer.parseInt(status));
        suggestion.setGmtModified(LocalDateTime.now());
        suggestionRepository.save(suggestion);
    }

    @Override
    @Transactional
    public void addFeedback(Long suggestionId, String content) {
        Suggestion suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new RuntimeException("Suggestion not found with id: " + suggestionId));

        Feedback feedback = new Feedback();
        feedback.setContent(content);
        feedback.setAuthor("System"); // TODO: Get current user
        feedback.setSuggestion(suggestion);
        feedback.setGmtCreate(LocalDateTime.now());
        feedback.setGmtModified(LocalDateTime.now());

        feedbackRepository.save(feedback);
    }

    private SuggestionListResponse.SuggestionListItem convertToListItem(Suggestion suggestion) {
        SuggestionListResponse.SuggestionListItem item = new SuggestionListResponse.SuggestionListItem();
        item.setId(suggestion.getId());
        item.setTitle(suggestion.getTitle());
        item.setStatus(String.valueOf(suggestion.getStatus()));
        item.setDate(suggestion.getGmtCreate().toString());
        return item;
    }

    private SuggestionResponse convertToResponse(Suggestion suggestion) {
        SuggestionResponse response = new SuggestionResponse();
        response.setId(suggestion.getId());
        response.setTitle(suggestion.getTitle());
        response.setProblem(suggestion.getProblemDescription());
        response.setAnalysis(suggestion.getProblemAnalysis());
        response.setSuggestions(List.of(suggestion.getSuggestion().split("\n")));
        response.setExpectedEffect(suggestion.getExpectedOutcome());
        response.setImages(suggestion.getImageUrls());
        response.setSubmitterWxid(suggestion.getUserWxid());
        response.setStatus(String.valueOf(suggestion.getStatus()));
        response.setOrganizationId(suggestion.getOrgId());
        response.setCreateTime(suggestion.getGmtCreate());
        response.setUpdateTime(suggestion.getGmtModified());

        // Add feedbacks
        List<Feedback> feedbacks = feedbackRepository.findBySuggestionId(suggestion.getId());
        response.setFeedbacks(feedbacks.stream()
                .map(this::convertToFeedbackResponse)
                .collect(Collectors.toList()));

        return response;
    }

    private SuggestionResponse.Feedback convertToFeedbackResponse(Feedback feedback) {
        SuggestionResponse.Feedback response = new SuggestionResponse.Feedback();
        response.setContent(feedback.getContent());
        response.setCreateTime(feedback.getGmtCreate());
        response.setAuthor(feedback.getAuthor());
        return response;
    }
} 