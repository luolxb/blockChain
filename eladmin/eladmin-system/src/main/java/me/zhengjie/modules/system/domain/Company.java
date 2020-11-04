package me.zhengjie.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import java.util.Date;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@TableName("company")
public class Company extends BaseModel {

    private Long id;

    @NotNull
    @ApiModelProperty(value = "企业类型")
    private String companyType;

    @NotNull
    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @NotNull
    @ApiModelProperty(value = "法人")
    private String corporate;

    @NotNull
    @ApiModelProperty(value = "公司LOGO")
    private Long logo;

    @NotNull
    @ApiModelProperty(value = "公司营业执照")
    private Long businessLicense;

    @NotNull
    @ApiModelProperty(value = "社会信用代码")
    private String socialCreditCode;

    @NotNull
    @ApiModelProperty(value = "地址")
    private String address;

    @NotNull
    @ApiModelProperty(value = "公司简介")
    private String profile;

    @NotNull
    @ApiModelProperty(value = "联系电话")
    private String companyPhone;

    @NotNull
    @ApiModelProperty(value = "钱包地址")
    private String walletAddress;

    @ApiModelProperty(value = "状态 1：启用  2：禁用")
    private Integer status;

    @ApiModelProperty(value = "审核状态 1：待审核 ，2 ：已审核")
    private Integer auditStatus;

    /**
     * 审核时间
     */
    @ApiModelProperty(value = "审核时间")
    private Date auditTime;

    @ApiModelProperty("私钥")
    private String privateKey;

    @ApiModelProperty("审核人")
    private String auditBy;

    private String brandName;
    private String code;

    private String isSend;

    private Date sendTime;
}
