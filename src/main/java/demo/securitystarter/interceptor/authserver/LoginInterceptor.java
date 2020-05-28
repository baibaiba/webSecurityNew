package demo.securitystarter.interceptor.authserver;

import demo.securitystarter.constants.CommonConstants;
import demo.securitystarter.dto.User;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class LoginInterceptor implements HandlerInterceptor {
    private static final String COOKIE_NAME_TOKEN = "cookie";

    /**
     * 检查是否已经登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //获取session中存储的token
        User user = (User) session.getAttribute(CommonConstants.SESSION_USER);

        if (user != null) {
            return true;
        } else {
            //如果token不存在，则跳转到登录页面
            response.sendRedirect(request.getContextPath() + "/login?redirectUri=" + getRequestUrl(request));
            return false;
        }
    }

    public static String getRequestUrl(HttpServletRequest request) {
        //当前请求路径
        String currentUrl = request.getRequestURL().toString();
        //请求参数
        String queryString = request.getQueryString();
        if (!StringUtils.isEmpty(queryString)) {
            currentUrl = currentUrl + "?" + queryString;
        }

        String result = "";
        try {
            result = URLEncoder.encode(currentUrl, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            //ignore
        }

        return result;
    }

    /**
     * 从cookie中 查找需要的cookie值
     *
     * @param request
     * @return
     */
    private String getCookieValue(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(LoginInterceptor.COOKIE_NAME_TOKEN)) {
                return cookie.getValue();
            }
        }

        return null;
    }
}
