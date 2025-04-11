package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.SuggestionRequest;
import cc.rockbot.dds.dto.SuggestionResponse;

import java.util.List;

/**
 * 建议服务接口
 * 提供建议的创建、查询、更新和删除等核心业务功能
 *
 * @author rockbot
 * @since 1.0
 */
public interface SuggestionService {
    /**
     * 创建新的建议
     *
     * @param request 建议请求对象，包含建议的详细信息
     * @return 创建后的建议响应对象
     * @throws RuntimeException 当创建建议失败时抛出
     */
    SuggestionResponse createSuggestion(SuggestionRequest request);

    /**
     * 更新建议状态
     *
     * @param suggestionId 建议ID
     * @param status 新的状态值
     * @throws RuntimeException 当建议不存在或更新失败时抛出
     */
    void updateSuggestionStatus(Long suggestionId, String status);

    /**
     * 根据ID获取建议
     *
     * @param id 建议ID
     * @return 建议响应对象
     * @throws RuntimeException 当建议不存在时抛出
     */
    SuggestionResponse getSuggestionById(Long id);

    /**
     * 更新建议信息
     *
     * @param id 建议ID
     * @param request 建议请求对象，包含要更新的信息
     * @return 更新后的建议响应对象
     * @throws RuntimeException 当建议不存在或更新失败时抛出
     */
    SuggestionResponse updateSuggestion(Long id, SuggestionRequest request);

    /**
     * 删除建议
     *
     * @param id 建议ID
     * @throws RuntimeException 当建议不存在或删除失败时抛出
     */
    void deleteSuggestion(Long id);

    /**
     * 获取所有建议
     *
     * @return 建议响应对象列表
     */
    List<SuggestionResponse> getAllSuggestions();
} 