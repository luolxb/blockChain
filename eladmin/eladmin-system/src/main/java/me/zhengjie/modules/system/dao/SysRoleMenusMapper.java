package me.zhengjie.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import me.zhengjie.modules.system.domain.SysRoleMenus;
import me.zhengjie.modules.system.service.dto.MenuDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface SysRoleMenusMapper extends BaseMapper<SysRoleMenus> {

    /**
     * 根据角色ID查询菜单
     *
     * @param roleId
     * @return
     */
    List<MenuDto> findMenuByRoleId(@Param("roleId") Long roleId);
}
