package me.zhengjie.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.modules.system.dao.SysRoleMenusMapper;
import me.zhengjie.modules.system.domain.SysRoleMenus;
import me.zhengjie.modules.system.service.SysRoleMenusService;
import me.zhengjie.modules.system.service.dto.MenuDto;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class SysRoleMenusServiceImpl extends ServiceImpl<SysRoleMenusMapper,SysRoleMenus> implements SysRoleMenusService {

    @Override
    public List<MenuDto> findMenuByRoleId(Long id){
        return this.baseMapper.findMenuByRoleId(id);

    }
}
