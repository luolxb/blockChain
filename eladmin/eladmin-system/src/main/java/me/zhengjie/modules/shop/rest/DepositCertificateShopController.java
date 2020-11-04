package me.zhengjie.modules.shop.rest;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.shop.domin.DepositCertificateShopRq;
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
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import java.io.IOException;

@RestController
@Api(tags = "存证管理【商城】")
@RequestMapping("/api/shop/certificate")
public class DepositCertificateShopController {

    @Autowired
    private DepositCertificateService depositCertificateService;

    @Log("获取存证")
    @ApiOperation("获取存证")
    @AnonymousPostMapping("/page")
    public RestResponse page(@RequestBody DepositCertificateShopRq depositCertificateShopRq) {
        Page<DepositCertificateVo> depositCertificateVoPage = depositCertificateService.certificateShopPage(depositCertificateShopRq);
        return RestResponse.success(depositCertificateVoPage);
    }


    @Log("详情")
    @ApiOperation("详情")
    @AnonymousGetMapping("/detail")
    public RestResponse detail(@RequestParam("id") Long id) {
        DepositCertificateVo depositCertificateVo = depositCertificateService.certificateShopDetail(id);
        return RestResponse.success(depositCertificateVo);
    }


}
