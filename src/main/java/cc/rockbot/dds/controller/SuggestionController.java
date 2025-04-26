package cc.rockbot.dds.controller;

import cc.rockbot.dds.dto.CreateSuggestionRequest;
import cc.rockbot.dds.dto.SuggestionLiteDTO;
import cc.rockbot.dds.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import cc.rockbot.dds.dto.ApiResponse;
import cc.rockbot.dds.exception.BusinessException;
import cc.rockbot.dds.exception.ErrorCode;
import cc.rockbot.dds.model.SuggestionDO;
import lombok.extern.slf4j.Slf4j;
import java.util.List;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.apache.commons.lang3.StringUtils;
import cc.rockbot.dds.util.JwtTokenService;
import java.time.LocalDateTime;
import cc.rockbot.dds.enums.SuggestionStatusEnum;
import com.alibaba.fastjson.JSON;

@Slf4j
@RestController
@RequestMapping("/api/v1/suggestions")
@Tag(name = "建议管理", description = "建议相关的接口")
@RequiredArgsConstructor
public class SuggestionController extends BaseController {

    private final SuggestionService suggestionService;

    private final JwtTokenService jwtTokenService;

    @PostMapping
    public ApiResponse<SuggestionDO> createSuggestion(@RequestBody CreateSuggestionRequest request) {
        try {
            if (request == null) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "建议信息不能为空");
            }
            String wxid = jwtTokenService.getWxidFromToken(request.getJwtToken());
            // 检查jwt token是否有效
            if (StringUtils.isBlank(wxid)) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "jwt token无效");
            }
            SuggestionDO suggestionDO = constructSuggestionDO(request, wxid);
            return ApiResponse.success(suggestionService.createSuggestion(suggestionDO));
        } catch (BusinessException e) {
            log.warn("创建建议业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("创建建议系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    private SuggestionDO constructSuggestionDO(CreateSuggestionRequest request, String wxid) {
        SuggestionDO suggestionDO = new SuggestionDO();
        suggestionDO.setTitle(request.getTitle());
        suggestionDO.setProblemDescription(request.getProblemDescription());
        suggestionDO.setProblemAnalysis(request.getProblemAnalysis());
        suggestionDO.setSuggestion(request.getSuggestion());
        suggestionDO.setExpectedOutcome(request.getExpectedOutcome());
        suggestionDO.setUserWxid(wxid);
        suggestionDO.setOrgId(request.getOrgId());
        suggestionDO.setStatus(SuggestionStatusEnum.SUBMITTED);
        suggestionDO.setGmtCreate(LocalDateTime.now());
        suggestionDO.setGmtModified(LocalDateTime.now());
        if (request.getImages() != null) {
            String imageUrlsJson = JSON.toJSONString(request.getImages());
            System.out.println("Debug - Serialized imageUrls: " + imageUrlsJson);
            suggestionDO.setImageUrls(imageUrlsJson);
        } else {
            suggestionDO.setImageUrls("[]");
        }
        return suggestionDO;
    }

    @GetMapping("/{id}")
    public ApiResponse<SuggestionDO> getSuggestion(@PathVariable Long id) {
        try {
            if (id == null) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "建议ID不能为空");
            }
            return ApiResponse.success(suggestionService.getSuggestionById(id));
        } catch (BusinessException e) {
            log.warn("获取建议业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("获取建议系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    /**
     * 根据JWT token获取用户的所有建议列表
     * 
     * @param jwtToken JWT token
     * @param orgId 组织ID
     * @param year 年份
     * @return 建议列表
     */
    @GetMapping
    public ApiResponse<List<SuggestionLiteDTO>> getAllSuggestions(@RequestParam String jwtToken
                                                                , @RequestParam String orgId
                                                                , @RequestParam String year) {
        try {
            if (jwtToken == null || jwtToken.isEmpty() || orgId == null || orgId.isEmpty() || year == null || year.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "JWT token、组织ID和年份不能为空");
            }
            return ApiResponse.success(suggestionService.getAllSuggestions(jwtToken, orgId, year));
        } catch (BusinessException e) {
            log.warn("获取建议列表业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("获取建议列表系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    @PutMapping("/{id}/status")
    public ApiResponse<Void> updateStatus(@PathVariable Long id, @RequestParam String status) {
        try {
            if (id == null || status == null || status.isEmpty()) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "建议ID和状态不能为空");
            }
            suggestionService.updateSuggestionStatus(id, status);
            return ApiResponse.success(null);
        } catch (BusinessException e) {
            log.warn("更新建议状态业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("更新建议状态系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }

    @PutMapping("/{id}/withdraw")
    public ApiResponse<Void> withdrawSuggestion(@PathVariable Long id) {
        try {
            if (id == null) {
                throw new BusinessException(ErrorCode.PARAM_ERROR, "建议ID不能为空");
            }
            suggestionService.updateSuggestionStatus(id, "WITHDRAWN");
            return ApiResponse.success(null);
        } catch (BusinessException e) {
            log.warn("撤回建议业务异常: {}", e.getMessage());
            return ApiResponse.error(e.getErrorCode().getCode(), e.getDetailMessage());
        } catch (Exception e) {
            log.error("撤回建议系统异常", e);
            return ApiResponse.error(ErrorCode.SYSTEM_ERROR.getCode(), "系统异常，请稍后重试");
        }
    }
} 