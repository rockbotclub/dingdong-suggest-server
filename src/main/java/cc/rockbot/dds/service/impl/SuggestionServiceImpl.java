package cc.rockbot.dds.service.impl;


import cc.rockbot.dds.enums.SuggestionStatusEnum;
import cc.rockbot.dds.model.SuggestionDO;
import cc.rockbot.dds.repository.SuggestionRepository;
import cc.rockbot.dds.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.apache.commons.lang3.StringUtils;
import cc.rockbot.dds.dto.SuggestionLiteDTO;
import java.time.LocalDateTime;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import java.util.Objects;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;

@Slf4j
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
    public void withdrawSuggestion(Long id, String wxid) {
        SuggestionDO suggestion = suggestionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));
        if (!Objects.equals(suggestion.getUserWxid(), wxid)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "不允许撤回其他人的建议");
        }
        if (suggestion.getStatus() != SuggestionStatusEnum.SUBMITTED) {
            throw new BusinessException(ErrorCode.SUGGESTION_STATUS_INVALID, "只有处于未审批状态的建议才能撤回");
        }

        suggestion.setStatus(SuggestionStatusEnum.WITHDRAWN);
        suggestion.setGmtModified(LocalDateTime.now());
        suggestionRepository.save(suggestion);
    }

    @Override
    @Transactional(readOnly = true)
    public SuggestionDO getSuggestionById(Long id, String wxid) {
        SuggestionDO suggestion = suggestionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));
        if (!Objects.equals(suggestion.getUserWxid(), wxid)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "不允许查看其他人的建议");
        }
        return suggestion;
    }

    @Override
    @Transactional
    public void deleteSuggestion(Long id, String wxid) {
        SuggestionDO suggestion = suggestionRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.SUGGESTION_NOT_FOUND));
        if (!Objects.equals(suggestion.getUserWxid(), wxid)) {
            throw new BusinessException(ErrorCode.FORBIDDEN, "不允许删除其他人的建议");
        }
        // 只有处于已撤回的建议才能删除
        if (suggestion.getStatus() != SuggestionStatusEnum.WITHDRAWN) {
            throw new BusinessException(ErrorCode.SUGGESTION_STATUS_INVALID, "只有处于已撤回的建议才能删除");
        }
        suggestionRepository.deleteById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<SuggestionLiteDTO> getAllSuggestions(String wxid, String orgId, int year, Pageable pageable) {
        if (StringUtils.isBlank(wxid)) {
            throw new BusinessException(ErrorCode.PARAM_ERROR, "Invalid wxid");
        }
        
        log.info("Searching suggestions with params - wxid: {}, orgId: {}, year: {}, page: {}, size: {}", 
            wxid, orgId, year, pageable.getPageNumber(), pageable.getPageSize());
        
        Page<SuggestionDO> suggestions = suggestionRepository.findByUserWxidAndOrgIdAndYear(
            wxid, orgId, year, pageable
        );
        
        log.info("Found {} suggestions", suggestions.getTotalElements());
        if (suggestions.getTotalElements() == 0) {
            // 检查是否有任何数据
            Page<SuggestionDO> allSuggestions = suggestionRepository.findAll(pageable);
            log.info("Total suggestions in database: {}", allSuggestions.getTotalElements());
            
            // 检查特定用户的所有建议
            List<SuggestionDO> userSuggestions = suggestionRepository.findByUserWxid(wxid);
            log.info("Total suggestions for user {}: {}", wxid, userSuggestions.size());
            
            // 检查特定组织的所有建议
            Page<SuggestionDO> orgSuggestions = suggestionRepository.findByOrgId(orgId, pageable);
            log.info("Total suggestions for org {}: {}", orgId, orgSuggestions.getTotalElements());
        }
        
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