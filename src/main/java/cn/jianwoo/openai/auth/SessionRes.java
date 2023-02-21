package cn.jianwoo.openai.auth;

import java.io.Serializable;

/**
 * @author gulihua
 * @Description
 * @date 2023-02-21 11:51
 */
public class SessionRes implements Serializable
{

    private static final long serialVersionUID = 197097599376580010L;

    private String accessToken;
    private String expires;
    private String id;
    private String image;
    private String email;

    public static SessionRes getInstance()
    {
        return new SessionRes();
    }

    /** cookie.__Secure-next-auth.session-token **/
    private String secureNextAuthSessionToken;

    public String getAccessToken()
    {
        return this.accessToken;
    }


    public void setAccessToken(String accessToken)
    {
        this.accessToken = accessToken;
    }


    public String getExpires()
    {
        return this.expires;
    }


    public void setExpires(String expires)
    {
        this.expires = expires;
    }


    public String getId()
    {
        return this.id;
    }


    public void setId(String id)
    {
        this.id = id;
    }


    public String getImage()
    {
        return this.image;
    }


    public void setImage(String image)
    {
        this.image = image;
    }


    public String getEmail()
    {
        return this.email;
    }


    public void setEmail(String email)
    {
        this.email = email;
    }


    public String getSecureNextAuthSessionToken()
    {
        return this.secureNextAuthSessionToken;
    }


    public void setSecureNextAuthSessionToken(String secureNextAuthSessionToken)
    {
        this.secureNextAuthSessionToken = secureNextAuthSessionToken;
    }


    @Override
    public String toString()
    {
        return "SessionRes{" + "accessToken='" + accessToken + '\'' + ", expires='" + expires + '\'' + ", id='" + id
                + '\'' + ", image='" + image + '\'' + ", email='" + email + '\'' + ", secureNextAuthSessionToken='"
                + secureNextAuthSessionToken + '\'' + '}';
    }
}
