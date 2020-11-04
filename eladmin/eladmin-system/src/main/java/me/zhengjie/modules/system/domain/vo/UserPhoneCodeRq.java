package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
@ApiModel("短信登录码请求实体类")
public class UserPhoneCodeRq {


    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty("手机不能为空")
    public String mobile;

    @NotBlank(message = "新手机号不能为空")
    @ApiModelProperty("新手机")
    public String newmobile;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String validCode;

    @NotBlank(message = "新验证码不能为空")
    @ApiModelProperty("新验证码")
    private String newvalidCode;

    @Override
    public String toString() {
        return "{username=" + mobile + ", password= ******}";
    }


}
