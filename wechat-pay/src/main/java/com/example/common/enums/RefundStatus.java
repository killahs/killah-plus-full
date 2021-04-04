package com.example.common.enums;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 退款状态信息
 */
public enum RefundStatus {

    SUCCESS,        // 退款成功
    FAILED,         // 退款失败
    REFUNDCLOSE,    // 退款关闭
    PROCESSING,     // 退款处理中
    CHANGE,         // 退款异常（退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败）
    NOTREFUND,      // 未退款

    QUERYERROR,     // 查询失败
    UNKNOWN,        // 状态未知

}
