package cc.rockbot.dds.service.impl;

import cc.rockbot.dds.dto.ProblemImage;
import cc.rockbot.dds.dto.SuggestionRequest;
import cc.rockbot.dds.dto.SuggestionResponse;
import cc.rockbot.dds.enums.SuggestionStatusEnum;
import cc.rockbot.dds.model.SuggestionDO;
import cc.rockbot.dds.repository.SuggestionRepository;
import cc.rockbot.dds.service.SuggestionService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestionRepository suggestionRepository;

    @Override
    @Transactional
    public SuggestionResponse createSuggestion(SuggestionRequest request) {
        SuggestionDO suggestion = new SuggestionDO();
        suggestion.setTitle(request.getTitle());
        suggestion.setProblemDescription(request.getProblemDescription());
        suggestion.setProblemAnalysis(request.getProblemAnalysis());
        suggestion.setSuggestion(request.getSuggestion());
        suggestion.setExpectedOutcome(request.getExpectedOutcome());
        suggestion.setStatus(SuggestionStatusEnum.SUBMITTED);
        suggestion.setGmtCreate(LocalDateTime.now());
        suggestion.setGmtModified(LocalDateTime.now());
        suggestion.setUserWxid(request.getUserWxid());
        suggestion.setOrgId(request.getOrgId());
        
        // Add logging and validation for imageUrls
        if (request.getImages() != null) {
            String imageUrlsJson = JSON.toJSONString(request.getImages());
            System.out.println("Debug - Serialized imageUrls: " + imageUrlsJson);
            suggestion.setImageUrls(imageUrlsJson);
        } else {
            suggestion.setImageUrls("[]");
        }

        suggestion = suggestionRepository.save(suggestion);
        return convertToResponse(suggestion);
    }

    @Override
    @Transactional
    public void updateSuggestionStatus(Long suggestionId, String status) {
        SuggestionDO suggestion = suggestionRepository.findById(suggestionId)
                .orElseThrow(() -> new RuntimeException("Suggestion not found"));

        SuggestionStatusEnum newStatus;
        try {
            newStatus = SuggestionStatusEnum.valueOf(status.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Invalid status: " + status);
        }

        // Business rules for status transitions
        if (suggestion.getStatus() == SuggestionStatusEnum.APPROVED && newStatus == SuggestionStatusEnum.WITHDRAWN) {
            throw new RuntimeException("Cannot withdraw an approved suggestion");
        }

        suggestion.setStatus(newStatus);
        suggestion.setGmtModified(LocalDateTime.now());
        suggestionRepository.save(suggestion);
    }

    @Override
    @Transactional(readOnly = true)
    public SuggestionResponse getSuggestionById(Long id) {
        SuggestionDO suggestion = suggestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suggestion not found"));
        return convertToResponse(suggestion);
    }

    @Override
    @Transactional
    public SuggestionResponse updateSuggestion(Long id, SuggestionRequest request) {
        SuggestionDO suggestion = suggestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suggestion not found"));

        suggestion.setTitle(request.getTitle());
        suggestion.setProblemDescription(request.getProblemDescription());
        suggestion.setProblemAnalysis(request.getProblemAnalysis());
        suggestion.setSuggestion(request.getSuggestion());
        suggestion.setExpectedOutcome(request.getExpectedOutcome());
        suggestion.setOrgId(request.getOrgId());
        suggestion.setGmtModified(LocalDateTime.now());
        
        // Add logging and validation for imageUrls
        if (request.getImages() != null) {
            String imageUrlsJson = JSON.toJSONString(request.getImages());
            System.out.println("Debug - Serialized imageUrls: " + imageUrlsJson);
            suggestion.setImageUrls(imageUrlsJson);
        } else {
            suggestion.setImageUrls("[]");
        }

        suggestion = suggestionRepository.save(suggestion);
        return convertToResponse(suggestion);
    }

    @Override
    @Transactional
    public void deleteSuggestion(Long id) {
        if (!suggestionRepository.existsById(id)) {
            throw new RuntimeException("Suggestion not found");
        }
        suggestionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<SuggestionResponse> getAllSuggestions() {
        return suggestionRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    private SuggestionResponse convertToResponse(SuggestionDO suggestion) {
        SuggestionResponse response = new SuggestionResponse();
        response.setId(suggestion.getId());
        response.setTitle(suggestion.getTitle());
        response.setProblemDescription(suggestion.getProblemDescription());
        response.setProblemAnalysis(suggestion.getProblemAnalysis());
        response.setSuggestion(suggestion.getSuggestion());
        response.setExpectedOutcome(suggestion.getExpectedOutcome());
        response.setStatus(suggestion.getStatus().getCode());
        response.setStatusDescription(suggestion.getStatus().getDescription());
        response.setCreateTime(suggestion.getGmtCreate());
        response.setUpdateTime(suggestion.getGmtModified());
        response.setUserWxid(suggestion.getUserWxid());
        response.setOrgId(suggestion.getOrgId());
        
        // Add logging to debug the imageUrls value
        System.out.println("Debug - imageUrls value: " + suggestion.getImageUrls());
        
        // Add null check and empty string check
        if (suggestion.getImageUrls() != null && !suggestion.getImageUrls().trim().isEmpty()) {
            try {
                response.setImages(JSON.parseArray(suggestion.getImageUrls(), ProblemImage.class));
            } catch (Exception e) {
                System.err.println("Error parsing imageUrls: " + e.getMessage());
                response.setImages(Collections.emptyList());
            }
        } else {
            response.setImages(Collections.emptyList());
        }
        return response;
    }
} 