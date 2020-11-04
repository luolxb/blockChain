package me.zhengjie.modules.system.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import me.zhengjie.modules.system.domain.SysInvitationCode;
import me.zhengjie.modules.system.domain.vo.SysInvitationCodeSearchRq;
import me.zhengjie.modules.system.domain.vo.SysInvitationCodeVo;
import me.zhengjie.modules.system.service.dto.UserDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface SysInvitationCodeMapper extends BaseMapper<SysInvitationCode> {


    List<SysInvitationCodeVo> codePage(Page<SysInvitationCodeVo> page,
                                       @Param("sysInvitationCodeSearchRq") SysInvitationCodeSearchRq sysInvitationCodeSearchRq,
                                       @Param("userDto") UserDto userDto);
}
