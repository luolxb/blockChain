package me.zhengjie.utils.block;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

/**
 * 封装 Rsa 加密/解密， 签名/验签
 */
public class RsaFullUtil {

    /**
     * 签名
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String sign(String data) throws Exception {
        return RsaUtils.sign(data, SecretKeyConstant.RSA.get(RsaUtils.PRIVATE_KEY));
    }

    /**
     * 验签
     *
     * @param encryptData
     * @param sign
     * @return
     * @throws Exception
     */
    public static boolean verify(String encryptData, String sign) throws Exception {
        return RsaUtils.verify(encryptData, SecretKeyConstant.RSA.get(RsaUtils.PUBLIC_KEY), sign);
    }

    /**
     * 解密
     *
     * @param encryptData
     * @return
     * @throws Exception
     */
    public static String decryptByPrivateKey(String encryptData) throws Exception {
        return RsaUtils.decryptByPrivateKey(encryptData, SecretKeyConstant.RSA.get(RsaUtils.PRIVATE_KEY));
    }

    /**
     * 解密成一个对象
     */
    public static <T> T decryptObject(String encryptData, Class<T> clazz) throws Exception {
        String data = decryptByPrivateKey(encryptData);
        return JSONObject.parseObject(data, clazz);
    }

    /***
     * 加密
     *
     * @param data
     * @return
     * @throws Exception
     */
    public static String encryptByPublicKey(String data) throws Exception {
        return RsaUtils.encryptByPublicKey(data, SecretKeyConstant.RSA.get(RsaUtils.PUBLIC_KEY));
    }

    /***
     * 加密, 对象加密
     *
     * @param object
     * @return
     * @throws Exception
     */
    public static String encryptObject(Object object) throws Exception {
        return encryptByPublicKey(JSON.toJSONString(object));
    }

}
