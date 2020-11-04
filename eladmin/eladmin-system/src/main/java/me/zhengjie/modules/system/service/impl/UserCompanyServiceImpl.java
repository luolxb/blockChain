package me.zhengjie.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.shop.domin.UserCompanyShopVo;
import me.zhengjie.modules.shop.domin.UserCompanyShopVo1;
import me.zhengjie.modules.system.dao.UserCompanyMapper;
import me.zhengjie.modules.system.domain.UserCompany;
import me.zhengjie.modules.system.domain.vo.UserCompanyVo;
import me.zhengjie.modules.system.service.UserCompanyService;
import org.springframework.stereotype.Service;


@Service
@Slf4j
public class UserCompanyServiceImpl extends ServiceImpl<UserCompanyMapper, UserCompany> implements UserCompanyService {
    @Override
    public UserCompanyShopVo1 sendUserCompany2Shop(String phone) {
        return this.baseMapper.sendUserCompany2Shop(phone);
    }

    @Override
    public UserCompanyVo getUserCompanyVoByCompanyId(Long id) {
        return this.baseMapper.getUserCompanyVoByCompanyId(id);
    }
}
