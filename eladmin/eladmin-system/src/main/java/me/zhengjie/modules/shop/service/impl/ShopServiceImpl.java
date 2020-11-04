package me.zhengjie.modules.shop.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.enums.OnChainStatusEnum;
import me.zhengjie.modules.shop.domin.UserCompanyShopVo;
import me.zhengjie.modules.shop.domin.UserCompanyShopVo1;
import me.zhengjie.modules.shop.service.ShopService;
import me.zhengjie.modules.system.domain.Company;
import me.zhengjie.modules.system.domain.DepositCertificate;
import me.zhengjie.modules.system.domain.User;
import me.zhengjie.modules.system.domain.UserCompany;
import me.zhengjie.modules.system.domain.vo.DepositCertificateShopVo;
import me.zhengjie.modules.system.eth.entity.EntityCallBack;
import me.zhengjie.modules.system.service.CompanyService;
import me.zhengjie.modules.system.service.DepositCertificateService;
import me.zhengjie.modules.system.service.UserCompanyService;
import me.zhengjie.utils.BlockRestResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;


@Service
@Slf4j
public class ShopServiceImpl implements ShopService {

    @Value("${shop.ip}")
    private String shopIp;

    @Value("${shop.port}")
    private String shopPort;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private CompanyService companyService;

    @Autowired
    private UserCompanyService userCompanyService;

    @Autowired
    private DepositCertificateService depositCertificateService;


    @Override
    @Transactional(rollbackFor = Exception.class)
    public void sendUserCompany2Shop(User user, Company company) {
        UserCompanyShopVo userCompanyShopVo = new UserCompanyShopVo();
        userCompanyShopVo.setUser_id(user.getId());
        userCompanyShopVo.setPassword(user.getPassword());
        userCompanyShopVo.setMobile(user.getPhone());
        userCompanyShopVo.setNike_name(user.getUsername());
        userCompanyShopVo.setCompany_id(company.getId());
        // TODO 暂时没有邮箱
        userCompanyShopVo.setEmail("");
        log.info("新增企业信息发送到商城数据 ==>{}", userCompanyShopVo);

        boolean flag = true;
        String url = "http://" + shopIp + ":" + shopPort + "/member/add";
        log.info("url==>{}", url);
        try {
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, userCompanyShopVo, String.class);
            log.info("新增企业信息发送到商城返回数据 ==>{}", stringResponseEntity);

            String body = stringResponseEntity.getBody();
            if (body == null) {
                flag = false;
            }
            // 获取响应code 判断是否接受成功
            JSONObject jsonObject = JSON.parseObject(body);
            Integer status = (Integer) jsonObject.get("status");
            if (status != 1) {
                flag = false;
            }
        } catch (Exception e) {
            log.error("e", e);
            log.error("发送企业信息到商城失败");
            flag = false;
        }

        if (flag) {
            Company company1 = new Company();
            company1.setId(company.getId());
            company1.setIsSend("Y");
            company1.setSendTime(new Date());
            companyService.updateById(company1);
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void send2Shop(BlockRestResponse blockRestResponse) {
        // 成功返回码 0
        if (blockRestResponse.getCode() != 0) {
            return;
        }
        // 构建存证实体类 根据ID修改
        EntityCallBack data = JSON.parseObject(String.valueOf(blockRestResponse.getData()), EntityCallBack.class);
        DepositCertificate one = depositCertificateService.getById(Long.parseLong(data.getId() + ""));
        if (one == null) {
            log.error("发送到商城的存证不存在");
            return;
        }

        UserCompany userCompany = userCompanyService.getOne(new QueryWrapper<UserCompany>().eq("user_id", one.getUserId()));
        if (userCompany == null) {
            log.error("发送到商城的商户ID不存在");
            return;
        }
        if (one.getOnChainStatus().equals(OnChainStatusEnum.ON_CHAIN_STATUS_YES.getCode()) && StringUtils.equals(one.getIsSend(), "Y")) {
            return;
        }

        one.setIsSend(null);
        log.info("发送到商城==>{}", one);
        // 发送到商城
        DepositCertificate depositCertificate = new DepositCertificate();
        depositCertificate.setId(Long.parseLong(data.getId() + ""));
        boolean flag = true;
        try {
            String url = "http://" + shopIp + ":" + shopPort + "/depositCertificate/insertIn";
            log.info("发送到商城url==>{}", url);
            ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(url, one, String.class);
            String body = stringResponseEntity.getBody();
            if (body == null) {
                flag = false;
            }

            // 获取响应code 判断是否接受成功
            JSONObject jsonObject = JSON.parseObject(body);
            Integer status = (Integer) jsonObject.get("status");
            if (status != 1) {
                flag = false;
            }

        } catch (Exception e) {
            flag = false;
            log.error("e", e);
        }

        if (flag) {
            depositCertificate.setIsSend("Y");
            depositCertificate.setSendTime(new Date());
            depositCertificateService.updateById(depositCertificate);
        }
    }

    /**
     * 根据用户手机号发送用户信息到商城
     * @param phone
     */
    @Override
    public void sendUserCompany2ShopAsync(String phone) {
        UserCompanyShopVo1 userCompanyShopVo1 = userCompanyService.sendUserCompany2Shop(phone);

        User user = new User();
        user.setPassword(userCompanyShopVo1.getPassword());
        user.setUsername(userCompanyShopVo1.getNikeName());
        user.setPhone(userCompanyShopVo1.getMobile());
        user.setId(userCompanyShopVo1.getUserId());
        Company company =  new Company();
        company.setId(userCompanyShopVo1.getCompanyId());

        log.info("sendUserCompany2ShopAsync==>{}" ,user);
        log.info("sendUserCompany2ShopAsync==>{}" ,company);
        sendUserCompany2Shop(user,company);
    }

}
