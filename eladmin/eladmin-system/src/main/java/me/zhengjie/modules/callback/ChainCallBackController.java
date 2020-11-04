package me.zhengjie.modules.callback;

import com.alibaba.fastjson.JSON;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.annotation.Log;
import me.zhengjie.annotation.rest.AnonymousGetMapping;
import me.zhengjie.annotation.rest.AnonymousPostMapping;
import me.zhengjie.enums.RelaTypeEnum;
import me.zhengjie.modules.shop.service.ShopService;
import me.zhengjie.modules.system.domain.AttachmentRela;
import me.zhengjie.modules.system.eth.entity.EntityCallBack;
import me.zhengjie.modules.system.service.AttachmentRelaService;
import me.zhengjie.modules.system.service.DepositCertificateService;
import me.zhengjie.utils.BlockRestResponse;
import me.zhengjie.utils.RestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;

@RestController
@RequestMapping("/callBack")
@Api(tags = "回调控制器")
@Slf4j
public class ChainCallBackController {

    @Autowired
    private DepositCertificateService depositCertificateService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private AttachmentRelaService attachmentRelaService;

    /**
     * 上链回调
     *
     * @return
     */
    @Log("上链回调")
    @ApiOperation("上链回调")
    @AnonymousPostMapping("/chain")
    public RestResponse onChainCallBack(@RequestBody BlockRestResponse blockRestResponse) {
        log.info("上链回调==>{}", blockRestResponse);
        depositCertificateService.onChainCallBack(blockRestResponse);
        EntityCallBack data = JSON.parseObject(String.valueOf(blockRestResponse.getData()), EntityCallBack.class);

        // 如果上链成功才发送到商城
        if (StringUtils.equals("2",data.getIsValid())) {
            shopService.send2Shop(blockRestResponse);
        }
        return RestResponse.success();

    }

    @Log("同步企业用户信息")
    @ApiOperation("同步企业用户信息")
    @AnonymousGetMapping("/sendUserCompany2Shop")
    public RestResponse sendUserCompany2Shop(@RequestParam String phone) {
        shopService.sendUserCompany2ShopAsync(phone);
        return RestResponse.success();
    }

    @AnonymousGetMapping("/saveAttachmentRela")
    public RestResponse saveAttachmentRela(@RequestParam int startId,
                                           @RequestParam int endId) {

        // -- 2289:律师确认书 4
        //-- 2288:评估书  5
        //-- 2286:保险单 3
        for (int i = startId; i < endId; i++) {
            AttachmentRela attachmentRela = new AttachmentRela();
            attachmentRela.setRelaType(RelaTypeEnum.RELA_TYPE_CERTIFICATE.getCode());
            attachmentRela.setCreateTime(new Date());
            attachmentRela.setCreateBy("admin");
            attachmentRela.setUpdateTime(new Date());
            attachmentRela.setUpdateBy("admin");
            attachmentRela.setRelaId(Long.parseLong(String.valueOf(i)));

            attachmentRela.setAttachmentType(4L);
            attachmentRela.setAttachmentId(2289L);
            // 律师确认书
            attachmentRelaService.save(attachmentRela);

            attachmentRela.setAttachmentType(3L);
            attachmentRela.setAttachmentId(2286L);
            // 保险单
            attachmentRelaService.save(attachmentRela);

            attachmentRela.setAttachmentType(5L);
            attachmentRela.setAttachmentId(2288L);
            // 评估书
            attachmentRelaService.save(attachmentRela);
        }

        return RestResponse.success();

    }
}
