package com.example.service;

import com.example.common.config.WXParamConfig;
import com.example.common.dto.PayOrderDTO;
import com.example.common.dto.PayResultDTO;
import com.example.common.dto.RefundResultDTO;
import com.example.common.dto.WeChatBean;
import com.example.common.vo.ResultVO;
import com.example.model.PayOrder;
import com.example.util.AESUtil;
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
     * 调用微信的接口（H5支付方式）
     * @param payOrderDTO
     * @return
     */
    public ResultVO submitOrderWechatH5(PayOrderDTO payOrderDTO) {
        PayOrder order = null;// 根据业务获取订单
        WeChatBean wechatBean = new WeChatBean()
                .setOpenId(payOrderDTO.getOpenId())
                .setOutTradeNo(order.getOutTradeNo())
                .setSpbillCreateIp(payOrderDTO.getClientIp())
                .setBody(order.getBody())
                .setNotifyUrl(wxParamConfig.payNotifyUrl)
                .setTotalFee(order.getTotalAmount());//如果订单是元，可使用AmountUtil转换

        Map<String, String> result = payChannelAgentService.createPayOrderByWeChatH5(wechatBean);
        return ResultVO.success(result);
    }

    /**
     * 微信支付回调
     * @param params
     * @return
     */
    public String wechatPayNotify(Map<String, String> params) {
        String returnCode = params.get("return_code");
        String resultCode = params.get("result_code");

        if(!WeChatUtil.codeIsOk(returnCode) || !WeChatUtil.codeIsOk(resultCode)){
            return null;
        }

        // 注意此处签名方式需与统一下单的签名类型一致
        try {
            if(!WXPayUtil.isSignatureValid(params, wxParamConfig.getKey(), WXPayConstants.SignType.HMACSHA256)){
                return null;
            }
        }catch (Exception e){
            log.info("[微信支付回调，签名校验失败] :{}", e.getMessage());
            return null;
        }

        String timeEnd = params.get("time_end");//完成时间
        String outTradeNo = params.get("out_trade_no");//订单号
        String transactionId = params.get("transaction_id");//渠道订单号

        // 处理业务逻辑...

        return successResponse();
    }

    /**
     * 微信退款
     * @param payOrderDTO
     * @return
     */
    public ResultVO wechatRefund(PayOrderDTO payOrderDTO) {
        PayOrder order = null;// 根据业务获取订单
        WeChatBean wechatBean = new WeChatBean()
                .setOutTradeNo(order.getOutTradeNo())
                .setNotifyUrl(wxParamConfig.refundNotifyUrl)
                .setTotalFee(order.getTotalAmount());
        ResultVO result = payChannelAgentService.refundByWechat(wechatBean);
        return result;
    }

    /**
     * 微信退款回调
     * @param params
     * @return
     */
    public String wechatRefundNotify(Map<String, String> params) {
        String returnCode = params.get("return_code");
        String returnMsg = params.get("return_msg");

        if(!WeChatUtil.codeIsOk(returnCode)){
            return null;
        }

        //对信息进行解密
        String reqInfo = params.get("req_info");
        String decryptData = AESUtil.decryptData(reqInfo, wxParamConfig.getKey());
        Map<String, String> reqInfoMap = WeChatUtil.xmlToMap(decryptData);

        String successTime = reqInfoMap.get("success_time");//退款时间
        String outTradeNo = reqInfoMap.get("out_trade_no");//订单号
        String transactionId = reqInfoMap.get("transaction_id");//渠道订单号

        // 处理业务逻辑...

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

    /**
     * 查询订单支付状态
     * @param payOrderDTO
     */
    public ResultVO wechatPayQuery(PayOrderDTO payOrderDTO) {
        PayOrder order = null;// 根据业务获取订单
        PayResultDTO result = payChannelAgentService.queryPayByWeChat(order.getOutTradeNo());
        return ResultVO.success();
    }

    /**
     * 查询订单退款结果
     * @param payOrderDTO
     */
    public ResultVO wechatRefundQuery(PayOrderDTO payOrderDTO) {
        PayOrder order = null;// 根据业务获取订单
        RefundResultDTO result = payChannelAgentService.queryRefundByWeChat(order.getOutTradeNo());
        return ResultVO.success();
    }
}
