package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("企业审核请求类")
public class CompanyAuditRq {

    @ApiModelProperty("企业ID")
    private List<Long> ids;

    @ApiModelProperty(value = "审核状态 1：待审核 ，2 ：已审核， 3：不通过")
    private Integer auditStatus;


}
