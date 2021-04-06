# WeChat-Pay 微信支付相关参考
- 该项目基于微信支付apiv2的接口，详细资料请参考官方文档。
- 项目中接口以及工具类均可跑起来，并能实现支付退款功能。
- 该部分目前只包含后端部分，前端部分需要自己查阅资料。

## 微信JSAPI支付方式
JSAPI支付方式，是用户在微信浏览器（微信公众号、朋友圈、聊天会话、微信扫一扫）中打开商户的HTML5页面，商户在H5页面通过调用微信支付提供的JSAPI接口调起微信支付模块完成支付。

### 业务流程、后台组装参数逻辑
- 用户通过不同场景点击进入商户网页，选择商品购买，完成选购流程。
- 后台使用网页授权获取用户基本信息，得到openid。 
- 从前台获取到用户提交的订单参数，生成第一次签名。
- 调用微信统一下单接口URL，生成预支付id："prepayId"。
- 对 prepayId 以及一些其他参数进行二次签名。
- 将所需的数据传给前台微信浏览器，H5页面唤醒微信支付。
- 微信将支付通知给后台（notify_url）。
- 后台执行回调操作，完成整个支付流程。

### 前端调起支付
```html
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

## 参考资料：
- **支付接口文档**：https://pay.weixin.qq.com/wiki/doc/api/index.html

