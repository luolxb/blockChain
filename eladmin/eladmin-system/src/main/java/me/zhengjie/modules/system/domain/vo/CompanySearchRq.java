package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("企业搜索请求类")
public class CompanySearchRq {


    @ApiModelProperty(value = "企业ID", hidden = true)
    private Long id;

    @ApiModelProperty(value = "企业类型")
    private String companyType;


    @ApiModelProperty(value = "用户名称")
    private String userName;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "审核状态 1：待审核，2：已审核")
    private Integer auditStatus;

    @ApiModelProperty(value = "开始时间/时间戳")
    private Long startTime;

    @ApiModelProperty(value = "结束时间/时间戳")
    private Long endTime;

    private String startTimeStr;

    private String endTimeStr;

    private Integer page = 1;

    private Integer size = 10;


}
