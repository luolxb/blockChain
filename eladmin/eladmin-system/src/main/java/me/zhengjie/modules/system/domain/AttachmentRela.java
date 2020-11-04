package me.zhengjie.modules.system.domain;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 附件关联
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AttachmentRela extends BaseModel {

    private Long id;
    /**
     * 附件ID
     */
    private Long attachmentId;

    /**
     * 附件类型1：图片  2：发票 3. 保险单 4 .确认书  5.评估证书
     */
    private Long attachmentType;

    /**
     * 关联ID
     */
    private Long relaId;

    /**
     * 关联类型
     */
    private String relaType;
}
