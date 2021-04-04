package com.example.common.dto;

import com.example.common.enums.TradeStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 支付响应信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PayResultDTO<T> implements Serializable {

    /**
     * 状态码，0:成功
     */
    private String code;

    /**
     * 描述
     */
    private String msg;

    /**
     * 原始渠道订单号
     */
    private String tradeNo;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 状态
     */
    private TradeStatus tradeState;

    /**
     * 支付完成时间
     */
    private String successTime;


    public static <T> PayResultDTO<T> fail(String outTradeNo, String msg, TradeStatus tradeState) {
        PayResultDTO<T> response = new PayResultDTO<T>();
        response.setCode("999999");
        response.setMsg(msg);
        response.setOutTradeNo(outTradeNo);
        response.setTradeState(tradeState);
        return response;
    }

    public static <T> PayResultDTO<T> success(String outTradeNo, String tradeNo, TradeStatus tradeState) {
        PayResultDTO<T> response = new PayResultDTO<T>();
        switch (tradeState) {
            case SUCCESS:
                response.setMsg("支付成功");
                break;
            case FAILED:
                response.setMsg("支付失败");
                break;
            case REFUND:
                response.setMsg("交易转入退款");
                break;
            case NOTPAY:
                response.setMsg("交易未支付");
                break;
            case CLOSED:
                response.setMsg("交易已关闭");
                break;
            case REVOKED:
                response.setMsg("交易已撤销");
                break;
            case USERPAYING:
                response.setMsg("交易等待支付");
                break;
            case ACCEPT:
                response.setMsg("已接收，等待扣款");
                break;
            case UNKNOWN:
                response.setMsg("状态未知");
                break;
        }
        response.setTradeNo(tradeNo);
        response.setOutTradeNo(outTradeNo);
        response.setTradeState(tradeState);
        return response;
    }

}
