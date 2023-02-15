# openAiAuth
OpenAI/chatGpt User name and password Authorized login The jar package is encapsulated

[简体中文文档](README.md).


## Usage

```java
String accessToken = new OpenAiAuth("<Your email>", "Your password").auth();
```
#### Use proxy

```java
String accessToken = new OpenAiAuth("<Your email>", "Your password", new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress("<host>", "<port>"))).auth();
```


#### Dependency

```xml

<dependency>
    <groupId>cn.jianwoo.openai.auth</groupId>
    <artifactId>openAiAuth</artifactId>
    <version>1.0.0</version>
</dependency>

<!--仓库地址-->
<repositories>
<repository>
    <id>nexues</id>
    <name>snapshots</name>
    <url>https://s01.oss.sonatype.org/content/groups/public/</url>
    <releases>
        <enabled>true</enabled>
    </releases>
    <snapshots>
        <enabled>true</enabled>
        <updatePolicy>always</updatePolicy>
    </snapshots>
</repository>
</repositories>
```

## Note

Since the API interface of the official website may be adjusted sometimes, it is not guaranteed that this jar package will be successfully adjusted. If it fails, please leave a message.
This program does not transfer or save any data to the server!



## What can we use accessToken for?

When logging in to chatGpt, you can obtain accessToken directly through the email and password call without logging in from the browser.