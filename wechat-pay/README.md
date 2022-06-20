# WeChat-Pay 微信支付相关参考
[![](https://img.shields.io/badge/Author-Killah-orange.svg)](https://github.com/killahs)
[![](https://img.shields.io/badge/version-1.0.0-brightgreen.svg)](https://github.com/killahs/killah-plus-full/tree/main/wechat-pay)
[![GitHub stars](https://img.shields.io/github/stars/Killahs/killah-plus-full.svg?style=social&label=Stars)](https://github.com/killahs/killah-plus-full)
[![GitHub forks](https://img.shields.io/github/forks/Killahs/killah-plus-full.svg?style=social&label=Fork)](https://github.com/killahs/killah-plus-full)

WeChat-Pay，主要是通过微信SDK，不依赖太多第三方JAR，完成支付开发的一个展示。仅仅作为例子使用简单快速完成支付模块的开发，可轻松应用到任何系统里。
微信支付支持多商户多应用，普通商户模式与服务商商模式当然也支持境外商户、同时支持Api-v2 版本的接口。

## 微信JSAPI支付方式
JSAPI支付方式，是用户在微信浏览器（微信公众号、朋友圈、聊天会话、微信扫一扫）中打开商户的HTML5页面，商户在H5页面通过调用微信支付提供的JSAPI接口调起微信支付模块完成支付。

### 业务流程、后台组装参数逻辑
- 用户通过不同场景点击进入商户网页，选择商品购买，完成选购流程
- 后台使用网页授权获取用户基本信息，得到 **openid**
- 从前台获取到用户提交的订单参数，生成第一次签名
- 调用微信统一下单接口URL，生成预支付id：**prepayId**
- 对 prepayId 以及一些其他参数进行二次签名
- 将所需的数据传给前台微信浏览器，H5页面唤醒微信支付
- 微信将支付通知给后台（notify_url）
- 后台执行回调操作，完成整个支付流程

### 前端调起支付
```js
//微信浏览器HTML5页面唤起微信支付
WeixinJSBridge.invoke(
    'getBrandWCPayRequest', {
        "appId": "{{appId}}",           //appid
        "timeStamp": "{{timeStamp}}",   //时间戳，自1970年以来的秒数
        "nonceStr": "{{nonceStr}}",     //随机串
        "package": "{{packages}}",      //prepayId信息
        "signType": "{{signType}}",     //微信签名方式：
        "paySign": "{{paySign}}"        //微信签名
    },
    function(res){
        if(res.err_msg == "get_brand_wcpay_request:ok" ) {
            //使用以上方式判断前端返回,微信团队郑重提示：res.err_msg将在用户支付成功后返回 ok，但并不保证它绝对可靠。
            alert("支付成功");
        }else if (res.err_msg === 'get_brand_wcpay_request:cancel') {
            alert("取消支付");
        }else{
            //支付失败
            alert(res.err_msg)
        }
    }
); 
```

## 微信H5支付方式
H5支付，是指商户在微信客户端外的移动端网页（如QQ浏览器、谷歌浏览器、Safari等）展示商品或服务，使用微信支付的场景。
简单来说，就是在微信外的所有浏览器来点击“微信支付”然后自动唤起微信客户端来支付。

### 接口流程图
- 用户在微信浏览器外的H5商户页面，使用微信支付进行支付。
- 后台向微信支付发起调用统一下单接口，与JSAPI不同的是：交易类型**trade_type=MWEB**。
- 统一下单接口返回支付相关参数给商户后台，如跳转支付url **mweb_url**，商户通过mweb_url调起微信支付中间页。
- 如支付成功，商户后台会接收到微信侧的异步通知回调。
- 用户在微信支付完成支付或取消支付,返回商户页面（默认为返回支付发起页面）
- 商户在展示页面，引导用户主动发起支付结果的查询

### 回调页面
正常流程用户支付完成后会返回至发起支付的页面，如需返回至指定页面，则可以在MWEB_URL后拼接上redirect_url参数，来指定回调页面。
> https://wx.tenpay.com/cgi-bin/mmpayweb-bin/checkmweb?prepay_id=wx20161110163838f231619da20804912345&package=1037687096&redirect_url=xxxxxxx


## 参考资料：
- **支付接口文档**：https://pay.weixin.qq.com/wiki/doc/api/index.html

