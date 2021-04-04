package com.example.service;


import com.example.common.config.WXParamConfig;
import com.example.common.config.WXSDKConfig;
import com.example.common.dto.PayResultDTO;
import com.example.common.dto.RefundResultDTO;
import com.example.common.dto.WeChatBean;
import com.example.common.enums.RefundStatus;
import com.example.common.enums.TradeStatus;
import com.example.common.vo.ResultVO;
import com.github.wxpay.sdk.WXPay;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 与第三支付渠道进行交互
 */
@Slf4j
@Service
public class PayChannelAgentService{

    @Autowired
    private WXParamConfig wxParamConfig;

    /**
     * 微信下单接口（JSAPI方式）
     * @param weChatBean 订单业务数据
     * @return h5网页的数据
     */
    public Map<String, String> createPayOrderByWeChatJSAPI(WeChatBean weChatBean) {
        WXSDKConfig config = new WXSDKConfig(wxParamConfig);
        try {
            WXPay wxPay = new WXPay(config);
            Map<String, String> params = new HashMap<>();
            params.put("out_trade_no", weChatBean.getOutTradeNo());//订单号
            params.put("body", weChatBean.getBody());//订单描述
            params.put("fee_type", "CNY");//人民币
            params.put("total_fee", String.valueOf(weChatBean.getTotalFee()));//金额
            params.put("spbill_create_ip", weChatBean.getSpbillCreateIp());//客户端ip
            params.put("notify_url", weChatBean.getNotifyUrl());
            params.put("trade_type", "JSAPI");
            params.put("openid", weChatBean.getOpenId());

            //调用下单接口，并处理数据
            Map<String, String> resp = wxPay.unifiedOrder(params);
            String returnCode = resp.get("return_code");
            String resultCode = resp.get("result_code");
            if (!"SUCCESS".equals(resultCode) || !"SUCCESS".equals(returnCode)) {
                String errCodeDes = resp.get("err_code_des");
                throw new RuntimeException(errCodeDes);
            }

            //微信H5参数唤醒支付
            Map<String, String> h5Params = new HashMap<>();
            h5Params.put("appId", wxParamConfig.getAppId());
            h5Params.put("timeStamp", System.currentTimeMillis() / 1000 + "");
            h5Params.put("nonceStr", UUID.randomUUID().toString());//随机字符串
            h5Params.put("package", "prepay_id=" + resp.get("prepay_id"));
            h5Params.put("signType", "HMAC-SHA256");
            h5Params.put("paySign", WXPayUtil.generateSignature(h5Params, wxParamConfig.getKey(), WXPayConstants.SignType.HMACSHA256));
            return h5Params;
        } catch (Exception e) {
            throw new RuntimeException("微信支付失败 :" + e.getMessage());
        }
    }

    /**
     * 微信下单接口（H5方式）
     * @param weChatBean 订单业务数据
     * @return
     */
    public Map<String, String> createPayOrderByWeChatH5(WeChatBean weChatBean) {
        WXSDKConfig config = new WXSDKConfig(wxParamConfig);
        try {
            //创建SDK客户端
            WXPay wxPay = new WXPay(config);
            Map<String, String> params = new HashMap<>();
            params.put("out_trade_no", weChatBean.getOutTradeNo()); //订单号
            params.put("body", weChatBean.getBody()); //订单描述
            params.put("fee_type", "CNY"); //人民币
            params.put("total_fee", String.valueOf(weChatBean.getTotalFee()));
            params.put("spbill_create_ip", weChatBean.getSpbillCreateIp());
            params.put("notify_url", weChatBean.getNotifyUrl());
            params.put("trade_type", "MWEB");
            params.put("scene_info", "{'h5_info':{'type':'Wap','wap_url':'www.xxxx.com','wap_name': 'xxxx'}}");
            Map<String, String> resp = wxPay.unifiedOrder(params);

            String returnCode = resp.get("return_code");
            String resultCode = resp.get("result_code");
            String mWebUrl = resp.get("mweb_url");

            if (!"SUCCESS".equals(resultCode) || !"SUCCESS".equals(returnCode)) {
                String errCodeDes = resp.get("err_code_des");
                throw new RuntimeException(errCodeDes);
            }
            Map<String, String> result = new HashMap<>();
            String redirectUrl = URLEncoder.encode("http://点击完成支付后跳转页面", "utf-8");
            result.put("mWebUrl", mWebUrl + "&redirect_url=" + redirectUrl);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("微信支付失败 :" + e.getMessage());
        }
    }

    /**
     * 微信退款申请
     * @param weChatBean
     * @return
     */
    public ResultVO refundByWechat(WeChatBean weChatBean) {
        WXSDKConfig config = new WXSDKConfig(wxParamConfig);

        try {
            WXPay wxPay = new WXPay(config);
            Map<String, String> params = new HashMap<>();
            params.put("out_trade_no", weChatBean.getOutTradeNo());
            params.put("out_refund_no", weChatBean.getOutTradeNo());
            params.put("total_fee", String.valueOf(weChatBean.getTotalFee()));
            params.put("refund_fee", String.valueOf(weChatBean.getTotalFee()));
            params.put("refund_fee_type", "CNY");
            params.put("notify_url", weChatBean.getNotifyUrl());

            //调用退款接口，并处理数据
            Map<String, String> resp = wxPay.refund(params);
            String returnCode = resp.get("return_code");
            String resultCode = resp.get("result_code");
            String returnMsg = resp.get("return_msg");
            if (!"SUCCESS".equals(returnCode) || !"SUCCESS".equals(resultCode)) {
                throw new RuntimeException(returnMsg);
            }
            return ResultVO.success();
        } catch (Exception e) {
            throw new RuntimeException("微信退款失败 :" + e.getMessage());
        }
    }

    /**
     * 查询微信订单状态
     * @param outTradeNo 闪聚平台的订单号
     * @return
     */
    public PayResultDTO queryPayByWeChat(String outTradeNo) {
        WXSDKConfig config = new WXSDKConfig(wxParamConfig);
        Map<String, String> result = null;

        try {
            WXPay wxPay = new WXPay(config);
            Map<String, String> params = new HashMap<>();
            params.put("out_trade_no", outTradeNo);
            result = wxPay.orderQuery(params);  //调用微信的退款查询接口
        } catch (Exception e) {
            return PayResultDTO.fail(outTradeNo, "调用微信订单查询接口失败", TradeStatus.QUERYERROR);
        }

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        String resultCode = result.get("result_code");
        String tradeState = result.get("trade_state");
        String transactionId = result.get("transaction_id");
        String timeEnd = result.get("time_end");

        if (!"SUCCESS".equals(returnCode) || !"SUCCESS".equals(resultCode)) {
            return PayResultDTO.fail(outTradeNo, returnMsg, TradeStatus.UNKNOWN);
        }

        switch (tradeState) {
            case "SUCCESS"://支付成功
                return PayResultDTO.success(outTradeNo, transactionId, TradeStatus.SUCCESS)
                        .setSuccessTime(timeEnd);
            case "PAYERROR"://支付失败
                return PayResultDTO.success(outTradeNo, transactionId, TradeStatus.FAILED);
            case "REFUND"://转入退款
                return PayResultDTO.success(outTradeNo, transactionId, TradeStatus.REFUND);
            case "CLOSED"://交易关闭
                return PayResultDTO.success(outTradeNo, transactionId, TradeStatus.CLOSED);
            case "REVOKED"://交易撤销
                return PayResultDTO.success(outTradeNo, transactionId, TradeStatus.REVOKED);
            case "ACCEPT"://等待扣款
                return PayResultDTO.success(outTradeNo, transactionId, TradeStatus.ACCEPT);
            case "NOTPAY"://未支付
                return PayResultDTO.success(outTradeNo, transactionId, TradeStatus.NOTPAY);
            case "USERPAYING"://支付中
                return PayResultDTO.success(outTradeNo, transactionId, TradeStatus.USERPAYING);
        }
        return PayResultDTO.success(outTradeNo, transactionId, TradeStatus.UNKNOWN);
    }

    /**
     * 查询退款订单信息
     * @param outTradeNo
     * @return
     */
    public RefundResultDTO queryRefundByWeChat(String outTradeNo) {
        WXSDKConfig config = new WXSDKConfig(wxParamConfig);

        Map<String, String> result = null;
        try {
            //创建SDK客户端
            WXPay wxPay = new WXPay(config);
            Map<String, String> params = new HashMap<>();
            params.put("out_trade_no", outTradeNo); //订单号
            result = wxPay.refundQuery(params);//调用微信的退款查询接口

        } catch (Exception e) {
            return RefundResultDTO.fail("调用微信订单查询接口失败", outTradeNo, RefundStatus.QUERYERROR);
        }

        String returnCode = result.get("return_code");
        String returnMsg = result.get("return_msg");
        String resultCode = result.get("result_code");
        String tradeState = result.get("refund_status_0");
        String refundSuccessTime = result.get("refund_success_time_0");

        if (!"SUCCESS".equals(returnCode) || !"SUCCESS".equals(resultCode)) {
            return RefundResultDTO.fail(outTradeNo, returnMsg, RefundStatus.UNKNOWN);
        }

        switch (tradeState) {
            case "SUCCESS": //退款成功
                return RefundResultDTO.success(outTradeNo, RefundStatus.SUCCESS, returnMsg)
                        .setSuccessTime(refundSuccessTime);
            case "REFUNDCLOSE"://退款关闭
                return RefundResultDTO.success(outTradeNo, RefundStatus.REFUNDCLOSE, returnMsg)
                        .setMsg(returnMsg);
            case "CHANGE"://退款异常
                return RefundResultDTO.success(outTradeNo, RefundStatus.CHANGE, returnMsg)
                        .setMsg(returnMsg);
            case "PROCESSING"://退款处理中
                return RefundResultDTO.success(outTradeNo, RefundStatus.PROCESSING, returnMsg);
        }
        return RefundResultDTO.success(outTradeNo, RefundStatus.UNKNOWN, returnMsg);
    }
}
