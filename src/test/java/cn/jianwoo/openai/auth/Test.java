package cn.jianwoo.openai.auth;

/**
 * @author gulihua
 * @Description
 * @date 2023-02-14 22:57
 */
public class Test
{
    public static void main(String[] args) throws Exception
    {
        String accessToken = new OpenAiAuth("1729846470@qq.com", "xxxxxxxxxx").auth();
//        accessToken = new OpenAiAuth("1729846470@qq.com", "xxxxxxxxxx", new Proxy(Proxy.Type.HTTP,
//                new InetSocketAddress("http://127.0.0.1", 5000))).auth();
        System.out.println(accessToken);

    }
}
