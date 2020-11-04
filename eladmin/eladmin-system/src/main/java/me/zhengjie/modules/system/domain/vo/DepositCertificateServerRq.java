package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("存证请求实体类")
public class DepositCertificateServerRq {

    @NotNull(message = "存证ID不能为空")
    @ApiModelProperty("存证ID")
    private Long id;

    @NotNull(message = "存证用户ID不能为空")
    @ApiModelProperty("存证用户ID")
    private Long userId;

    /**
     * 模板id
     */
    @ApiModelProperty("模板ID")
    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    @NotNull(message = "保险单不能为空")
    @ApiModelProperty(value = "保险单")
    private Long policy;

    @NotNull(message = "确认书不能为空")
    @ApiModelProperty(value = "确认书")
    private Long confirmation;

    @NotNull(message = "评估证书不能为空")
    @ApiModelProperty(value = "评估证书")
    private Long evaluationCertificate;


}
