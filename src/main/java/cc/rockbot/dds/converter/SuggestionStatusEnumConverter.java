package cc.rockbot.dds.converter;

import cc.rockbot.dds.enums.SuggestionStatusEnum;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

/**
 * 建议状态枚举转换器
 * 用于在数据库和Java对象之间转换建议状态
 */
@Converter(autoApply = true)
public class SuggestionStatusEnumConverter implements AttributeConverter<SuggestionStatusEnum, Integer> {

    @Override
    public Integer convertToDatabaseColumn(SuggestionStatusEnum status) {
        if (status == null) {
            return null;
        }
        return status.getCode();
    }

    @Override
    public SuggestionStatusEnum convertToEntityAttribute(Integer code) {
        if (code == null) {
            return null;
        }
        return SuggestionStatusEnum.fromCode(code);
    }
} 