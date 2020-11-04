package me.zhengjie.modules.system.domain.vo;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
@ApiModel("通证搜索表现类")
public class CompanyTokenSearchVo {

    @ApiModelProperty(value = "企业名称")
    private String companyName;

    @ApiModelProperty(value = "钱包地址")
    private String walletAddress;

    @ApiModelProperty(value = "存证数量")
    private Integer depositCertificateNumber;


}
