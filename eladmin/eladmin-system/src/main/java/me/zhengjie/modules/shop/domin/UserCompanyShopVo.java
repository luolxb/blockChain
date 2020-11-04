package me.zhengjie.modules.shop.domin;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCompanyShopVo implements Serializable {

    private Long user_id;  //这里的通证发行者ID，就是seller端的店铺ID

    private String email;

    private String mobile;

    private String nike_name;

    private String password;

    private Long company_id;  //这里的公司ID，就是seller端的组ID
}
