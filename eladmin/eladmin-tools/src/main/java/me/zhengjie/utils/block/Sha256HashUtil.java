package me.zhengjie.utils.block;

import java.math.BigInteger;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

/**
 * 计算字符串的 sha256 hash值
 *
 * @author dengzhipeng
 * @date 2019/05/07
 */
public class Sha256HashUtil {

    private static final String SHA_256 = "SHA-256";

    private static final int HEX = 16;

    /**
     * 获取字符串的 hash 值，默认 UTF-8 编码
     *
     * @param value
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String getHashSHA256(String value) throws NoSuchAlgorithmException {
        return getHashSHA256(value, StandardCharsets.UTF_8);
    }

    /**
     * 获取字符串的 hash 值，默认 UTF-8 编码
     *
     * @param value
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static BigInteger getHashSHA256OfDecimal(String value) throws NoSuchAlgorithmException {
        return new BigInteger(getHashSHA256(value), HEX);
    }

    /**
     * 获取字符串的 hash 散列值
     *
     * @param value   原数据
     * @param charset 编码类型
     * @return hash 散列值
     * @throws NoSuchAlgorithmException
     */
    public static String getHashSHA256(String value, Charset charset) throws NoSuchAlgorithmException {
        byte[] bytes = value.getBytes(charset);
        return getHashSHA256(bytes);
    }


    public static String getHashSHA256(byte[] bytes) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance(SHA_256);
        md.update(bytes, 0, bytes.length);
        byte[] digest = md.digest();
        return toHex(digest);
    }

    public static String toHex(byte[] bytes) {
        StringBuilder hash = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                hash.append('0');
            }
            hash.append(hex);
        }
        return hash.toString();
    }

    /**
     * 生成 tokenId，返回10进制
     *
     * @param list
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static BigInteger generateTokenOfDecimal(List<String> list) throws NoSuchAlgorithmException {
        return new BigInteger(generateTokenId(list), 16);
    }

    /**
     * 生成 tokenId，返回10进制
     *
     * @param list
     * @return
     * @throws NoSuchAlgorithmException
     */
    public static String generateTokenId(List<String> list) throws NoSuchAlgorithmException {
        List<String> hashList = new ArrayList<>(20);
        for (String s : list) {
            hashList.add(Sha256HashUtil.getHashSHA256(s));
        }
        StringBuffer buffer = new StringBuffer();
        hashList.forEach(s -> {
            buffer.append(s);
        });
        return Sha256HashUtil.getHashSHA256(buffer.toString());
    }

    /**
     * 测试用例
     *
     * @param args
     */
    public static void main(String[] args) {
        try {
            String string = "新时空";
            String hash = Sha256HashUtil.getHashSHA256(string);
            System.out.println("十六进制: " + hash);
//            System.out.println(hash.length());
            // 十进制
            System.out.println("十进制: " + getHashSHA256OfDecimal(string));


            List<String> list = new ArrayList<>();
            list.add("123123121321231231231231231231231231231231231312312123123121321231231231231231231231231231231231312312");
            list.add("123123121321231231231231231231231231231231231312312123123121321231231231231231231231231231231231312312");
            System.out.println("十六进制 = " + generateTokenId(list));
            System.out.println("十进制 = " + generateTokenOfDecimal(list));

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

    }
}
