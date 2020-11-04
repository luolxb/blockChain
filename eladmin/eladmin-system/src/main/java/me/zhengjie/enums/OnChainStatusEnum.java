package me.zhengjie.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 上链状态枚举
 */
@Getter
@AllArgsConstructor
public enum OnChainStatusEnum {

    /**
     * 上链转态1:未上链  2：已上链 3：上链中 4：上链失败 5:申请上链
     */
    ON_CHAIN_STATUS_No(1, "未上链"),
    ON_CHAIN_STATUS_YES(2, "已上链"),
    ON_CHAIN_STATUS_ING(3, "上链中"),
    ON_CHAIN_STATUS_FAIL(4, "上链失败"),
    ON_CHAIN_STATUS_APPLI(5, "申请上链");


    private final Integer code;
    private final String description;

    public static CodeBiEnum find(Integer code) {
        for (CodeBiEnum value : CodeBiEnum.values()) {
            if (code.equals(value.getCode())) {
                return value;
            }
        }
        return null;
    }
}
