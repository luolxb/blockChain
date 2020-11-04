package me.zhengjie.modules.shop.domin;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class DepositCertificateShopRq {

    private Long id;


    @ApiModelProperty("当前页")
    private Integer page=1;

    @ApiModelProperty("每页大小")
    private Integer size = 10;
}
