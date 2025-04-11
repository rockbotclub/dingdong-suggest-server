package cc.rockbot.dds.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import jakarta.validation.Valid;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import java.util.List;

/**
 * 建议响应数据传输对象
 * 用于返回建议的详细信息
 *
 * @author rockops
 * @since 1.0
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class SuggestionResponse extends SuggestionRequest {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 建议ID
     */
    private Long id;

    /**
     * 建议状态
     * 0: 待处理
     * 1: 处理中
     * 2: 已完成
     * 不能为空，取值范围0-2
     */
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不能小于0")
    @Max(value = 2, message = "状态值不能大于2")
    private Integer status;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 问题图片列表
     * 最多3张图片，以JSON格式存储在数据库中
     */
    @Size(max = 3, message = "最多只能上传3张图片")
    @Valid
    @JsonProperty("images")
    private List<ProblemImage> images;
}