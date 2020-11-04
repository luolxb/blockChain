package me.zhengjie.modules.system.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.dao.SysInvitationCodeMapper;
import me.zhengjie.modules.system.domain.SysInvitationCode;
import me.zhengjie.modules.system.domain.vo.SysInvitationCodeSearchRq;
import me.zhengjie.modules.system.domain.vo.SysInvitationCodeVo;
import me.zhengjie.modules.system.service.SysInvitationCodeService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.DateUtil;
import me.zhengjie.utils.NtsUtil;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class SysInvitationCodeServiceImpl extends ServiceImpl<SysInvitationCodeMapper, SysInvitationCode> implements SysInvitationCodeService {

    @Override
    public String generateCode(UserDto userDto) {
        String s = NtsUtil.generateCode(8);
        SysInvitationCode sysInvitationCode = new SysInvitationCode();
        sysInvitationCode.setCode(s);
        sysInvitationCode.setCreateBy(userDto.getUsername());
        sysInvitationCode.setCreateTime(new Date());
        boolean save = this.save(sysInvitationCode);
        if (!save) {
            throw new BadRequestException("生成邀请码异常");
        }
        return s;
    }

    /**
     * 邀请码分页
     *
     * @param userDto
     * @param sysInvitationCodeSearchRq
     * @return
     */
    @Override
    public Page<SysInvitationCodeVo> codePage(UserDto userDto, SysInvitationCodeSearchRq sysInvitationCodeSearchRq) {
        Integer size = sysInvitationCodeSearchRq.getSize();
        Integer pageNumber = sysInvitationCodeSearchRq.getPage();
        Page<SysInvitationCodeVo> page = new Page<>(pageNumber, size);
        sysInvitationCodeSearchRq.setStartTimeStr(DateUtil.getFormatDate(sysInvitationCodeSearchRq.getStartTime(), "yyyy-MM-dd"));
        sysInvitationCodeSearchRq.setEndTimeStr(DateUtil.getFormatDate(sysInvitationCodeSearchRq.getEndTime(), "yyyy-MM-dd"));
        List<SysInvitationCodeVo> codeList = this.baseMapper.codePage(page, sysInvitationCodeSearchRq, userDto);
        return page.setRecords(codeList);
    }
}
