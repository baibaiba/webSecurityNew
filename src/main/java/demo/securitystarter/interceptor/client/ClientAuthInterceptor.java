package demo.securitystarter.interceptor.client;

import demo.securitystarter.constants.CommonConstants;
import demo.securitystarter.util.JsonUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 用于校验OAuth2.0登录中的状态码
 */
public class ClientAuthInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();

        //当前系统请求认证服务器成功之后返回的Authorization Code
        String code = request.getParameter("code");
        //原样返回的状态码
        String resultStatus = request.getParameter("status");

        //获取session中存储的token
        String accessToken = (String) session.getAttribute(CommonConstants.SESSION_ACCESS_TOKEN);

        if (!StringUtils.isEmpty(accessToken)) {
            return true;
        }

        //code不为空，则说明当前请求是从认证服务器返回的回调请求
        if (!StringUtils.isEmpty(code)) {

            // 验证一些有效性
            if ("aaaaa".equals(code)) {
                response.setCharacterEncoding("UTF-8");
                response.setHeader("Content-type", "application/json;charset=UTF-8");
                Map<String, String> result = new HashMap<>(2);
                result.put("error", "invalid_status");
                result.put("error_description", "状态码校验失败，为避免CSRF攻击，请重新登录");

                response.getWriter().write(JsonUtil.parseObjToJson(result));
                return false;
            }

            return true;

        } else {
            return true;
        }
    }
}
