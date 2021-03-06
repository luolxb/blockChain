package me.zhengjie.modules.system.domain.vo;


import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("存证模板请求实体类")
public class DepositCertificateTemplateRq {


    @ApiModelProperty("存证模板ID")
    private Long id;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    private String productName;

    /**
     * 产品规格
     */
    @ApiModelProperty("产品规格")
    private String specification;

    /**
     * 产品箱号
     */
    @ApiModelProperty("产品箱号")
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
     * 模板名称
     */
    @NotBlank(message = "模板名称不能为空")
    @ApiModelProperty("模板名称")
    private String templateName;

    /**
     * 模板备注
     */
    @NotBlank(message = "模板备注不能为空")
    @ApiModelProperty("模板备注")
    private String templateRemark;

    /**
     * 产品图片 IDS
     */
    @ApiModelProperty("产品图片")
    private List<Long> productIds;

    /**
     * 采购发票 IDS
     */
    @ApiModelProperty("采购发票")
    private List<Long> invoiceIds;

    /**
     * 扩充参数
     */
    @ApiModelProperty("扩充参数")
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
