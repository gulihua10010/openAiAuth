package cn.jianwoo.openai.auth;

import java.net.HttpCookie;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import cn.hutool.core.util.ReUtil;
import cn.hutool.http.HtmlUtil;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.StrUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;

/**
 * OpenAiAuth 登录实现
 *
 * @blog https://jianwoo.cn
 * @author gulihua
 * @github https://github.com/gulihua10010/
 * @bilibili 顾咕咕了
 * @date 2023-02-10 22:14
 */
public class AuthPost
{
    private static final Log log = LogFactory.get();
    public static final String ERROR_CODE = "999999";
    public static final String ERROR_MSG = "Authorization service error, code returns %s";
    private static Map<String, String> HEADER;
    public static final String URL_1 = "https://explorer.api.openai.com/";
    public static final String URL_2 = "https://explorer.api.openai.com/api/auth/csrf";
    public static final String URL_3 = "https://explorer.api.openai.com/api/auth/signin/auth0?prompt=login";
    public static final String URL_4 = "";
    public static final String URL_5 = "https://auth0.openai.com/u/login/identifier?state=%s";
    public static final String URL_6 = "https://auth0.openai.com/u/login/identifier?state=%s";
    public static final String URL_7 = "https://auth0.openai.com/u/login/password?state=%s";
    public static final String URL_8 = "https://auth0.openai.com/u/login/password?state=%s";
    public static final String URL_9 = "https://auth0.openai.com/authorize/resume?state=%s";
    public static final String URL_10 = "https://explorer.api.openai.com/api/auth/callback/auth0?code=%s&state=%s";
    public static final String URL_11 = "https://explorer.api.openai.com/api/auth/session";
    public static final String ERR_MESSAGE1 = "Request Url (%s) failed with error code %s";
    public static final String ERR_MESSAGE2 = "Request Url (%s) failed with msg %s";
    private OpenAiAuth openAiAuth;
    /** 会话共享 */
    private List<HttpCookie> cookies;
    static
    {
        HEADER = new HashMap<String, String>();
        HEADER.put("User-Agent",
                "Mozilla/5.0 (Macintosh; Arm Mac OS X 10_15_7) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/109.0.0.0 Safari/537.36");
        HEADER.put("Accept-Language", "en-US,en;q=0.8");
        HEADER.put("Connection", "keep-alive");
        HEADER.put("Accept-Encoding", "gzip, deflate, br");
    }

    private AuthPost()
    {

    }


    public AuthPost(OpenAiAuth openAiAuth)
    {
        this.openAiAuth = openAiAuth;

    }


    public SessionRes post() throws PostException
    {
        return post1();
    }


    /**
     *
     * 构造请求访问主页
     *
     * @author gulihua
     * @return
     */
    private SessionRes post1() throws PostException
    {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(HEADER);
        headers.put("Host", "explorer.api.openai.com");
        HttpResponse response = HttpRequest.get(URL_1).headerMap(headers, true).setProxy(openAiAuth.getProxy())
                .execute();
        if (response.getStatus() != 200)
        {
            log.error(formatMsg1(URL_1, response.getStatus()));
            log.error(">>>>response:{}",response.body());
            throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
        }
        cookies = response.getCookies();
        return post2();

    }


    /**
     *
     * 获取csrf token
     *
     * @author gulihua
     * @return
     */
    private SessionRes post2() throws PostException
    {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(HEADER);
        headers.put("Host", "explorer.api.openai.com");
        headers.put("Referer", "https://explorer.api.openai.com/auth/login");
        headers.put("Accept", "*/*");

        HttpResponse response = HttpRequest.get(URL_2).headerMap(headers, true).setProxy(openAiAuth.getProxy())
                .cookie(cookies).execute();
        if (response.getStatus() != 200)
        {
            log.error(formatMsg1(URL_2, response.getStatus()));
            log.error(">>>>response:{}",response.body());
            throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
        }
        String csrfToken = null;
        try
        {
            String res = response.body();
            JSONObject json = JSONObject.parseObject(res);
            csrfToken = json.getString("csrfToken");

        }
        catch (Exception e)
        {
            log.error(formatMsg2(URL_2, response.body()));
            throw new PostException("300001", "The csrfToken cannot be obtained.");
        }

        cookies = response.getCookies();
        return post3(csrfToken);
    }


    /**
     *
     * 请求/api/auth/signin/auth0?prompt=login，获取 url
     *
     * @author gulihua
     * @param csrfToken csrf token
     * @return
     */
    private SessionRes post3(String csrfToken) throws PostException
    {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(HEADER);
        headers.put("Host", "explorer.api.openai.com");
        headers.put("Referer", "https://explorer.api.openai.com/auth/login");
        headers.put("Origin", "https://explorer.api.openai.com");
        headers.put("Accept", "*/*");
        headers.put("Sec-Fetch-Site", "same-origin");
        headers.put("Sec-Fetch-Mode", "cors");
        headers.put("Sec-Fetch-Dest", "empty");
        headers.put("Content-Type", "application/x-www-form-urlencoded");

        Map<String, Object> payload = new HashMap<>();
        payload.put("callbackUrl", "/");
        payload.put("csrfToken", csrfToken);
        payload.put("json", true);

        HttpResponse response = HttpRequest.post(URL_3).headerMap(headers, true).form(payload)
                .setProxy(openAiAuth.getProxy()).cookie(cookies).execute();
        if (response.getStatus() != 200)
        {
            log.error(formatMsg1(URL_3, response.getStatus()));
            log.error(">>>>response:{}",response.body());
            throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
        }

        String url = null;
        try
        {
            String res = response.body();
            JSONObject json = JSONObject.parseObject(res);
            url = json.getString("url");

        }
        catch (Exception e)
        {
            log.error(formatMsg2(URL_3, response.body()));
            throw new PostException(ERROR_CODE, "The URL cannot be obtained.");

        }
        if (StrUtil.isBlank(url) || url.contains("error"))
        {
            throw new PostException("200001", "You have been rate limited.");
        }
        cookies = response.getCookies();
        return post4(url);
    }


    /**
     *
     * 请求url, 并获取 state
     *
     * @author gulihua
     * @param url 上一步返回的 url 地址
     * @return
     */
    private SessionRes post4(String url) throws PostException
    {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(HEADER);
        headers.put("Host", "auth0.openai.com");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        HttpResponse response = HttpRequest.get(url).headerMap(headers, true).setProxy(openAiAuth.getProxy())
                .cookie(cookies).execute();
        if (response.getStatus() != 200 && response.getStatus() != 302)
        {
            log.error(formatMsg1(url, response.getStatus()));
            log.error(">>>>response:{}",response.body());
            if (response.body().contains("rate limited")){
                throw new PostException(ERROR_CODE, "You are being rate limited.");
            }
            throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
        }
        if (!response.body().contains("state"))
        {
            log.error(formatMsg2(url, response.body()));
            throw new PostException("300001", "The state cannot be obtained.");
        }

        cookies = response.getCookies();
        String state = response.body().split("state")[1];
        state = state.split("\"")[0].substring(1);
        return post5(state);

    }


    /**
     *
     * 请求/u/login/identifier?state=%s
     *
     * @author gulihua
     * @param state state
     * @return
     */
    private SessionRes post5(String state) throws PostException
    {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(HEADER);
        headers.put("Host", "auth0.openai.com");
        headers.put("Referer", "https://explorer.api.openai.com/");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        HttpResponse response = HttpRequest.get(String.format(URL_5, state)).headerMap(headers, true)
                .setProxy(openAiAuth.getProxy()).cookie(cookies).execute();
        if (response.getStatus() != 200)
        {
            log.error(formatMsg1(URL_5, response.getStatus()));
            log.error(">>>>response:{}",response.body());
            throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
        }
        cookies = response.getCookies();

        return post6(state);

    }


    /**
     *
     * 请求/u/login/identifier?state=%s
     *
     * @author gulihua
     * @param state state
     * @return
     */
    private SessionRes post6(String state) throws PostException
    {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(HEADER);
        headers.put("Host", "auth0.openai.com");
        headers.put("Referer", String.format(URL_6, state));
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Origin", "https://auth0.openai.com");

        Map<String, Object> payload = new HashMap<>();
        payload.put("state", state);
        payload.put("username", openAiAuth.getEmail());
        payload.put("js-available", false);
        payload.put("webauthn-available", true);
        payload.put("is-brave", false);
        payload.put("webauthn-platform-available", true);
        payload.put("action", "default");

        HttpResponse response = HttpRequest.post(String.format(URL_6, state)).headerMap(headers, true).form(payload)
                .setProxy(openAiAuth.getProxy()).cookie(cookies).execute();
        if (response.getStatus() != 200 && response.getStatus() != 302)
        {
            log.error(formatMsg1(URL_6, response.getStatus()));
            log.error(">>>>response:{}",response.body());

            throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
        }
        if (!response.body().contains("state"))
        {
            log.error(formatMsg2(URL_6, response.body()));
            throw new PostException("300001", "The state cannot be obtained.");
        }

        cookies = response.getCookies();

        return post7(state);
    }


    /**
     *
     * 请求/u/login/password?state=%s
     *
     * @author gulihua
     * @param state state
     * @return
     */
    private SessionRes post7(String state) throws PostException
    {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(HEADER);
        headers.put("Host", "auth0.openai.com");
        headers.put("Referer", "https://explorer.api.openai.com/");
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        HttpResponse response = HttpRequest.get(String.format(URL_7, state)).headerMap(headers, true)
                .setProxy(openAiAuth.getProxy()).cookie(cookies).execute();
        if (response.getStatus() != 200)
        {
            log.error(formatMsg1(URL_7, response.getStatus()));
            log.error(">>>>response:{}",response.body());

            throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
        }
        cookies = response.getCookies();

        return post8(state);

    }


    /**
     *
     * 请求/u/login/password?state=%s
     *
     * @author gulihua
     * @param state state
     * @return
     */
    private SessionRes post8(String state) throws PostException
    {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(HEADER);
        headers.put("Host", "auth0.openai.com");
        headers.put("Referer", String.format(URL_8, state));
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Origin", "https://auth0.openai.com");

        Map<String, Object> payload = new HashMap<>();
        payload.put("state", state);
        payload.put("username", openAiAuth.getEmail());
        payload.put("password", openAiAuth.getPassword());
        payload.put("action", "default");
        HttpResponse response = HttpRequest.post(String.format(URL_8, state)).headerMap(headers, true).form(payload)
                .setProxy(openAiAuth.getProxy()).cookie(cookies).execute();
        if (response.getStatus() == 400)
        {
            if (response.body().contains("Wrong email or password"))
            {
                throw new PostException("400001", "Wrong email or password");
            }
            else if (response.body().contains("will be blocked"))
            {
                throw new PostException("400002",
                        "We have detected suspicious login behavior and further attempts will be blocked. Please contact the administrator");
            }
            else
            {
                throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
            }
        }
        if (response.getStatus() != 200 && response.getStatus() != 302)
        {
            log.error(formatMsg1(URL_8, response.getStatus()));
            log.error(">>>>response:{}",response.body());
            throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
        }

        cookies = response.getCookies();

        String newstate = response.body().split("state")[1];
        newstate = newstate.split("\"")[0].substring(1);
        return post9(state, newstate);
    }


    /**
     *
     * 请求/authorize/resume?state=%s，获取新的 url
     *
     * @author gulihua
     * @param state state
     * @param newstate 新的 state
     * @return
     */
    private SessionRes post9(String state, String newstate) throws PostException
    {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(HEADER);
        headers.put("Host", "auth0.openai.com");
        headers.put("Referer", String.format(URL_9, state));
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");
        headers.put("Content-Type", "application/x-www-form-urlencoded");
        headers.put("Origin", "https://auth0.openai.com");

        HttpResponse response = HttpRequest.get(String.format(URL_9, newstate)).headerMap(headers, true)
                .setProxy(openAiAuth.getProxy()).cookie(cookies).execute();
        if (response.getStatus() != 200 && response.getStatus() != 302)
        {
            log.error(formatMsg1(URL_9, response.getStatus()));
            log.error(">>>>response:{}",response.body());
            throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
        }
        cookies = response.getCookies();
        String url = response.body().split(" href=\"")[1];
        url = url.split("\"")[0];
        return post10(url);

    }


    /**
     *
     * 请求url
     *
     * @author gulihua
     * @param url 上一步返回的 url
     * @return
     */
    private SessionRes post10(String url) throws PostException
    {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(HEADER);
        headers.put("Host", "explorer.api.openai.com");
        headers.put("Referer", url);
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        // 根据抓包，此处要删除 cookies
        HttpResponse response = HttpRequest.get(url).headerMap(headers, true).setProxy(openAiAuth.getProxy()).execute();
        if (response.getStatus() != 200 && response.getStatus() != 302)
        {
            log.error(formatMsg1(url, response.getStatus()));
            log.error(">>>>response:{}",response.body());
            throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
        }

        cookies = response.getCookies();
//        for (int i = 0; i < response.getCookies().size(); i++)
//        {
//            System.out.println(response.getCookies().get(i).getName() + ": " + response.getCookies().get(i).getValue());
//
//        }
        return post11();

    }


    /**
     *
     * 请求/api/auth/session，获取 accessToken
     *
     * @author gulihua
     * @return
     */
    private SessionRes post11() throws PostException
    {
        Map<String, String> headers = new HashMap<>();
        headers.putAll(HEADER);
        headers.put("Host", "explorer.api.openai.com");
        headers.put("Referer", URL_11);
        headers.put("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8");

        HttpResponse response = HttpRequest.get(URL_11).headerMap(headers, true).setProxy(openAiAuth.getProxy())
                .cookie(cookies).execute();
        if (response.getStatus() != 200 && response.getStatus() != 302)
        {
            log.error(formatMsg1(URL_11, response.getStatus()));
            log.error(">>>>response:{}",response.body());
            throw new PostException(ERROR_CODE, ERROR_MSG, response.getStatus());
        }

//
//        System.out.println(response.body());

        SessionRes session = SessionRes.getInstance();

        try
        {
            String res = response.body();
            JSONObject json = JSONObject.parseObject(res);
            session.setAccessToken(json.getString("accessToken"));
            session.setExpires(json.getString("expires"));
            JSONObject user = json.getJSONObject("user");
            if (user != null)
            {
                session.setEmail(user.getString("email"));
                session.setId(user.getString("id"));
                session.setImage(user.getString("image"));
            }
            session.setSecureNextAuthSessionToken(response.getCookie("__Secure-next-auth.session-token").getValue());

        }
        catch (Exception e)
        {
            log.error(formatMsg2(URL_11, response.body()));
            throw new PostException("300001", "The accessToken cannot be obtained.");

        }
        if (StrUtil.isBlank(session.getAccessToken()))
        {
            throw new PostException("300001",
                    "Fetch accessToken failed, maybe the interface on the website has changed.");
        }

        return session;

    }


    private String formatMsg1(String url, int code)
    {
        return String.format(ERR_MESSAGE1, url, code);
    }


    private String formatMsg2(String url, String msg)
    {
        return String.format(ERR_MESSAGE2, url, msg);
    }
}
