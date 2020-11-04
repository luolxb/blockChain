package me.zhengjie.modules.system.eth.entity;

import lombok.Data;

import java.math.BigInteger;

@Data
public class EntityCallBack {

    /**
     * 参数 id
     */
    private Integer id;

    /**
     * 区块 hash
     */
    private String hash;

    /**
     * 区块高度
     */
    private BigInteger blockNumber;


    /**
     * 上链是否成功: 1失败 2成功
     */
    private String isValid;
}
