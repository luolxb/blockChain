/*
 *  Copyright 2019-2020 Zheng Jie
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *  http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码的 Vo 类
 *
 * @author Zheng Jie
 * @date 2019年7月11日13:59:49
 */
@Data
public class UserPassVo {

    @NotBlank(message = "手机号不能为空")
    @ApiModelProperty("手机不能为空")
    public String mobile;

//    @NotBlank(message = "旧密码不能为空")
//    @ApiModelProperty("旧密码")
//    private String oldPass;

    @NotBlank(message = "新密码不能为空")
    @ApiModelProperty("新密码")
    private String newPass;

    @NotBlank(message = "验证码不能为空")
    @ApiModelProperty("验证码")
    private String validCode;


}
