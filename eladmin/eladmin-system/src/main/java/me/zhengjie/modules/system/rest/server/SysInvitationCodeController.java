package me.zhengjie.modules.system.rest.server;


import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.system.domain.SysInvitationCode;
import me.zhengjie.modules.system.domain.vo.SysInvitationCodeSearchRq;
import me.zhengjie.modules.system.domain.vo.SysInvitationCodeVo;
import me.zhengjie.modules.system.rest.JwtBaseController;
import me.zhengjie.modules.system.service.SysInvitationCodeService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

@Api(tags = "11.邀请码【服务端】")
@RestController
@RequestMapping("/api/s/code")
@Slf4j
public class SysInvitationCodeController extends JwtBaseController {

    @Autowired
    private SysInvitationCodeService sysInvitationCodeService;

    @Log("生成邀请码")
    @ApiOperation("生成邀请码")
    @GetMapping("/generate")
    @PreAuthorize("@el.check('code:generate')")
    public RestResponse generateCode(@ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        String code = sysInvitationCodeService.generateCode(userDto);
        return RestResponse.success("生成邀请码成功", code);

    }

    @Log("邀请码分页")
    @ApiOperation("邀请码分页")
    @PostMapping("/page")
    @PreAuthorize("@el.check('code:list')")
    public RestResponse page(@RequestBody SysInvitationCodeSearchRq sysInvitationCodeSearchRq,
                             @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        Page<SysInvitationCodeVo> page = sysInvitationCodeService.codePage(userDto, sysInvitationCodeSearchRq);
        return RestResponse.success(page);

    }


}
