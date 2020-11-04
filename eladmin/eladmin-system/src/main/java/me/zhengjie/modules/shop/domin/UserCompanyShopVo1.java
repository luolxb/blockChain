package me.zhengjie.modules.shop.domin;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCompanyShopVo1 implements Serializable {

    private Long userId;  //这里的通证发行者ID，就是seller端的店铺ID

    private String email;

    private String mobile;

    private String nikeName;

    private String password;

    private Long companyId;  //这里的公司ID，就是seller端的组ID
}
