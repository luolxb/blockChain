package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("注册码新增用户请求实体类")
public class UserCodeRq {

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty("手机不能为空")
    public String mobile;
    @NotBlank(message = "用户名不能为空")
    @ApiModelProperty("用户名")
    public String userName;
    @NotBlank(message = "密码不能为空")
    @ApiModelProperty("密码")
    public String password;
    @NotBlank(message = "确认密码不能为空")
    @ApiModelProperty("确认密码")
    public String passwordc;
    @ApiModelProperty("邀请码")
    private String invitationCode;
    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String validCode;

    @Override
    public String toString() {
        return "{username=" + mobile + ", password= ******}";
    }


}
