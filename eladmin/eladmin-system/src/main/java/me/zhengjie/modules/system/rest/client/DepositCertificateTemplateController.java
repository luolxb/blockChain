package me.zhengjie.modules.system.rest.client;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.Log;
import me.zhengjie.modules.system.domain.vo.DepositCertificateTemplateVo;
import me.zhengjie.modules.system.rest.JwtBaseController;
import me.zhengjie.modules.system.service.DepositCertificateTemplateServie;
import me.zhengjie.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "5.存证模板【客户端】")
@RequestMapping("/api/template")
public class DepositCertificateTemplateController extends JwtBaseController {

    @Autowired
    private DepositCertificateTemplateServie depositCertificateTemplateServie;


    @Log("存证模板分页")
    @ApiOperation("存证模板分页")
    @PostMapping("/page")
    public RestResponse page(Pageable pageable) {
        Page<DepositCertificateTemplateVo> depositCertificateTemplateVoPage = depositCertificateTemplateServie.templatePage(pageable);
        return RestResponse.success(depositCertificateTemplateVoPage);
    }


}
