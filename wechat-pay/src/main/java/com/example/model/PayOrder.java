package com.example.model;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PayOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    private Long id;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 原始渠道订单号
     */
    private String payChannelTradeNo;

    /**
     * 支付渠道
     */
    private String payChannel;

    /**
     * 订单描述
     */
    private String body;

    /**
     * 币种CNY
     */
    private String currency;

    /**
     * 订单总金额，单位为分
     */
    private Integer totalAmount;

    /**
     * 客户端IP
     */
    private String clientIp;

    /**
     * 交易状态支付状态：0-订单生成, 1-支付中(目前未使用), 2-支付成功, 3-业务处理完成, 4-关闭
     */
    private String tradeState;

    /**
     * 渠道支付错误码
     */
    private String errorCode;

    /**
     * 渠道支付错误信息
     */
    private String errorMsg;

    /**
     * 订单过期时间
     */
    private LocalDateTime expireTime;

    /**
     * 支付成功时间
     */
    private LocalDateTime successTime;

}
