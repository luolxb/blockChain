package me.zhengjie.utils;

import lombok.Data;

/**
 * 区块链返回响应实体类
 *
 * @param
 */
@Data
public class BlockRestResponse<T> {

    private Integer code;

    private String message;

    private T data;

}
