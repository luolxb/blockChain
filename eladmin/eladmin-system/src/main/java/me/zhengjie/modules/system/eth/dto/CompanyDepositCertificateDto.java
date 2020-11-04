package me.zhengjie.modules.system.eth.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import me.zhengjie.domain.LocalStorage;

import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

@Data
public class CompanyDepositCertificateDto {

    /**
     * 存证ID
     */
    private Long id;

    @ApiModelProperty(value = "企业ID")
    private Long companyId;

    @ApiModelProperty(value = "企业类型")
    private String companyType;

    @ApiModelProperty(value = "企业类型名称")
    private String companyTypeName;

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


//
//    @ApiModelProperty("存证ID")
//    private Long id;
//
//    /**
//     * 存证名称
//     */
//    @ApiModelProperty("存证名称")
//    private String certificateName;
//
//    /**
//     * 存证角色
//     */
//    @ApiModelProperty("存证角色")
//    private String certificateRole;
//
//    /**
//     * 存证角色
//     */
//    @ApiModelProperty("存证角色")
//    private String certificateRoleName;
//
    /**
     * 存证logo
     */
    @ApiModelProperty("存证logo")
    private Long certificateLogo;

//    @ApiModelProperty("存证logo")
//    private List<LocalStorage> certificateLogoPic;

    /**
     * 产品规格
     */
    @ApiModelProperty("产品规格")
    private String specification;

    /**
     * 产品箱号
     */
    @ApiModelProperty("产品箱号")
    @NotBlank(message = "产品箱号不能为空")
    private String caseNumber;

    /**
     * 产品批次号
     */
    @ApiModelProperty("产品批次号")
    private String batchNumber;

    /**
     * 产品数量
     */
    @ApiModelProperty("产品数量")
    private Long amount;

    /**
     * 采购企业
     */
    @ApiModelProperty("采购企业")
    private String purchasingCompany;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;


    /**
     * 产品图片
     */
    @ApiModelProperty("产品图片")
    private List<LocalStorage> productList;

    /**
     * 采购发票
     */
    @ApiModelProperty("采购发票")
    private List<LocalStorage> invoiceList;
//
//    /**
//     * 模板id
//     */
//    @ApiModelProperty("模板id")
//    private Long templateId;
//
//    /**
//     * 申请时间
//     */
//    @ApiModelProperty("申请时间")
//    private Date appliTime;

    /**
     * 上链时间
     */
    @ApiModelProperty("上链时间")
    private Date onChainTime;

    /**
     * 哈希值
     */
    @ApiModelProperty("哈希值")
    private String hasdValue;

    /**
     * 区块高度
     */
    @ApiModelProperty("区块高度")
    private String blockHeight;
//
//    /**
//     * 上链转态1:未上链  2：已上链 3：上链中 4：上链失败
//     */
//    @ApiModelProperty("上链转态1:未上链  2：已上链")
//    private Integer onChainStatus;

//    /**
//     * 审核备注
//     */
//    @ApiModelProperty("审核备注")
//    private String auditRemark;

    @ApiModelProperty(value = "保险单")
    private List<LocalStorage> policy;

    @ApiModelProperty(value = "确认书")
    private List<LocalStorage> confirmation;

    @ApiModelProperty(value = "评估证书")
    private List<LocalStorage> evaluationCertificate;

    private String parameter;

}
