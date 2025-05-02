package cc.rockbot.dds.dto;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.Data;


/** 
 * 建议简要信息DTO
 * 
 */
@Data
public class SuggestionLiteDTO implements Serializable {
    private Long id;
    private String title;
    private String status;
    private LocalDateTime createTime;
}
