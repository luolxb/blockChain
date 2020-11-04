package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@ApiModel("客户端存证请求实体类")
public class DepositCertificateRq {

    @ApiModelProperty("存证ID")
    private Long id;

    @ApiModelProperty("存证用户ID")
    private Long userId;

    /**
     * 存证名称
     */
    @ApiModelProperty("存证名称")
    @NotBlank(message = "存证名称不能为空")
    private String certificateName;

    /**
     * 存证角色
     */
    @ApiModelProperty("存证角色")
    @NotBlank(message = "存证角色不能为空")
    private String certificateRole;

    /**
     * 存证logo
     */
    @ApiModelProperty("存证logo")
    @NotNull(message = "存证logo不能为空")
    private Long certificateLogo;

    /**
     * 产品规格
     */
    @ApiModelProperty("产品规格")
    @NotBlank(message = "产品规格不能为空")
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
    @NotBlank(message = "产品批次号不能为空")
    private String batchNumber;

    /**
     * 产品数量
     */
    @ApiModelProperty("产品数量")
    @NotNull(message = "产品数量不能为空")
    private Long amount;

    /**
     * 采购企业
     */
    @ApiModelProperty("采购企业")
    @NotBlank(message = "采购企业不能为空")
    private String purchasingCompany;

    /**
     * 产品名称
     */
    @ApiModelProperty("产品名称")
    @NotBlank(message = "产品名称不能为空")
    private String productName;

    /**
     * 产品图片 IDS
     */
    @ApiModelProperty("产品图片")
    @Size(min = 1, message = "产品图片不能为空")
    @NotNull(message = "产品图片不能为空")
    private List<Long> productIds;

    /**
     * 采购发票 IDS
     */
    @ApiModelProperty("采购发票")
    @Size(min = 1, message = "采购发票不能为空")
    @NotNull(message = "采购发票不能为空")
    private List<Long> invoiceIds;

    /**
     * 模板id
     */
    @ApiModelProperty("模板ID")
    @NotNull(message = "模板ID不能为空")
    private Long templateId;

    /**
     * 扩充参数
     */
    @ApiModelProperty("扩充参数")
    private String parameter;

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

    /**
     * 容量
     */
    @ApiModelProperty("容量")
    private String capacity;


}
