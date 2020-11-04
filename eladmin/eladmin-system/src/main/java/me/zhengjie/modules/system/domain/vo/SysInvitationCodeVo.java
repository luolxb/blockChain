package me.zhengjie.modules.system.domain.vo;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.zhengjie.modules.system.domain.BaseModel;

/**
 * 系统邀请码
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class SysInvitationCodeVo {

    /**
     * 系统邀请码ID
     */
    private Long id;

    /**
     * 系统邀请码
     */
    private String code;

    /**
     * 是否使用 1：没有，2 已使用
     */
    private Integer isUse;

    private String userName;

    private String companyName;


}
