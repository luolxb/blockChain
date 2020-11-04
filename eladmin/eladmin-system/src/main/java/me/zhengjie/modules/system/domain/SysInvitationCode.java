package me.zhengjie.modules.system.domain;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

/**
 * 系统邀请码
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("sys_invitation_code")
public class SysInvitationCode extends BaseModel {

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

}
