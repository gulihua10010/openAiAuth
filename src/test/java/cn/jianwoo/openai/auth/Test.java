package cn.jianwoo.openai.auth;

import com.alibaba.fastjson.JSON;

/**
 * @author gulihua
 * @Description
 * @date 2023-02-14 22:57
 */
public class Test
{
    public static void main(String[] args) throws PostException
    {
        SessionRes accessToken = new OpenAiAuth("1729846470@qq.com", "xxxxxxx").auth();
//        accessToken = new OpenAiAuth("1729846470@qq.com", "xxxxxxxxxx", new Proxy(Proxy.Type.HTTP,
//                new InetSocketAddress("http://127.0.0.1", 5000))).auth();
        System.out.println(JSON.toJSONString(accessToken));

    }

}
