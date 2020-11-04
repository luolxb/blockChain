package me.zhengjie.modules.system.eth.entity;

import lombok.Data;

@Data
public class EntityBlock {

    /**
     * 地址
     */
    private String address;

    /**
     * 助记词
     */
    private String mnemonic;

    /**
     * 私钥
     */
    private String privateKey;

    /**
     * 公钥
     */
    private String publicKey;
}
