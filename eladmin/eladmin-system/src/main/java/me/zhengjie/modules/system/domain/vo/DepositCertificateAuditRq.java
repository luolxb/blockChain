package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("存证审核请求实体类")
public class DepositCertificateAuditRq {

    @ApiModelProperty("存证IDS")
    private List<Long> id;

    /**
     * 审核备注
     */
    @ApiModelProperty("审核备注")
    private String auditRemark;


    /**
     * 审核状态 1：上链  2：拒绝
     */
    @ApiModelProperty("审核状态 1：上链  2：拒绝")
    private Integer auditStatus;

}
