package com.example.controller;

import com.example.common.dto.PayOrderDTO;
import com.example.common.vo.ResultVO;
import com.example.service.TransactionService;
import com.example.util.IPUtil;
import com.example.util.WeChatUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @Author: Killah
 * @Version: 1.0
 */
@Slf4j
@RestController
@RequestMapping("/weChat/pay")
public class WeChatPayController {

    @Autowired
    private TransactionService transactionService;

    /**
     * 微信JSAPI付款
     * @param payOrderDTO
     * @param request
     * @return
     */
    @PostMapping("/jsapi")
    public ResultVO wechatPayByJsapi(@RequestBody PayOrderDTO payOrderDTO, HttpServletRequest request) {
        payOrderDTO.setClientIp(IPUtil.getIpAddr(request));
        return transactionService.submitOrderWechatJsapi(payOrderDTO);
    }

    /**
     * 微信H5付款
     * @param payOrderDTO
     * @param request
     * @return
     */
    @PostMapping("/h5")
    public ResultVO wechatPayByH5(@RequestBody PayOrderDTO payOrderDTO, HttpServletRequest request) {
        payOrderDTO.setClientIp(IPUtil.getIpAddr(request));
        return transactionService.submitOrderWechatH5(payOrderDTO);
    }

    /**
     * 微信支付回调
     * @param request
     * @return
     */
    @RequestMapping("/payNotify")
    public String wechatPayNotify(HttpServletRequest request) {
        String xmlMsg = WeChatUtil.readData(request);
        Map<String, String> params = WeChatUtil.xmlToMap(xmlMsg);
        return transactionService.wechatPayNotify(params);
    }

    /**
     * 微信退款
     * @param payOrderDTO
     * @return
     */
    @RequestMapping("/refund")
    public ResultVO wechatRefund(@RequestBody PayOrderDTO payOrderDTO) {
        return transactionService.wechatRefund(payOrderDTO);
    }

    /**
     * 微信退款回调
     * @param request
     * @return
     */
    @RequestMapping("/refundNotify")
    public String wechatRefundNotify(HttpServletRequest request) {
        String xmlMsg = WeChatUtil.readData(request);
        Map<String, String> params = WeChatUtil.xmlToMap(xmlMsg);
        return transactionService.wechatRefundNotify(params);
    }

    /**
     * 查看订单支付状态
     * @param payOrderDTO
     * @return
     */
    @GetMapping("/payQuery")
    public ResultVO payQuery(@RequestBody PayOrderDTO payOrderDTO) {
        return transactionService.wechatPayQuery(payOrderDTO);
    }

    /**
     * 查看订单退款状态
     * @param payOrderDTO
     * @return
     */
    @GetMapping("/refundQuery")
    public ResultVO refundQuery(@RequestBody PayOrderDTO payOrderDTO) {
        return transactionService.wechatRefundQuery(payOrderDTO);
    }
}
