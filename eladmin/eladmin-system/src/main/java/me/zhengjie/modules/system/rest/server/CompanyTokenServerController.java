package me.zhengjie.modules.system.rest.server;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.modules.system.domain.vo.CompanyTokenSearchRq;
import me.zhengjie.modules.system.domain.vo.CompanyTokenSearchVo;
import me.zhengjie.modules.system.rest.JwtBaseController;
import me.zhengjie.modules.system.service.CompanyService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@RestController
@Api(tags = "7.通证管理【服务端】")
@RequestMapping("/api/s/token")
public class CompanyTokenServerController extends JwtBaseController {

    @Autowired
    private CompanyService companyService;


    @Log("通证管理分页")
    @ApiOperation("通证管理分页")
    @PostMapping("/page")
    @PreAuthorize("@el.check('token:list')")
    public RestResponse page(Pageable pageable,
                             @RequestBody CompanyTokenSearchRq companyTokenSearchRq,
                             @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        Page<CompanyTokenSearchVo> companyTokenVoPage = companyService.companyTokenPage(pageable, companyTokenSearchRq, userDto);
        return RestResponse.success(companyTokenVoPage);
    }

    @Log("导出")
    @ApiOperation("导出")
    @AnonymousPostMapping(value = "/download")
    @PreAuthorize("@el.check('token:download')")
    public RestResponse download(HttpServletResponse response, @RequestBody CompanyTokenSearchRq companyTokenSearchRq) throws IOException {
        String token = companyService.downloadCompanyToken(companyService.companyTokenAll(companyTokenSearchRq), response);
        return RestResponse.success("导出成功",token);
    }


}
