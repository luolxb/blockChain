package me.zhengjie.modules.system.sms.entity;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SendSmsEntity {

    /**
     * 用户的账号唯一标识“Account Sid”，在开发者控制台获取
     */
    private String sid;

    /**
     * 用户密钥“Auth Token”，在开发者控制台获取
     */
    private String token;

    /**
     * 创建应用时系统分配的唯一标示
     */
    private String appid;

    /**
     * 可在后台短信产品→选择接入的应用→短信模板-模板ID，查看该模板ID
     */
    private String templateid;

    /**
     * 模板中的替换参数，如该模板不存在参数则无需传该参数或者参数为空，如果有多个参数则需要写在同一个字符串中，以英文逗号分隔 （如：“a,b,c”），参数中不能含有特殊符号“【】”和“,”
     */
    private String param;

    /**
     * 接收的单个手机号
     */
    private String mobile;

    /**
     * 用户透传ID，随状态报告返回
     */
    private String uid;


}
