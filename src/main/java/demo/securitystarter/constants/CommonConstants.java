package demo.securitystarter.constants;

public class CommonConstants {
    /**
     * 用户信息在session中存储的变量名
     */
    public static final String SESSION_USER = "SESSION_USER";

    /**
     * 授权页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_AUTH_REDIRECT_URL = "SESSION_AUTH_REDIRECT_URL";

    /**
     * 登录页面的回调地址在session中存储的变量名
     */
    public static final String SESSION_LOGIN_REDIRECT_URL = "LOGIN_REDIRECT_URL";

    /**
     * access_token在session中存储的变量名
     */
    public static final String SESSION_ACCESS_TOKEN = "SESSION_ACCESS_TOKEN";

    /**
     * 请求Authorization Code的随机状态码在session中存储的变量名
     */
    public static final String SESSION_AUTH_CODE_STATUS = "AUTH_CODE_STATUS";
}
