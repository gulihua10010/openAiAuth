# openAiAuth
openAI/chatGpt 用户名、密码授权登录 jar 包封装

[English Doc](README_en.md).

## 用法

```java
String accessToken = new OpenAiAuth("<Your email>", "Your password").auth();
```
#### 使用代理

```java
String accessToken = new OpenAiAuth("<Your email>", "Your password", new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress("<host>", "<port>"))).auth();
```

## 注意

由于官网的 API 接口有时会调整，不能保证这个 jar 包一定调成功，如果失败可以留言。
本程序不会传输或保存任何数据到服务器上!



## accessToken可以用来干嘛？

当登录 chatGpt 时，可以不用浏览器上登录，然后抓包获取accessToken，直接通过邮箱和密码调用获取。
