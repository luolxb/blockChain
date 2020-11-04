package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("存证搜索请求类")
public class DepositCertificateSearchRq {

    @ApiModelProperty(hidden = true)
    private Long id;

    /**
     * 存证名称
     */
    @ApiModelProperty("存证名称")
    private String certificateName;

    /**
     * 上链转态1:未上链  2：已上链 3：上链中 4：上链失败
     */
    @ApiModelProperty("上链转态1:未上链  2：已上链")
    private String onChainStatus;

    private Integer onChainStatus3;

    private Integer onChainStatus4;

    /**
     * 企业名称
     */
    @ApiModelProperty("企业名称")
    private String companyName;

    @ApiModelProperty("开始时间")
    private Long startTime;

    @ApiModelProperty("结束时间")
    private Long endTime;


    private String startTimeStr;

    private String endTimeStr;

    private Integer page = 1;

    private Integer size = 10;


}
