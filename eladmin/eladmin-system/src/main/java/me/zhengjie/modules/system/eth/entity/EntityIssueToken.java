package me.zhengjie.modules.system.eth.entity;

import lombok.Data;

import java.math.BigInteger;

/**
 * 上链请求实体类
 */
@Data
public class EntityIssueToken {

    /**
     * 钱包地址
     */
    private String address;

    /**
     *
     * 回调地址
     */
    private String callUrl;
    /**
     * 律师确定权书
     */
    private String attorneyCertImages;
    /**
     * 价值评估证书
     */
    private String evaluationCertImages;
    /**
     * 保险单
     */
    private String insurancePolicyImages;
    /**
     * 产品图片
     */
    private String productImages;

    /**
     * 产品logo
     */
    private String productLogo;

    /**
     * 产品批次
     */
    private String productLot;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 产品箱号
     */
    private String productNumber;

    /**
     * 产品规格
     */
    private String productSpec;

    /**
     * 采购单
     */
    private String purchaseNoteImages;

    /**
     * 采购企业
     */
    private String sourcingCompany;

    /**
     * 存证ID
     */
    private Long id;
    /**
     * 产品数量
     */
    private Integer productCount;


}
