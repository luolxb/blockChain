package me.zhengjie.modules.system.rest.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.vo.DepositCertificateRq;
import me.zhengjie.modules.system.domain.vo.DepositCertificateSearchRq;
import me.zhengjie.modules.system.domain.vo.DepositCertificateVo;
import me.zhengjie.modules.system.rest.JwtBaseController;
import me.zhengjie.modules.system.service.DepositCertificateService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@Api(tags = "6.存证管理【客户端】")
@RequestMapping("/api/certificate")
public class DepositCertificateController extends JwtBaseController {

    @Autowired
    private DepositCertificateService depositCertificateService;

    @Log("新增存证")
    @ApiOperation("新增存证")
    @PostMapping("/add")
    public RestResponse create(@Valid @RequestBody DepositCertificateRq depositCertificateRq,
                               BindingResult bindingResult,
                               @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        depositCertificateService.create(depositCertificateRq, userDto);
        return RestResponse.success();
    }

    @Log("存证分页")
    @ApiOperation("存证分页")
    @PostMapping("/page")
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
    public RestResponse detail(@RequestParam("id") Long id,
                               @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        return RestResponse.success(depositCertificateService.detail(id,userDto));
    }


    @Log("修改存证")
    @ApiOperation("修改存证")
    @PostMapping("/edit")
    public RestResponse edit(@Valid @RequestBody DepositCertificateRq depositCertificateRq,
                             BindingResult bindingResult,
                             @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        depositCertificateService.edit(depositCertificateRq, userDto);
        return RestResponse.success();
    }


    @Log("申请上链")
    @ApiOperation("申请上链")
    @PostMapping("/appliChain")
    public RestResponse appliChain(@RequestBody DepositCertificateRq depositCertificateRq,
                                   @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        depositCertificateService.appliChain(depositCertificateRq, userDto);
        return RestResponse.success();
    }

}
