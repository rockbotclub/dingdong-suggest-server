package cc.rockbot.dds.dto;

import cc.rockbot.dds.enums.SuggestionStatusEnum;
import com.fasterxml.jackson.annotation.JsonProperty;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.Valid;
import lombok.Data;
import lombok.Builder;
import java.time.LocalDateTime;
import java.util.List;
import java.io.Serializable;
/**
 * 建议响应数据传输对象
 * 用于返回建议的详细信息
 *
 * @author rockops
 * @since 1.0
 */
@Data
@Builder
public class CreateSuggestionResponse implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 建议ID
     */
    private Long id;

    /**
     * 建议状态
     * 0: 已提交
     * 1: 已批准
     * 2: 已拒绝
     * 3: 已实施
     * 不能为空，取值范围0-3
     */
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值不能小于0")
    @Max(value = 3, message = "状态值不能大于3")
    private Integer status;

    
}