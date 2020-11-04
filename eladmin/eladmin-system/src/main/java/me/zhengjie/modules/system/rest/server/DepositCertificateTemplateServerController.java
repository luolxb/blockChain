package me.zhengjie.modules.system.rest.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.vo.DepositCertificateTemplateRq;
import me.zhengjie.modules.system.domain.vo.DepositCertificateTemplateVo;
import me.zhengjie.modules.system.rest.JwtBaseController;
import me.zhengjie.modules.system.service.DepositCertificateTemplateServie;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@Api(tags = "5.存证模板【服务端】")
@RequestMapping("/api/s/template")
public class DepositCertificateTemplateServerController extends JwtBaseController {

    @Autowired
    private DepositCertificateTemplateServie depositCertificateTemplateServie;


    @Log("新增存证模板")
    @ApiOperation("新增存证模板")
    @PostMapping("/add")
    @PreAuthorize("@el.check('template:add')")
    public RestResponse create(@Valid @RequestBody DepositCertificateTemplateRq depositCertificateTemplateRq,
                               BindingResult bindingResult,
                               @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        depositCertificateTemplateServie.create(depositCertificateTemplateRq, userDto);
        return RestResponse.success();
    }

    @Log("编辑存证模板")
    @ApiOperation("编辑存证模板")
    @PostMapping("/edit")
    @PreAuthorize("@el.check('template:edit')")
    public RestResponse edit(@Valid @RequestBody DepositCertificateTemplateRq depositCertificateTemplateRq,
                             BindingResult bindingResult,
                             @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        depositCertificateTemplateServie.edit(depositCertificateTemplateRq, userDto);
        return RestResponse.success();
    }

    @Log("删除存证模板")
    @ApiOperation("删除存证模板")
    @PostMapping("/del/{id}")
    @ApiImplicitParam(name = "id", value = "模板ID", dataType = "Long", paramType = "path")
    @PreAuthorize("@el.check('template:del')")
    public RestResponse del(@ApiIgnore @ModelAttribute("userDto") UserDto userDto,
                            @PathVariable Long id) {
        depositCertificateTemplateServie.del(id, userDto);
        return RestResponse.success();
    }

    @Log("存证模板分页")
    @ApiOperation("存证模板分页")
    @PostMapping("/page")
    @PreAuthorize("@el.check('template:list')")
    public RestResponse page(Pageable pageable) {
        Page<DepositCertificateTemplateVo> depositCertificateTemplateVoPage = depositCertificateTemplateServie.templatePage(pageable);
        return RestResponse.success(depositCertificateTemplateVoPage);
    }


}
