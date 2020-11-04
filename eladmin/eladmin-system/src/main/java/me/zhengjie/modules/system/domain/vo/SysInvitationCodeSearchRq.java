package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@ApiModel("邀请码搜索实体类")
public class SysInvitationCodeSearchRq {

    @ApiModelProperty("邀请码ID")
    private Long id;

    @ApiModelProperty("当前页")
    private Integer page = 1;

    @ApiModelProperty("每页大小")
    private Integer size = 10;

    @ApiModelProperty("邀请码")
    private String code;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束时间")
    private Long endTime;

    private String startTimeStr;

    private String endTimeStr;

    private String userName;

    private String companyName;


}
