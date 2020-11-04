package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;
import me.zhengjie.domain.LocalStorage;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("存证表现实体类")
public class DepositCertificateVo implements Serializable {

    @ApiModelProperty("存证ID")
    private Long id;

    @ApiModelProperty("存证用户ID")
    private Long userId;

    /**
     * 存证名称
     */
    @ApiModelProperty("存证名称")
    private String certificateName;

    /**
     * 存证角色
     */
    @ApiModelProperty("存证角色")
    private String certificateRole;

    /**
     * 存证角色
     */
    @ApiModelProperty("存证角色")
    private String certificateRoleName;

    /**
     * 存证logo
     */
    @ApiModelProperty("存证logo")
    private Long certificateLogo;

    @ApiModelProperty("存证logo")
    private List<LocalStorage> certificateLogoPic;


    @ApiModelProperty("企业logo")
    private List<LocalStorage> companyLogoPic;

    @ApiModelProperty("企业logo")
    private Long companyLogo;

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

    /**
     * 模板id
     */
    @ApiModelProperty("模板id")
    private Long templateId;

    /**
     * 申请时间
     */
    @ApiModelProperty("申请时间")
    private Date appliTime;
    @ApiModelProperty("申请时间")
    private Date createTime;

    /**
     * 上链时间
     */
    @ApiModelProperty("上链时间")
    private Date onChainTime;

    /**
     * 上链转态1:未上链  2：已上链 3：上链中 4：上链失败
     */
    @ApiModelProperty("上链转态1:未上链  2：已上链")
    private Integer onChainStatus;
    private String onChainStatusStr;

    /**
     * 审核备注
     */
    @ApiModelProperty("审核备注")
    private String auditRemark;

    @ApiModelProperty(value = "保险单")
    private List<LocalStorage> policy;

    @ApiModelProperty(value = "确认书")
    private List<LocalStorage> confirmation;

    @ApiModelProperty(value = "评估证书")
    private List<LocalStorage> evaluationCertificate;

    /**
     * 哈希值
     */
    private String hasdValue;

    /**
     * 区块高度
     */
    private String blockHeight;

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "类目")
    private String category;

    @ApiModelProperty(value = "审核时间")
    private Date auditTime;


    /**
     * 扩充参数
     */
    private String parameter;

    /**
     * 容量
     */
    @ApiModelProperty("容量")
    private String capacity;
    /**
     * 香型
     */
    @ApiModelProperty("香型")
    private String fragrance;

    /**
     * 度数
     */
    @ApiModelProperty("度数")
    private String degree;


}
