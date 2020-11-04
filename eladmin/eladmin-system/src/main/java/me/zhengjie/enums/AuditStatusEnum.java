package me.zhengjie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AuditStatusEnum {

    /**
     * 审核状态 1：上链  2：拒绝
     */
    AUDIT_STATUS_AGREE(1, "上链"),
    AUDIT_STATUS_REFUSE(2, "决绝");


    private final Integer code;
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
