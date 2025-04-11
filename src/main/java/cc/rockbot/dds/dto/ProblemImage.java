package cc.rockbot.dds.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import lombok.Data;
import java.io.Serializable;

/**
 * 问题图片数据传输对象
 * 用于存储问题相关的图片信息
 *
 * @author rockops
 * @since 1.0
 */
@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProblemImage implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    /**
     * 图片URL
     * 不能为空，最大长度500字符
     */
    @NotBlank(message = "图片URL不能为空")
    @Size(max = 500, message = "图片URL长度不能超过500个字符")
    private String url;

    /**
     * 图片名称
     * 最大长度255字符
     */
    @Size(max = 255, message = "图片名称长度不能超过255个字符")
    private String name;

    /**
     * 图片大小（字节）
     */
    private Long size;

    /**
     * 图片类型
     * 最大长度50字符
     */
    @Size(max = 50, message = "图片类型长度不能超过50个字符")
    private String contentType;
} 