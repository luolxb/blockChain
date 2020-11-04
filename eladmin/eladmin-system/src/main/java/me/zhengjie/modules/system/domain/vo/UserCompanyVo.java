package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

@Data
@ApiModel("用户企业表现实体类")
public class UserCompanyVo extends CompanyVo {

    @ApiModelProperty(value = "用户ID")
    private Long userId;

    @ApiModelProperty(value = "用户名称")
    private String username;

    @ApiModelProperty(value = "用户昵称")
    private String nickName;

    @ApiModelProperty(value = "邮箱")
    private String email;


    @ApiModelProperty(value = "邮箱")
    private String phone;

    @ApiModelProperty(value = "用户性别")
    private String gender;

    @ApiModelProperty(value = "头像真实名称")
    private String avatarName;

    @ApiModelProperty(value = "头像存储的路径")
    private String avatarPath;

    @ApiModelProperty(value = "最后修改密码的时间")
    private Date pwdResetTime;

}
