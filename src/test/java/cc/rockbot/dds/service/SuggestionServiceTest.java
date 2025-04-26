package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.ProblemImage;
import cc.rockbot.dds.dto.SuggestionRequest;
import cc.rockbot.dds.dto.SuggestionResponse;
import cc.rockbot.dds.enums.SuggestionStatusEnum;
import cc.rockbot.dds.model.SuggestionDO;
import cc.rockbot.dds.repository.SuggestionRepository;
import cc.rockbot.dds.service.impl.SuggestionServiceImpl;
import com.alibaba.fastjson.JSON;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import cc.rockbot.dds.dto.SuggestionLiteDTO;

/**
 * 建议服务测试类
 * 用于测试SuggestionService的各种业务逻辑，包括创建、查询、更新和删除建议等功能
 */
@ExtendWith(MockitoExtension.class)
class SuggestionServiceTest {

    @Mock
    private SuggestionRepository suggestionRepository;

    @InjectMocks
    private SuggestionServiceImpl suggestionService;

    private SuggestionDO suggestion;
    private SuggestionRequest request;
    private ProblemImage problemImage;
    private String imageUrlsJson;

    /**
     * 测试初始化方法
     * 在每个测试方法执行前设置测试数据
     */
    @BeforeEach
    void setUp() {
        // 初始化问题图片对象
        problemImage = new ProblemImage();
        problemImage.setUrl("https://example.com/image.jpg");
        problemImage.setName("test-image.jpg");

        // 初始化图片URL JSON
        List<ProblemImage> images = Collections.singletonList(problemImage);
        imageUrlsJson = JSON.toJSONString(images);

        // 初始化建议实体对象
        suggestion = new SuggestionDO();
        suggestion.setId(1L);
        suggestion.setTitle("Test Title");
        suggestion.setProblemDescription("Test Problem Description");
        suggestion.setProblemAnalysis("Test Problem Analysis");
        suggestion.setSuggestion("Test Suggestion");
        suggestion.setExpectedOutcome("Test Expected Outcome");
        suggestion.setStatus(SuggestionStatusEnum.SUBMITTED);
        suggestion.setGmtCreate(LocalDateTime.now());
        suggestion.setGmtModified(LocalDateTime.now());
        suggestion.setUserWxid("test_wxid");
        suggestion.setOrgId("test_org");
        suggestion.setImageUrls(imageUrlsJson);

        // 初始化建议请求对象
        request = new SuggestionRequest();
        request.setTitle("Test Title");
        request.setProblemDescription("Test Problem Description");
        request.setProblemAnalysis("Test Problem Analysis");
        request.setSuggestion("Test Suggestion");
        request.setExpectedOutcome("Test Expected Outcome");
        request.setUserWxid("test_wxid");
        request.setOrgId("test_org");
        request.setImages(images);
    }

    /**
     * 测试创建建议功能
     * 验证创建建议时是否正确保存并返回建议对象
     */
    @Test
    void createSuggestion_ShouldCreateNewSuggestion() {
        when(suggestionRepository.save(any(SuggestionDO.class))).thenReturn(suggestion);

        SuggestionResponse response = suggestionService.createSuggestion(request);

        assertNotNull(response);
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Problem Description", response.getProblemDescription());
        assertEquals("Test Problem Analysis", response.getProblemAnalysis());
        assertEquals("Test Suggestion", response.getSuggestion());
        assertEquals("Test Expected Outcome", response.getExpectedOutcome());
        assertEquals(SuggestionStatusEnum.SUBMITTED.getCode(), response.getStatus());
        assertEquals(SuggestionStatusEnum.SUBMITTED.getDescription(), response.getStatusDescription());
        assertEquals("test_wxid", response.getUserWxid());
        assertEquals("test_org", response.getOrgId());
        assertNotNull(response.getImages());
        assertEquals(1, response.getImages().size());
        assertEquals("https://example.com/image.jpg", response.getImages().get(0).getUrl());
        verify(suggestionRepository).save(any(SuggestionDO.class));
    }

    /**
     * 测试根据ID获取建议功能
     * 验证通过ID查询建议时是否正确返回建议对象
     */
    @Test
    void getSuggestionById_ShouldReturnSuggestion() {
        when(suggestionRepository.findById(1L)).thenReturn(java.util.Optional.of(suggestion));

        SuggestionResponse response = suggestionService.getSuggestionById(1L);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Problem Description", response.getProblemDescription());
        assertEquals("Test Problem Analysis", response.getProblemAnalysis());
        assertEquals("Test Suggestion", response.getSuggestion());
        assertEquals("Test Expected Outcome", response.getExpectedOutcome());
        assertEquals(SuggestionStatusEnum.SUBMITTED.getCode(), response.getStatus());
        assertEquals(SuggestionStatusEnum.SUBMITTED.getDescription(), response.getStatusDescription());
        assertEquals("test_wxid", response.getUserWxid());
        assertEquals("test_org", response.getOrgId());
        assertNotNull(response.getImages());
        assertEquals(1, response.getImages().size());
        assertEquals("https://example.com/image.jpg", response.getImages().get(0).getUrl());
    }

    /**
     * 测试获取所有建议功能
     * 验证获取所有建议时是否正确返回建议列表
     */
    @Test
    void getAllSuggestions_ShouldReturnAllSuggestions() {
        List<SuggestionDO> suggestions = Arrays.asList(suggestion);
        when(suggestionRepository.findByUserWxidAndOrgIdAndYear(anyString(), anyString(), anyString()))
            .thenReturn(suggestions);

        List<SuggestionLiteDTO> responses = suggestionService.getAllSuggestions(
            "test_jwt_token", 
            "test_org", 
            "2024"
        );

        assertNotNull(responses);
        assertEquals(1, responses.size());
        SuggestionLiteDTO response = responses.get(0);
        assertEquals(1L, response.getId());
        assertEquals("Test Title", response.getTitle());
        assertEquals(SuggestionStatusEnum.SUBMITTED.getCode(), response.getStatus());
        assertNotNull(response.getCreateTime());
    }

    /**
     * 测试更新建议功能
     * 验证更新建议时是否正确更新并返回更新后的建议对象
     */
    @Test
    void updateSuggestion_ShouldUpdateSuggestion() {
        when(suggestionRepository.findById(1L)).thenReturn(java.util.Optional.of(suggestion));
        when(suggestionRepository.save(any(SuggestionDO.class))).thenReturn(suggestion);

        SuggestionResponse response = suggestionService.updateSuggestion(1L, request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals("Test Title", response.getTitle());
        assertEquals("Test Problem Description", response.getProblemDescription());
        assertEquals("Test Problem Analysis", response.getProblemAnalysis());
        assertEquals("Test Suggestion", response.getSuggestion());
        assertEquals("Test Expected Outcome", response.getExpectedOutcome());
        assertEquals(SuggestionStatusEnum.SUBMITTED.getCode(), response.getStatus());
        assertEquals(SuggestionStatusEnum.SUBMITTED.getDescription(), response.getStatusDescription());
        assertEquals("test_wxid", response.getUserWxid());
        assertEquals("test_org", response.getOrgId());
        assertNotNull(response.getImages());
        assertEquals(1, response.getImages().size());
        assertEquals("https://example.com/image.jpg", response.getImages().get(0).getUrl());
        verify(suggestionRepository).save(any(SuggestionDO.class));
    }

    /**
     * 测试删除建议功能
     * 验证删除建议时是否正确执行删除操作
     */
    @Test
    void deleteSuggestion_ShouldDeleteSuggestion() {
        when(suggestionRepository.existsById(1L)).thenReturn(true);
        doNothing().when(suggestionRepository).deleteById(1L);

        suggestionService.deleteSuggestion(1L);

        verify(suggestionRepository).deleteById(1L);
    }

    /**
     * 测试获取不存在的建议时抛出异常
     * 验证当建议不存在时是否正确抛出异常
     */
    @Test
    void getSuggestionById_WhenSuggestionNotFound_ShouldThrowException() {
        when(suggestionRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> suggestionService.getSuggestionById(1L));
    }

    /**
     * 测试更新不存在的建议时抛出异常
     * 验证当更新不存在的建议时是否正确抛出异常
     */
    @Test
    void updateSuggestion_WhenSuggestionNotFound_ShouldThrowException() {
        when(suggestionRepository.findById(1L)).thenReturn(java.util.Optional.empty());

        assertThrows(RuntimeException.class, () -> suggestionService.updateSuggestion(1L, request));
        verify(suggestionRepository, never()).save(any(SuggestionDO.class));
    }

    /**
     * 测试删除不存在的建议时抛出异常
     * 验证当删除不存在的建议时是否正确抛出异常
     */
    @Test
    void deleteSuggestion_WhenSuggestionNotFound_ShouldThrowException() {
        when(suggestionRepository.existsById(1L)).thenReturn(false);

        assertThrows(RuntimeException.class, () -> suggestionService.deleteSuggestion(1L));
        verify(suggestionRepository, never()).deleteById(1L);
    }

    /**
     * 测试更新建议状态功能
     * 验证更新建议状态时是否正确更新
     */
    @Test
    void updateSuggestionStatus_ShouldUpdateStatus() {
        when(suggestionRepository.findById(1L)).thenReturn(java.util.Optional.of(suggestion));
        when(suggestionRepository.save(any(SuggestionDO.class))).thenReturn(suggestion);

        suggestionService.updateSuggestionStatus(1L, "APPROVED");

        verify(suggestionRepository).save(any(SuggestionDO.class));
    }

    /**
     * 测试使用无效状态更新建议状态时抛出异常
     * 验证当使用无效状态更新时是否正确抛出异常
     */
    @Test
    void updateSuggestionStatus_WithInvalidStatus_ShouldThrowException() {
        when(suggestionRepository.findById(1L)).thenReturn(java.util.Optional.of(suggestion));

        assertThrows(RuntimeException.class, () -> suggestionService.updateSuggestionStatus(1L, "INVALID_STATUS"));
        verify(suggestionRepository, never()).save(any(SuggestionDO.class));
    }

    @Test
    void updateSuggestionStatus_ToWithdrawn_ShouldUpdateStatus() {
        when(suggestionRepository.findById(1L)).thenReturn(java.util.Optional.of(suggestion));
        when(suggestionRepository.save(any(SuggestionDO.class))).thenReturn(suggestion);

        suggestionService.updateSuggestionStatus(1L, "WITHDRAWN");

        verify(suggestionRepository).save(any(SuggestionDO.class));
    }

    @Test
    void updateSuggestionStatus_ToWithdrawn_WhenAlreadyApproved_ShouldThrowException() {
        suggestion.setStatus(SuggestionStatusEnum.APPROVED);
        when(suggestionRepository.findById(1L)).thenReturn(java.util.Optional.of(suggestion));

        assertThrows(RuntimeException.class, () -> suggestionService.updateSuggestionStatus(1L, "WITHDRAWN"));
        verify(suggestionRepository, never()).save(any(SuggestionDO.class));
    }
} 