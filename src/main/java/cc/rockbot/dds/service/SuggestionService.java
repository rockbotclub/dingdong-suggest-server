package cc.rockbot.dds.service;

import cc.rockbot.dds.dto.CreateSuggestionRequest;
import cc.rockbot.dds.dto.SuggestionLiteDTO;
import cc.rockbot.dds.model.SuggestionDO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    SuggestionDO createSuggestion(SuggestionDO suggestionDO);


    /**
     * 撤回建议
     *
     * @param id 建议ID
     * @param wxid 用户微信ID
     * @throws RuntimeException 当建议不存在或撤回失败时抛出
     */
    void withdrawSuggestion(Long id, String wxid);

    /**
     * 根据ID获取建议
     *
     * @param id 建议ID
     * @param wxid 用户微信ID
     * @return 建议响应对象
     * @throws RuntimeException 当建议不存在时抛出
     */
    SuggestionDO getSuggestionById(Long id, String wxid);

    /**
     * 删除建议
     *
     * @param id 建议ID
     * @throws RuntimeException 当建议不存在或删除失败时抛出
     */
    void deleteSuggestion(Long id, String wxid);

    /**
     * 获取用户的建议列表
     * @param wxid 用户微信ID
     * @param orgId 组织ID
     * @param year 年份
     * @param pageable 分页参数
     * @return 建议列表
     */
    Page<SuggestionLiteDTO> getAllSuggestions(String wxid, String orgId, int year, Pageable pageable);
} 