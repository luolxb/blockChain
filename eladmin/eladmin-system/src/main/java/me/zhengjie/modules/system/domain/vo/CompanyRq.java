package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("企业请求类")
public class CompanyRq {

    @ApiModelProperty(value = "商户ID")
    private Long id;

    @NotBlank(message = "企业类型不能为空")
    @ApiModelProperty(value = "企业类型")
    private String companyType;

    @NotBlank(message = "企业名称不能为空")
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @NotBlank(message = "法人不能为空")
    @ApiModelProperty(value = "法人")
    private String corporate;

    @NotNull(message = "公司LOGO不能为空")
    @ApiModelProperty(value = "公司LOGO")
    private Long logo;

    @NotNull(message = "公司营业执照不能为空")
    @ApiModelProperty(value = "公司营业执照")
    private Long businessLicense;

    @NotBlank(message = "社会信用代码不能为空")
    @ApiModelProperty(value = "社会信用代码")
    private String socialCreditCode;

    @NotBlank(message = "地址不能为空")
    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "公司简介")
    private String profile;

    @NotBlank(message = "联系电话不能为空")
    @ApiModelProperty(value = "联系电话")
    private String companyPhone;

    @NotBlank(message = "钱包地址不能为空")
    @ApiModelProperty(value = "钱包地址")
    private String walletAddress;

    @ApiModelProperty(value = "状态 1：启用  2：禁用")
    private Integer status;

    @ApiModelProperty(value = "审核状态 1：待审核 2：已审核,3:不通过")
    private Integer auditStatus;

    @ApiModelProperty(value = "开始时间/时间戳")
    private Long startTime;

    @ApiModelProperty(value = "结束时间/时间戳")
    private Long endTime;

}
