package me.zhengjie.modules.system.service;

import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.system.domain.SysRoleMenus;
import me.zhengjie.modules.system.service.dto.MenuDto;

import java.util.List;

public interface SysRoleMenusService extends IService<SysRoleMenus> {

    List<MenuDto> findMenuByRoleId(Long id);

}
