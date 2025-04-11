package cc.rockbot.dds.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.util.List;

@Data
public class SuggestionRequest {
    @NotBlank(message = "建议标题不能为空")
    @Size(max = 255, message = "建议标题不能超过255个字符")
    private String title;

    @NotBlank(message = "问题描述不能为空")
    private String problem;

    private String analysis;

    @NotNull(message = "具体建议不能为空")
    @Size(min = 1, message = "至少需要一条具体建议")
    private List<String> suggestions;

    private String expectedEffect;

    private List<String> images;

    @NotBlank(message = "提交人姓名不能为空")
    private String submitterName;

    @NotBlank(message = "提交人部门不能为空")
    private String submitterDepartment;

    @NotBlank(message = "提交人电话不能为空")
    private String submitterPhone;

    @NotBlank(message = "提交人微信ID不能为空")
    private String submitterWxid;

    @NotBlank(message = "组织ID不能为空")
    private String organizationId;

    @NotBlank(message = "组织名称不能为空")
    private String organizationName;
} 