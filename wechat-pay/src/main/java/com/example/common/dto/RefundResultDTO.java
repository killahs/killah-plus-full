package com.example.common.dto;

import com.example.common.enums.RefundStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.io.Serializable;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 退款响应信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RefundResultDTO<T> implements Serializable {

    /**
     * 状态码, 0:成功
     */
    private String code = "0";

    /**
     * 描述
     */
    private String msg;

    /**
     * 商户订单号
     */
    private String outTradeNo;

    /**
     * 状态
     */
    private RefundStatus refundStatus;

    /**
     * 退款成功时间
     */
    private String successTime;


    public static <T> RefundResultDTO<T> fail(String outTradeNo, String msg, RefundStatus refundStatus) {
        RefundResultDTO<T> response = new RefundResultDTO<T>();
        response.setCode("999999");
        response.setMsg(msg);
        response.setOutTradeNo(outTradeNo);
        response.setRefundStatus(refundStatus);
        return response;
    }

    public static <T> RefundResultDTO<T> success(String outTradeNo, RefundStatus refundStatus, String msg) {
        RefundResultDTO<T> response = new RefundResultDTO<T>();
        switch (refundStatus) {
            case SUCCESS:
                response.setMsg("退款成功");
                break;
            case FAILED:
                response.setMsg("退款失败: " + msg);
                break;
            case REFUNDCLOSE:
                response.setMsg("退款关闭: " + msg);
                break;
            case PROCESSING:
                response.setMsg("退款处理中: " + msg);
                break;
            case CHANGE:
                response.setMsg("退款异常(退款到银行发现用户的卡作废或者冻结了，导致原路退款银行卡失败): " + msg);
                break;
            case UNKNOWN:
                response.setMsg("状态未知");
                break;
        }
        response.setOutTradeNo(outTradeNo);
        response.setRefundStatus(refundStatus);
        return response;
    }

}
