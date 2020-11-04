package me.zhengjie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum RelaTypeEnum {

    /**
     * 关联类型
     */
    RELA_TYPE_TEMPLATE("template", "存证模板"),
    RELA_TYPE_CERTIFICATE("certificate", "存证");

    private final String code;
    private final String description;

    public static CodeBiEnum find(Integer code) {
        for (CodeBiEnum value : CodeBiEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return null;
    }
}
