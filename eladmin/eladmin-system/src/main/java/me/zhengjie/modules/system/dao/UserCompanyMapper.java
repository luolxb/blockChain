package me.zhengjie.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.shop.domin.UserCompanyShopVo;
import me.zhengjie.modules.shop.domin.UserCompanyShopVo1;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.UserCompany;
import me.zhengjie.modules.system.domain.vo.UserCompanyVo;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

@Repository
@Mapper
public interface UserCompanyMapper extends BaseMapper<UserCompany> {

    UserCompanyShopVo1 sendUserCompany2Shop(@Param("phone") String phone);

    UserCompanyVo getUserCompanyVoByCompanyId(@Param("id") Long id);
}
