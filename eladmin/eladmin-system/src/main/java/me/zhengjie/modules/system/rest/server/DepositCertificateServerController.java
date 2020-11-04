package me.zhengjie.modules.system.rest.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.vo.DepositCertificateAuditRq;
import me.zhengjie.modules.system.domain.vo.DepositCertificateSearchRq;
import me.zhengjie.modules.system.domain.vo.DepositCertificateServerRq;
import me.zhengjie.modules.system.domain.vo.DepositCertificateVo;
import me.zhengjie.modules.system.rest.JwtBaseController;
import me.zhengjie.modules.system.service.DepositCertificateService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@Api(tags = "6.存证管理【服务端】")
@RequestMapping("/api/s/certificate")
public class DepositCertificateServerController extends JwtBaseController {

    @Autowired
    private DepositCertificateService depositCertificateService;


    @Log("存证分页")
    @ApiOperation("存证分页")
    @PostMapping("/page")
    @PreAuthorize("@el.check('certificate:list')")
    public RestResponse page(Pageable pageable,
                             @RequestBody DepositCertificateSearchRq depositCertificateSearchRq,
                             @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        Page<DepositCertificateVo> depositCertificateVoPage = depositCertificateService.certificatePage(pageable, userDto, depositCertificateSearchRq);
        return RestResponse.success(depositCertificateVoPage);
    }

    @Log("查看")
    @ApiOperation("查看")
    @ApiImplicitParam(name = "id", value = "存证ID", dataType = "Long")
    @GetMapping("/detail")
    @PreAuthorize("@el.check('certificate:detail')")
    public RestResponse detail(@RequestParam("id") Long id,
                               @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        return RestResponse.success(depositCertificateService.detail(id,userDto));
    }


    @Log("修改存证")
    @ApiOperation("修改存证")
    @PostMapping("/edit")
    @PreAuthorize("@el.check('certificate:edit')")
    public RestResponse edit(@Valid @RequestBody DepositCertificateServerRq depositCertificateServerRq,
                             BindingResult bindingResult,
                             @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        depositCertificateService.editS(depositCertificateServerRq, userDto);
        return RestResponse.success();
    }


    @Log("审核")
    @ApiOperation("审核")
    @PostMapping("/audit")
    @PreAuthorize("@el.check('certificate:audit')")
    public RestResponse audit(@Valid @RequestBody DepositCertificateAuditRq depositCertificateAuditRq,
                              @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        depositCertificateService.audit(depositCertificateAuditRq, userDto);
        return RestResponse.success();
    }

    @Log("导出")
    @ApiOperation("导出")
    @AnonymousPostMapping(value = "/download")
    @PreAuthorize("@el.check('certificate:download')")
    public RestResponse download(HttpServletResponse response, @RequestBody DepositCertificateSearchRq depositCertificateSearchRq) throws IOException {
        String download = depositCertificateService.download(depositCertificateService.depositCertificateAll(depositCertificateSearchRq), response);
        return RestResponse.success("导出成功",download);
    }

}
