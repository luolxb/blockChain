package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.zhengjie.domain.LocalStorage;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("企业表现实体类Vo")
public class CompanyVo {

    @ApiModelProperty(value = "企业ID")
    private Long id;

    @ApiModelProperty(value = "企业类型")
    private String companyType;

    @ApiModelProperty(value = "企业类型名称")
    private String companyTypeName;

    @ApiModelProperty(value = "品牌名称")
    private String brandName;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "法人")
    private String corporate;

    @ApiModelProperty(value = "公司LOGO")
    private LocalStorage logoPic;

    @ApiModelProperty(value = "公司营业执照")
    private Long businessLicense;

    @ApiModelProperty(value = "公司LOGO")
    private Long logo;

    @ApiModelProperty(value = "公司营业执照")
    private LocalStorage businessLicensePic;

    @ApiModelProperty(value = "社会信用代码")
    private String socialCreditCode;

    @ApiModelProperty(value = "地址")
    private String address;

    @ApiModelProperty(value = "公司简介")
    private String profile;

    @ApiModelProperty(value = "联系电话")
    private String companyPhone;

    @ApiModelProperty(value = "钱包地址")
    private String walletAddress;

    @ApiModelProperty(value = "状态 1：启用  2：禁用")
    private Integer status;

    @ApiModelProperty(value = "审核状态 1：待审核 ，2 ：已审核")
    private Integer auditStatus;
    private String auditStatusStr;

    @ApiModelProperty("私钥")
    private String privateKey;


    private String code;

    @ApiModelProperty("私钥")
    private Date createTime;


}
