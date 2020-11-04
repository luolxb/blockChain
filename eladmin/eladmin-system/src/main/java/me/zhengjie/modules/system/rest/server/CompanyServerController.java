package me.zhengjie.modules.system.rest.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.vo.*;
import me.zhengjie.modules.system.rest.JwtBaseController;
import me.zhengjie.modules.system.service.CompanyService;
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
@Api(tags = "2.企业信息【服务端】")
@RequestMapping("/api/s/company")
public class CompanyServerController extends JwtBaseController {

    @Autowired
    private CompanyService companyService;

    @Log("新增企业信息")
    @ApiOperation("新增企业信息")
    @PostMapping("/add")
    @PreAuthorize("@el.check('company:add')")
    public RestResponse add(@Valid @RequestBody CompanyRq companyRq,
                            BindingResult bindingResult,
                            @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        companyService.createServer(companyRq, userDto);
        return RestResponse.success();
    }

    @Log("删除企业信息")
    @ApiOperation("删除企业信息")
    @ApiImplicitParam(name = "ids", value = "ids", dataType = "String", example = "1,2,3", paramType = "path")
    @PostMapping("delete/{ids}")
    @PreAuthorize("@el.check('company:del')")
    public RestResponse delete(@PathVariable("ids") String ids) {
        companyService.delete(ids);
        return RestResponse.success();
    }

    @Log("企业信息分页")
    @ApiOperation("企业信息分页")
    @PostMapping("/page")
    @PreAuthorize("@el.check('company:list')")
    public RestResponse page(Pageable pageable, @RequestBody CompanySearchRq companySearchRq) {
        Page<CompanyVo> companyPage = companyService.companyPage(pageable, companySearchRq);
        return RestResponse.success(companyPage);

    }

    @Log("查看")
    @ApiOperation("查看")
    @GetMapping("/detail")
    @ApiImplicitParam(name = "id", value = "企业ID", dataType = "Long")
    @PreAuthorize("@el.check('company:detail')")
    public RestResponse detail(@RequestParam("id") Long id) {
        CompanyVo companyVo = companyService.detail(id);
        return RestResponse.success(companyVo);

    }

    @Log("修改企业信息")
    @ApiOperation("修改企业信息")
    @PostMapping("/update")
    @PreAuthorize("@el.check('company:edit')")
    public RestResponse update(@Valid @RequestBody CompanyRq companyRq,
                               BindingResult bindingResult,
                               @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
        companyService.updateCompany(companyRq, userDto);
        return RestResponse.success();
    }

    @Log("审核企业信息")
    @ApiOperation("审核企业信息")
    @PostMapping("/audit")
    @PreAuthorize("@el.check('company:audit')")
    public RestResponse audit(@RequestBody CompanyAuditRq companyAuditRq,
                              @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        companyService.audit(companyAuditRq, userDto);
        return RestResponse.success();
    }

    @Log("用户详情")
    @ApiOperation("用户详情")
    @GetMapping("/user")
    @PreAuthorize("@el.check('company:user')")
    public RestResponse userCompany(@ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        UserCompanyVo userCompanyVo = companyService.userCompany(userDto);
        return RestResponse.success(userCompanyVo);
    }

    @Log("导出")
    @ApiOperation("导出")
    @AnonymousPostMapping(value = "/download")
    @PreAuthorize("@el.check('company:download')")
    public RestResponse download(HttpServletResponse response, @RequestBody CompanySearchRq companySearchRq) throws IOException {
        String download = companyService.download(companyService.companyAll(companySearchRq), response);
        return RestResponse.success("导出成功",download);

    }

}
