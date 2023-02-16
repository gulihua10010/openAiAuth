package cn.jianwoo.openai.auth;

/**
 * @author GuLihua
 * @Description
 * @date 2020-08-04 14:21
 */
public class PostException extends Exception
{
    public static final PostException SYSTEM_ERROR = new PostException("999999", "授权服务出错!");
    private static final long serialVersionUID = -4668522971027224346L;
    protected String code;
    protected String msg;

    public PostException()
    {
    }


    public PostException(String code, String msg, Object... args)
    {
        super(args == null || args.length == 0 ? msg : String.format(msg, args));
        this.code = code;
        this.msg = args == null || args.length == 0 ? msg : String.format(msg, args);
    }


    public PostException(String message)
    {
        super(message);
        this.msg = message;
    }


    public PostException(String message, Throwable cause)
    {
        super(message, cause);
        this.msg = message;

    }


    public PostException(Throwable cause)
    {
        super(cause);
    }


    public PostException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
        this.msg = message;

    }


    public PostException getNewInstance(String code, String msg, Object... args)
    {
        return new PostException(code, msg, args);
    }


    public PostException formatMsg(String msg, Object... args)
    {
        return new PostException(code, msg, args);
    }


    public String getCode()
    {
        return code;
    }


    public void setCode(String code)
    {
        this.code = code;
    }


    public String getMsg()
    {
        return msg;
    }


    public void setMsg(String msg)
    {
        this.msg = msg;
    }


    public PostException format(Object... args)
    {
        return new PostException(this.code, this.msg, args);
    }
}
