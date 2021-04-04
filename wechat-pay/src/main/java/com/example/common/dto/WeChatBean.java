package com.example.common.dto;

import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 微信支付接口参数
 */
@Data
@Accessors(chain = true)
public class WeChatBean implements Serializable {

    /**
     * 公众号
     */
    private String appid;

    /**
     * 商户号
     */
    private String mchId;

    /**
     * 随机字符串
     */
    private String nonceStr;

    /**
     * 签名
     */
    private String sign;

    /**
     * 商品描述
     */
    private String body;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 标价金额
     */
    private Integer totalFee;

    /**
     * 终端IP
     */
    private String spbillCreateIp;

    /**
     * 通知地址
     */
    private String notifyUrl;

    /**
     * 交易类型
     */
    private String tradeType;

    /**
     * openID
     */
    private String openId;

}
