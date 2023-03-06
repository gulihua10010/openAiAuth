package cn.jianwoo.openai.auth;

import com.alibaba.fastjson.JSON;

import java.net.InetSocketAddress;
import java.net.Proxy;

/**
 * @author gulihua
 * @Description
 * @date 2023-02-14 22:57
 */
public class Test
{
    public static void main(String[] args) throws PostException
    {
//        SessionRes accessToken = new OpenAiAuth("1729846470@qq.com", "lihua0105...").auth();
        SessionRes accessToken = new OpenAiAuth("1729846470@qq.com", "lihua0105...", new Proxy(Proxy.Type.HTTP,
                new InetSocketAddress("127.0.0.1", 7890))).auth();
        System.out.println(JSON.toJSONString(accessToken));

    }

}
