package cc.rockbot.dds.service.impl;

import cc.rockbot.dds.dto.ProblemImage;
import cc.rockbot.dds.dto.CreateSuggestionRequest;
import cc.rockbot.dds.enums.SuggestionStatusEnum;
import cc.rockbot.dds.model.SuggestionDO;
import cc.rockbot.dds.repository.SuggestionRepository;
import cc.rockbot.dds.service.SuggestionService;
import com.alibaba.fastjson.JSON;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.apache.commons.lang3.StringUtils;
import cc.rockbot.dds.dto.SuggestionLiteDTO;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class SuggestionServiceImpl implements SuggestionService {

    private final SuggestionRepository suggestionRepository;


    @Override
    @Transactional
    public SuggestionDO createSuggestion(SuggestionDO suggestionDO) {
        
        suggestionDO = suggestionRepository.save(suggestionDO);
        return suggestionDO;
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
    public SuggestionDO getSuggestionById(Long id) {
        SuggestionDO suggestion = suggestionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Suggestion not found"));
        return suggestion;
    }

    @Override
    @Transactional
    public SuggestionDO updateSuggestion(Long id, CreateSuggestionRequest request) {
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
        return suggestion;
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
    public Page<SuggestionLiteDTO> getAllSuggestions(String wxid, String orgId, int year, Pageable pageable) {
        if (StringUtils.isBlank(wxid)) {
            throw new RuntimeException("Invalid wxid");
        }
        
        Page<SuggestionDO> suggestions = suggestionRepository.findByUserWxidAndOrgIdAndYear(
            wxid, orgId, year, pageable
        );
        
        return suggestions.map(this::convertToLiteDTO);
    }

    private SuggestionLiteDTO convertToLiteDTO(SuggestionDO suggestion) {
        SuggestionLiteDTO dto = new SuggestionLiteDTO();
        dto.setId(suggestion.getId());
        dto.setTitle(suggestion.getTitle());
        dto.setStatus(suggestion.getStatus().name());
        dto.setCreateTime(suggestion.getGmtCreate());
        return dto;
    }

} 