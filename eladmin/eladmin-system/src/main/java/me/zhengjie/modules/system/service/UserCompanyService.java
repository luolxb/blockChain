package me.zhengjie.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.shop.domin.UserCompanyShopVo;
import me.zhengjie.modules.shop.domin.UserCompanyShopVo1;
import me.zhengjie.modules.system.domain.UserCompany;
import me.zhengjie.modules.system.domain.vo.UserCompanyVo;

public interface UserCompanyService extends IService<UserCompany> {

    UserCompanyShopVo1 sendUserCompany2Shop(String phone);

    UserCompanyVo getUserCompanyVoByCompanyId(Long id);
}
