package com.example.service;


import com.example.common.config.WXParamConfig;
import com.example.common.config.WXSDKConfig;
import com.example.common.dto.WeChatBean;
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
     * 微信下单接口
     *
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

}
