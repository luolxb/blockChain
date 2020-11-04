package me.zhengjie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum AttachmentTypeEnum {

    /**
     * 附件类型
     */
    ATTACHMENT_TYPE_PIC(1, "图片"),
    ATTACHMENT_TYPE_INVOICE(2, "发票"),
    ATTACHMENT_TYPE_POLICY(3, "保险单"),
    ATTACHMENT_TYPE_CONFIRMATION(4, "确认书"),
    ATTACHMENT_TYPE_EVALUATION_CERTIFICATE(5, "评估证书");


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
