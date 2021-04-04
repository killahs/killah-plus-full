package com.example.service;

import com.example.common.config.WXParamConfig;
import com.example.common.dto.PayOrderDTO;
import com.example.common.dto.WeChatBean;
import com.example.common.vo.ResultVO;
import com.example.model.PayOrder;
import com.example.util.WeChatUtil;
import com.github.wxpay.sdk.WXPayConstants;
import com.github.wxpay.sdk.WXPayUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;


/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 与第三支付渠道进行交互实现
 */
@Slf4j
@Service
public class TransactionService {

    @Autowired
    private WXParamConfig wxParamConfig;

    @Autowired
    private PayChannelAgentService payChannelAgentService;

    /**
     * 调用微信的接口（JSAPI支付方式）
     * @param payOrderDTO
     * @return h5页面所需要的数据
     */
    public ResultVO submitOrderWechatJsapi(PayOrderDTO payOrderDTO) {
        PayOrder order = null;// 根据业务获取订单
        WeChatBean wechatBean = new WeChatBean()
                .setOpenId(payOrderDTO.getOpenId())
                .setOutTradeNo(order.getOutTradeNo())
                .setSpbillCreateIp(payOrderDTO.getClientIp())
                .setBody(order.getBody())
                .setNotifyUrl(wxParamConfig.payNotifyUrl)
                .setTotalFee(order.getTotalAmount());//如果订单是元，可使用AmountUtil转换
        Map<String, String> payResult = payChannelAgentService.createPayOrderByWeChatJSAPI(wechatBean);
        return ResultVO.success(payResult);
    }

    /**
     * 微信支付回调
     * @param params
     * @return
     */
    public String wechatPayNotify(Map<String, String> params) {
        String returnCode = params.get("return_code");
        String resultCode = params.get("result_code");

        // 注意此处签名方式需与统一下单的签名类型一致
        try {
            if(!WXPayUtil.isSignatureValid(params, wxParamConfig.getKey(), WXPayConstants.SignType.HMACSHA256)){
                return null;
            }
        }catch (Exception e){
            log.info("[微信支付回调，签名校验失败] :{}", e.getMessage());
            return null;
        }

        // IPAY API
        //if (!WxPayKit.verifyNotify(params, wxParamConfig.getKey(), SignType.HMACSHA256)) {
            //return null;
        //}

        String timeEnd = params.get("time_end");
        String outTradeNo = params.get("out_trade_no");
        String transactionId = params.get("transaction_id");
        // 处理业务逻辑

        return successResponse();
    }

    /**
     * 商户处理通知参数后同步返回给微信参数
     * @return
     */
    public String successResponse(){
        Map<String, String> xmlResult = new HashMap<>(2);
        xmlResult.put("return_code", "SUCCESS");
        xmlResult.put("return_msg", "OK");
        return WeChatUtil.toXml(xmlResult);
    }
}
