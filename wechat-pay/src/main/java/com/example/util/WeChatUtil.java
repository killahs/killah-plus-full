package com.example.util;

import cn.hutool.core.util.StrUtil;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 微信支付相关工具类
 */
public class WeChatUtil {

    /**
     * 获取请求的流
     *
     * @param request
     * @return
     */
    public static String readData(HttpServletRequest request) {
        BufferedReader br = null;
        try {
            StringBuilder result = new StringBuilder();

            String line;
            for (br = request.getReader(); (line = br.readLine()) != null; result.append(line)) {
                if (result.length() > 0) {
                    result.append("\n");
                }
            }

            line = result.toString();
            return line;
        } catch (IOException var12) {
            throw new RuntimeException(var12);
        } finally {
            if (br != null) {
                try {
                    br.close();
                } catch (IOException var11) {
                    var11.printStackTrace();
                }
            }
        }
    }

    /**
     * XML字符串转换为MAP格式
     *
     * @param xmlStr
     * @return
     */
    public static Map<String, String> xmlToMap(String xmlStr) {
        XmlHelper xmlHelper = XmlHelper.of(xmlStr);
        return xmlHelper.toMap();
    }


    /**
     * MAP格式转换为XML字符串
     *
     * @param params
     * @return
     */
    public static String toXml(Map<String, String> params) {
        StringBuffer xml = forEachMap(params, "<xml>", "</xml>");
        return xml.toString();
    }

    public static StringBuffer forEachMap(Map<String, String> params, String prefix, String suffix) {
        StringBuffer xml = new StringBuffer();
        if (StrUtil.isNotEmpty(prefix)) {
            xml.append(prefix);
        }
        Iterator var4 = params.entrySet().iterator();
        while (var4.hasNext()) {
            Map.Entry<String, String> entry = (Map.Entry) var4.next();
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            if (!StrUtil.isEmpty(value)) {
                xml.append("<").append(key).append(">");
                xml.append((String) entry.getValue());
                xml.append("</").append(key).append(">");
            }
        }
        if (StrUtil.isNotEmpty(suffix)) {
            xml.append(suffix);
        }
        return xml;
    }
}
