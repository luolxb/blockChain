package me.zhengjie.modules.system.domain.vo;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import me.zhengjie.modules.system.domain.BaseModel;

import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class DepositCertificateShopVo extends BaseModel implements Serializable {

    private Long id;

    /**
     * 存证名称
     */
    private String certificateName;

    /**
     * 存证角色
     */
    private String certificateRole;

    /**
     * 存证logo
     */
    private Long certificateLogo;

    /**
     * 用户id
     */
    private Long userId;

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
     * 产品名称
     */
    private String productName;

    /**
     * 存证审核时间
     */
    private Date auditTime;

    /**
     * 哈希值
     */
    private String hasdValue;

    /**
     * 区块高度
     */
    private String blockHeight;

    /**
     * 模板id
     */
    private Long templateId;

    /**
     * 申请时间
     */
    private Date appliTime;

    /**
     * 上链时间
     */
    private Date onChainTime;

    /**
     * 上链转态1:未上链  2：已上链 3：上链中 4：上链失败 5:申请上链
     */
    private Integer onChainStatus;

    /**
     * 审核备注
     */
    private String auditRemark;

    /**
     * 审核状态 1：上链  2：拒绝
     */
    private Integer auditStatus;

    /**
     * 审核人
     */
    private String auditBy;

    /**
     * 扩充参数
     */
    private String parameter;

    /**
     * 是否发送
     */
    private String isSend;
    /**
     * 发送时间
     */
    private Date sendTime;


    /**
     * 商城的 商户id
     */
    private Long sellerId;

    /**
     * 证书
     */
    private String certificate;

}
