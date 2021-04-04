package com.example.common.config;

import com.github.wxpay.sdk.IWXPayDomain;
import com.github.wxpay.sdk.WXPayConfig;
import com.github.wxpay.sdk.WXPayConstants;
import org.springframework.util.Assert;

import java.io.*;

/**
 * @Author: Killah
 * @Version: 1.0
 * @Description: 微信支付参数
 */
public class WXSDKConfig extends WXPayConfig {

    /**
     * 证书字节码
     */
    private byte[] certData;

    /**
     * 支付参数
     */
    private final WXParamConfig config;

    /**
     * 配置微信SDK
     * @param config
     */
    public WXSDKConfig(WXParamConfig config) {
        Assert.notNull(config, "微信参数配置不能为空");
        this.config = config;
    }

    @Override
    public String getAppID() {
        return config.getAppId();
    }


    @Override
    public String getMchID() {
        return config.getMchId();
    }

    @Override
    public String getKey() {
        return config.getKey();
    }

    @Override
    public InputStream getCertStream() {
        // 读取证书
        readCertPath(this.config.certPath);
        return new ByteArrayInputStream(this.certData);
    }

    @Override
    public int getHttpConnectTimeoutMs() {
        return 8000;
    }

    @Override
    public int getHttpReadTimeoutMs() {
        return 10000;
    }

    @Override
    public IWXPayDomain getWXPayDomain() {
        return new IWXPayDomain() {
            @Override
            public void report(String s, long l, Exception e) {

            }

            @Override
            public DomainInfo getDomain(WXPayConfig wxPayConfig) {//api.mch.weixin.qq.com
                return new DomainInfo(WXPayConstants.DOMAIN_API, true);
            }
        };
    }

    /**
     * 获取证书字节码
     * @param certPath
     */
    public void readCertPath(String certPath) {
        InputStream certStream = null;
        try {
            File file = new File(certPath);
            certStream = new FileInputStream(file);
            this.certData = new byte[(int) file.length()];
            certStream.read(this.certData);
            certStream.close();
        }catch (IOException e){
            throw new RuntimeException("读取证书异常");
        }finally {
            if(certStream != null){
                try {
                    certStream.close();
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        }
    }
}
