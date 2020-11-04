package me.zhengjie.modules.system.sms.service;

import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import me.zhengjie.exception.BadRequestException;
import me.zhengjie.modules.system.sms.entity.SendSmsEntity;
import me.zhengjie.utils.NtsUtil;
import me.zhengjie.utils.RedisKey;
import me.zhengjie.utils.RedisUtils;
import me.zhengjie.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@Slf4j
public class SendSmsServiceImpl {

    @Value("${sms.send.url}")
    private String sendUrl;

    @Value("${sms.send.templateid}")
    private String templateid;

    @Value("${sms.sid}")
    private String sid;

    @Value("${sms.token}")
    private String token;

    @Value("${sms.appid}")
    private String appid;

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private RedisUtils redisUtils;


    /**
     * 获取短信验证码
     *
     * @param mobile
     * @return
     */
    public String getCode(String mobile) {
        // 验证手机号是否正确
        if (!NtsUtil.isPhone(mobile)) {
            throw new BadRequestException("手机号不正确");
        }
        // 删除之前的验证码
        redisUtils.del(RedisKey.SMS_SEND + mobile);

        // 获取随机验证码
        String code = NtsUtil.generateCode2(6);
        // 验证码有效时间 5分钟
        int time = 5;
        String param = code + "," + time;
        SendSmsEntity sendSmsEntity = new SendSmsEntity();
        sendSmsEntity.setAppid(appid);
        sendSmsEntity.setMobile(mobile);
        sendSmsEntity.setTemplateid(templateid);
        sendSmsEntity.setSid(sid);
        sendSmsEntity.setToken(token);
        sendSmsEntity.setParam(param);

        // 发送
        ResponseEntity<String> stringResponseEntity = restTemplate.postForEntity(sendUrl, sendSmsEntity, String.class);
        String body = stringResponseEntity.getBody();
        if (null == body) {
            log.error("获取验证码异常=>{}", stringResponseEntity);
            throw new BadRequestException("获取验证码失败");
        }

        JSONObject jsonObject = JSONObject.parseObject(body);
        String resCode = (String) jsonObject.get("code");
        if (!StringUtils.equals(resCode, "000000")) {
            log.error("获取验证码异常=>{}", body);
            throw new BadRequestException("获取验证码失败");
        }
        // 加入到redis  5分钟过时
        redisUtils.set(RedisKey.SMS_SEND + mobile, code, 60 * time);
        return code;
    }
}
