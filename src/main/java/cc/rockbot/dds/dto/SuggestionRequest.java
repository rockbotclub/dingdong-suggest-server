package cc.rockbot.dds.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.Valid;
import lombok.Data;
import java.io.Serializable;
import java.util.List;

/**
 * 建议请求数据传输对象
 * 用于接收创建或更新建议的请求数据
 *
 * @author rockops
 * @since 1.0
 */
@Data
public class SuggestionRequest implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 建议标题
     * 不能为空，最大长度255字符
     */
    @NotBlank(message = "标题不能为空")
    @Size(max = 255, message = "标题长度不能超过255个字符")
    private String title;

    /**
     * 问题描述
     * 不能为空，最大长度1000字符
     */
    @NotBlank(message = "问题描述不能为空")
    @Size(max = 1000, message = "问题描述长度不能超过1000个字符")
    private String problemDescription;

    /**
     * 问题分析
     * 最大长度1000字符
     */
    @Size(max = 1000, message = "问题分析长度不能超过1000个字符")
    private String problemAnalysis;

    /**
     * 建议内容
     * 不能为空，最大长度1000字符
     */
    @NotBlank(message = "建议内容不能为空")
    @Size(max = 1000, message = "建议内容长度不能超过1000个字符")
    private String suggestion;

    /**
     * 预期结果
     * 最大长度1000字符
     */
    @Size(max = 1000, message = "预期结果长度不能超过1000个字符")
    private String expectedOutcome;

    /**
     * 用户微信ID
     * 不能为空，最大长度255字符
     */
    @NotBlank(message = "用户微信ID不能为空")
    @Size(max = 255, message = "用户微信ID长度不能超过255个字符")
    private String userWxid;

    /**
     * 组织ID
     * 不能为空，最大长度255字符
     */
    @NotBlank(message = "组织ID不能为空")
    @Size(max = 255, message = "组织ID长度不能超过255个字符")
    private String orgId;

    /**
     * 问题图片列表
     * 最多3张图片，以JSON格式存储在数据库中
     */
    @Size(max = 3, message = "最多只能上传3张图片")
    @Valid
    @JsonProperty("images")
    private List<ProblemImage> images;
} 