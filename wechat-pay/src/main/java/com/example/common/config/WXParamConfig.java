package com.example.common.config;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.context.annotation.Configuration;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 微信配置参数
 */
@Data
@Configuration
@Accessors(chain = true)
@ConfigurationProperties(prefix = "wechat")
public class WXParamConfig {

    /**
     * 微信AppId
     */
    private String appId;

    /**
     * AppId对应的接口密码
     */
    private String appSecret;

    /**
     * 商户Id
     */
    private String mchId;

    /**
     * 商户Key
     */
    private String key;

    /**
     * 证书路径
     */
    public String certPath;

    /**
     * 支付通知回调地址
     */
    public String payNotifyUrl;

    /**
     * 退款通知回调地址
     */
    public String refundNotifyUrl;

}
