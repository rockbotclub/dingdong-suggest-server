package cc.rockbot.dds.enums;

import lombok.Getter;

/**
 * 建议状态枚举
 * 定义了建议的四种状态：已提交、已批准、已拒绝、已实施
 */
@Getter
public enum SuggestionStatusEnum {
    SUBMITTED(0, "已提交"),
    APPROVED(1, "已批准"),
    REJECTED(2, "已拒绝"),
    IMPLEMENTED(3, "已实施");

    private final int code;
    private final String description;

    SuggestionStatusEnum(int code, String description) {
        this.code = code;
        this.description = description;
    }

    /**
     * 根据状态码获取枚举值
     *
     * @param code 状态码
     * @return 对应的枚举值
     * @throws IllegalArgumentException 当状态码无效时抛出
     */
    public static SuggestionStatusEnum fromCode(int code) {
        for (SuggestionStatusEnum status : values()) {
            if (status.getCode() == code) {
                return status;
            }
        }
        throw new IllegalArgumentException("Invalid status code: " + code);
    }

    /**
     * 根据状态码获取描述
     *
     * @param code 状态码
     * @return 状态描述
     */
    public static String getDescriptionByCode(int code) {
        return fromCode(code).getDescription();
    }
} 