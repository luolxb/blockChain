package me.zhengjie.modules.system.rest.client;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.modules.system.sms.service.SendSmsServiceImpl;
import me.zhengjie.utils.RestResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "9.公共接口")
@RequestMapping("/api/company")
public class CommonController {

    @Autowired
    private SendSmsServiceImpl sendSmsService;

    @Log("获取验证码")
    @ApiOperation("获取验证码")
    @AnonymousGetMapping("/code")
    public RestResponse code(@RequestParam("mobile") String mobile) {
        sendSmsService.getCode(mobile);
        return RestResponse.success("发送获取验证码成功");
    }
}
