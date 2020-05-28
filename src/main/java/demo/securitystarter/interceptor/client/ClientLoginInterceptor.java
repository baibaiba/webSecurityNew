package demo.securitystarter.interceptor.client;

import demo.securitystarter.constants.CommonConstants;
import demo.securitystarter.interceptor.authserver.LoginInterceptor;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 定义一些页面需要做登录检查
 */
public class ClientLoginInterceptor extends HandlerInterceptorAdapter {

    /**
     * 检查是否已经登录
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //获取session中存储的token
        String accessToken = (String) session.getAttribute(CommonConstants.SESSION_ACCESS_TOKEN);

        if (!StringUtils.isEmpty(accessToken)) {
            return true;
        } else {
            //如果token不存在，则跳转等登录页面
            response.sendRedirect(request.getContextPath() + "/clientLogin?redirectUrl=" + LoginInterceptor.getRequestUrl(request));

            return false;
        }
    }
}
