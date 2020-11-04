package me.zhengjie.modules.system.rest.client;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.Log;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.domain.vo.CompanyRq;
import me.zhengjie.modules.system.domain.vo.UserCompanyVo;
import me.zhengjie.modules.system.rest.JwtBaseController;
import me.zhengjie.modules.system.service.CompanyService;
import me.zhengjie.modules.system.service.dto.UserDto;
import me.zhengjie.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;

@RestController
@Api(tags = "2.企业信息【客户端】")
@RequestMapping("/api/company")
public class CompanyController extends JwtBaseController {

    @Autowired
    private CompanyService companyService;


    @Log("完善企业信息")
    @ApiOperation("完善企业信息")
    @PostMapping("/add")
    public RestResponse create(@Valid @RequestBody CompanyRq companyRq,
                               BindingResult bindingResult,
                               @ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getFieldError().getDefaultMessage());
        }
//        if (companyRq.getAuditStatus() ==1|| companyRq.getAuditStatus()== 2) {
//            throw new BadRequestException("企业认证状态错误");
//        }
        if (companyRq.getId() == null) {
            companyService.create(companyRq, userDto);
        } else {
            companyService.updateCompany(companyRq, userDto);
        }
        return RestResponse.success();
    }

    @Log("用户企业详情")
    @ApiOperation("用户企业详情")
    @GetMapping("/userCompany")
//        @PreAuthorize("@el.check('company:list')")
    public RestResponse userCompany(@ApiIgnore @ModelAttribute("userDto") UserDto userDto) {
        UserCompanyVo userCompanyVo = companyService.userCompany(userDto);
        return RestResponse.success(userCompanyVo);
    }

    @Log("获取钱包地址")
    @ApiOperation("获取钱包地址")
    @GetMapping("/walletAddress")
    public RestResponse walletAddress(@ApiIgnore @ModelAttribute("userDto") UserDto userDto) throws Exception {
        String walletAddress = companyService.walletAddress(userDto);
        return RestResponse.success("获取钱包地址", walletAddress);
    }

    @Log("判断用户企业信息是否完善")
    @ApiOperation("判断用户企业信息是否完善")
    @GetMapping("/check/company")
    public RestResponse checkCompany(@ApiIgnore @ModelAttribute("userDto") UserDto userDto) throws Exception {
        boolean flag = companyService.checkCompany(userDto);
        return RestResponse.success("判断用户企业信息是否完善", flag);
    }


    @Log("获取私钥")
    @ApiOperation("获取私钥")
    @GetMapping("/privateKey")
    public RestResponse privateKey(@ApiIgnore @ModelAttribute("userDto") UserDto userDto,
                                   @RequestParam("companyId") Long companyId) throws Exception {
        String privateKey = companyService.privateKey(userDto, companyId);
        return RestResponse.success("获取私钥", privateKey);
    }


}
