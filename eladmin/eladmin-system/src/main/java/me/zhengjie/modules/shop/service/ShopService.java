package me.zhengjie.modules.shop.service;

import me.zhengjie.modules.shop.domin.UserCompanyShopVo;
import me.zhengjie.modules.system.domain.Company;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.utils.BlockRestResponse;
import org.springframework.scheduling.annotation.Async;

public interface ShopService {

    @Async
    void sendUserCompany2Shop(User user, Company company);


    @Async
    void send2Shop(BlockRestResponse blockRestResponse);

    void sendUserCompany2ShopAsync(String phone);
}
