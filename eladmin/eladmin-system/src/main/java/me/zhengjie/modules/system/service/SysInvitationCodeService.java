package me.zhengjie.modules.system.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import me.zhengjie.modules.system.domain.SysInvitationCode;
import me.zhengjie.modules.system.domain.vo.SysInvitationCodeSearchRq;
import me.zhengjie.modules.system.domain.vo.SysInvitationCodeVo;
import me.zhengjie.modules.system.service.dto.UserDto;

public interface SysInvitationCodeService extends IService<SysInvitationCode> {


    String generateCode(UserDto userDto);

    Page<SysInvitationCodeVo> codePage(UserDto userDto, SysInvitationCodeSearchRq sysInvitationCodeSearchRq);
}
