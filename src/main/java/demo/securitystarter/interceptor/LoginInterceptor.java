package demo.securitystarter.interceptor;

import demo.securitystarter.service.SecurityService;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
    private SecurityService securityTestService;

    private static final String COOKIE_NAME_TOKEN = "cookie";

    public LoginInterceptor(SecurityService securityTestService) {
        this.securityTestService = securityTestService;
    }

    /**
     * 之前执行
     *
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        // 这里是取出Cookie
        String paramToken = request.getHeader(COOKIE_NAME_TOKEN);
        String cookieToken = getCookieValue(request);
        if (StringUtils.isEmpty(cookieToken) && StringUtils.isEmpty(paramToken)) {
            // 如果请求参数及cookie都没有带我们需要的值
            // 调整登录页
            response.sendRedirect("/login");
            return false;
        }

        String token = StringUtils.isEmpty(paramToken) ? cookieToken : paramToken;
        Object byToken = securityTestService.getByToken(response, token);

        //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        //response.sendError(407,"无权限");
        // false不会继续向后执行
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

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
