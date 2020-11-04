package me.zhengjie.modules.system.domain;

import com.baomidou.mybatisplus.annotation.TableName;
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
@TableName("deposit_certificate_template")
public class DepositCertificateTemplate extends BaseModel {


    private Long id;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 产品规格
     */
    private String specification;

    /**
     * 产品箱号
     */
    private String caseNumber;

    /**
     * 产品批次号
     */
    private String batchNumber;

    /**
     * 产品数量
     */
    private Long amount;

    /**
     * 采购企业
     */
    private String purchasingCompany;

    /**
     * 模板名称
     */
    private String templateName;

    /**
     * 模板备注
     */
    private String templateRemark;


    /**
     * 扩充参数
     */
    private String parameter;

    /**
     * 存证Logo
     */
    private Long certificateLogo;

    /**
     * 容量
     */
    private String capacity;
    /**
     * 香型
     */
    private String fragrance;

    /**
     * 度数
     */
    private String degree;


}
