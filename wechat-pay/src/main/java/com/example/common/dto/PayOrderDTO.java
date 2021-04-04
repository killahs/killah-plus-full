package com.example.common.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * @Author: Killah
 * @Describe: 下单请求对象
 */
@Data
public class PayOrderDTO implements Serializable {

    /**
     * 商户id
     */
    private Long orderId;

    /**
     * c端付款用户身份标识
     */
    private String openId;

    /**
     * 客户端IP
     */
    private String clientIp;


}
