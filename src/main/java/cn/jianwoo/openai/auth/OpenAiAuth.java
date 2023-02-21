package cn.jianwoo.openai.auth;

import java.net.Proxy;

/**
 * OpenAiAuth登录实体类
 * 
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2023-02-10 22:10
 */
public class OpenAiAuth
{
    /** 注册邮箱 */
    private String email;
    /** 密码 */
    private String password;

    /** 代理 */
    private Proxy proxy;

    private OpenAiAuth()
    {
    }


    public OpenAiAuth(String email, String password)
    {
        this(email, password, null);
    }


    public OpenAiAuth(String email, String password, Proxy proxy)
    {
        this.email = email;
        this.password = password;
        this.proxy = proxy;
    }


    protected String getEmail()
    {
        return this.email;
    }


    protected String getPassword()
    {
        return this.password;
    }


    protected Proxy getProxy()
    {
        return this.proxy;
    }


    public SessionRes auth() throws PostException
    {
        return new AuthPost(this).post();
    }
}
