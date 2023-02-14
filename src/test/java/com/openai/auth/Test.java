package com.openai.auth;

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
        System.out.println(accessToken);

    }
}
