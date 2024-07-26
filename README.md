## 一、调用入口

调用地址：http://xxxxx/router ，所有请求都为POST请求。

## 二、公共参数

调用任何一个API都必须传入的参数，目前支持的公共参数有：

| 参数名称 | 参数类型 | 是否必须 | 参数描述 |
| --- | ---| ---| ---|
| appKey | String | 是 | 平台颁发给应用的AppKey。例：12345678 |
| session | String | 是 | 平台颁发给应用的授权session。例：xxxxx |
| method | String | 是 | 具体API接口名称，例：api.order.demo |
| timestamp | String | 是 | 时间戳，格式为yyyy-MM-dd HH:mm:ss，时区为GMT+8，例如：2016-01-01 12:00:00。服务端允许客户端请求最大时间误差为10分钟。 |
| format | String | 否 | 请求的body和返回body响应格式，可填写：json，xml，不传默认为json格式|
| v | String | 是 | API协议版本，可选值：1.0 |
| sign | String | 是 | API输入参数签名结果，签名算法参照下面的介绍。 |

## 三、业务参数

API调用除了必须包含公共参数外，如果API本身有业务级的参数也必须传入，每个API的业务级参数请考API文档说明。

业务级的参数都是放在body中，POST请求时发送过去。

## 四、签名算法

为了防止API调用过程中被黑客恶意篡改，调用任何一个API都需要携带签名，服务端会根据请求参数，对签名进行验证，签名不合法的请求将会被拒绝。目前使用MD5(sign_method=md5)签名算法有三种，签名大体过程如下：

1）对所有API请求公共参数，根据参数名称的ASCII码表的顺序排序。如：appKey:1, session:2, method:3, format:4, v:5, timestamp:6排序后的顺序是appKey:1, format:4, method:3, session:2, timestamp:6, v:5 。

2）将排序好的参数名和参数值拼装在一起，根据上面的示例得到的结果为：appKey1format4method3session2timestamp6v5。

3）将上一步的结果，加上POST请求的body内容，body=bodyjson,在拼装的字符串前后加上appSecret后,最终的字符串=appSecret+参数拼装的字符串+body+appSecret。根据上面的示例得到的结果为：secretappKey1format4method3session2timestamp6v5bodyjsonsecret。

3）把拼装好的字符串采用utf-8编码，使用MD5签名算法对编码。

4）将摘要得到的字节流结果使用十六进制表示，如：hex("helloworld".getBytes("utf-8")) = "68656C6C6F776F726C64"

JAVA签名示例代码

```java
public static String sign(Map<String, String> params, String body, String secretKey) throws IOException {
    // 1. 第一步，确保参数已经排序
    String[] keys = params.keySet().toArray(new String[0]);
    Arrays.sort(keys);
    // 2. 第二步，把所有参数名和参数值拼接在一起(包含body体)
    StringBuilder sb = new StringBuilder(secretKey); // 前面加上secretKey
    for (String key : keys) {
        if (!"sign".equals(key)) {
            String value = params.get(key);
            if (StringUtils.hasText(key) && StringUtils.hasText((value))) {
                sb.append(key).append(value);
            }
        }
    }
    sb.append(body); // 拼接body体
    sb.append(secretKey);// 最后加上secretKey

    // 3. 第三步，使用加密算法进行加密
    byte[] bytes = encryptMD5(sb.toString());

    // 4. 把二进制转换成大写的十六进制
    String sign = byte2Hex(bytes);

    return sign;
}

private static byte[] encryptMD5(String message) throws IOException {
    try {
        MessageDigest md5Instance = MessageDigest.getInstance("MD5");
        return md5Instance.digest(message.getBytes("UTF-8"));
    } catch (GeneralSecurityException var3) {
        throw new IOException(var3.toString());
    }
}

private static String byte2Hex(byte[] bytes) {
    StringBuilder sign = new StringBuilder();
    for (int i = 0; i < bytes.length; ++i) {
        String hex = Integer.toHexString(bytes[i] & 255);
        if (hex.length() == 1) {
        sign.append("0");
        }
        sign.append(hex.toUpperCase());
    }
    return sign.toString();
}
```

## 五、调用示例

以api.order.demo调用为例，具体步骤如下：

### 1. 设置参数值

参数设置值

公共参数

method=api.order.demo

appKey=12345678

session=test

timestamp=2016-01-01 12:00:00

format=json

v=1.0

业务参数

请求的body={"startTime":"2016-01-01 12:00:00","endTime":"2016-01-02 12:00:00","shopTitle":"xxxx店铺"}

### 2. 按ASCII顺序排序（去掉等号）
app_key12345678

formatjson

methodapi.order.demo

sessiontest

timestamp2016-01-01 12:00:00

v1.0

### 3. 拼接参数名与参数值

appKey12345678formatjsonmethodapi.order.demosessiontesttimestamp2016-01-01 12:00:00v1.0

### 4. 拼接body和sercet,生成签名

假设appSecret为helloworld，则拼接的字符串：helloworld+按顺序拼接好的参数名与参数值+body+helloworld 

最终的字符串：helloworldappKey12345678formatjsonmethodapi.order.demosessiontesttimestamp2016-01-01 12:00:00v1.0{"startTime":"2016-01-01 12:00:00","endTime":"2016-01-02 12:00:00","shopTitle":"xxxx店铺"}helloworld

签名结果为：hex(md5(helloworld+按顺序拼接好的参数名与参数值+body+helloworld)) = "746A0E59C3D587D581CA81644DC2915F"。

### 5. 组装HTTP请求

将所有参数名和参数值采用utf-8进行URL编码（参数顺序可随意，但必须要包括sign签名参数），然后通过POST发起请求，如：

https://xxxxx/router?method=api.order.demo&v=1.0&session=test&format=json&sign=746A0E59C3D587D581CA81644DC2915F&appKey=12345678&timestamp=2016-01-01+12%3A00%3A00

HTTP Body:
{"startTime":"2016-01-01 12:00:00","endTime":"2016-01-02 12:00:00","shopTitle":"xxxx店铺"}

## 六、注意事项

1. 所有的请求和响应数据编码皆为utf-8格式，URL里的所有参数名和参数值请做URL编码。请求的Content-Type是application/json。

2. 所有API都必须用POST发起请求。要求系统参数都放在query体中，比如上述HTTP请求，系统参数app_key应该在query体，而业务参数在body体里。

