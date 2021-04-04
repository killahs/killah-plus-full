package com.example.common.enums;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 交易状态信息
 */
public enum TradeStatus {

    SUCCESS,    // 支付成功
    FAILED,     // 支付失败
    REFUND,     // 转入退款
    CLOSED,     // 已关闭
    REVOKED,    // 已撤销
    USERPAYING, // 支付中
    NOTPAY,     // 未支付
    ACCEPT,     // 已接收，等待扣款

    QUERYERROR, // 查询失败
    UNKNOWN,    // 状态未知
}
