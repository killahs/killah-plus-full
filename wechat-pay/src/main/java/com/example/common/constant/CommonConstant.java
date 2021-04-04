package com.example.common.constant;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 常量
 */
public interface CommonConstant {

    Integer SUCCESS_CODE = 200;
    Integer ERROR_CODE = 400;

    String PAY_PAY_NOTIFY_XML_DEMO = "<xml><appid><![CDATA[wxb852a3f35ee64492]]></appid>\n" +
            "<bank_type><![CDATA[OTHERS]]></bank_type>\n" +
            "<cash_fee><![CDATA[1]]></cash_fee>\n" +
            "<fee_type><![CDATA[CNY]]></fee_type>\n" +
            "<is_subscribe><![CDATA[Y]]></is_subscribe>\n" +
            "<mch_id><![CDATA[1601167911]]></mch_id>\n" +
            "<nonce_str><![CDATA[LfF5n5AqN47AbBnmHB65WPdmgLsGivu0]]></nonce_str>\n" +
            "<openid><![CDATA[ool5F68r8qMpKn6gQppdoFjoSb28]]></openid>\n" +
            "<out_trade_no><![CDATA[WxPay1375340944954826754]]></out_trade_no>\n" +
            "<result_code><![CDATA[SUCCESS]]></result_code>\n" +
            "<return_code><![CDATA[SUCCESS]]></return_code>\n" +
            "<sign><![CDATA[FFF49E3368E244BA50B7F4C3C8938C87F744612A713A1AE7C02D637469CAEC8C]]></sign>\n" +
            "<time_end><![CDATA[20210326145653]]></time_end>\n" +
            "<total_fee>1</total_fee>\n" +
            "<trade_type><![CDATA[JSAPI]]></trade_type>\n" +
            "<transaction_id><![CDATA[4200000939202103261870690700]]></transaction_id>\n" +
            "</xml>";
}
