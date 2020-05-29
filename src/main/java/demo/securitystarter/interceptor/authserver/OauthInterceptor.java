package demo.securitystarter.interceptor.authserver;

import demo.securitystarter.constants.CommonConstants;
import demo.securitystarter.dto.AuthClientUser;
import demo.securitystarter.dto.User;
import demo.securitystarter.interceptor.authserver.LoginInterceptor;
import demo.securitystarter.util.JsonUtil;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

/**
 * 检查是否已经存在授权
 */
public class OauthInterceptor extends HandlerInterceptorAdapter {
    static int index = 0;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        //参数信息
        String params = "?redirectUri=" + LoginInterceptor.getRequestUrl(request);

        //客户端ID
        String clientIdStr = request.getParameter("client_id");
        //权限范围
        String scopeStr = request.getParameter("scope");
        //回调URL
        String redirectUri = request.getParameter("redirect_uri");
        //返回形式
        String responseType = request.getParameter("response_type");

        //获取session中存储的token
        User user = (User) session.getAttribute(CommonConstants.SESSION_USER);

        if (!StringUtils.isEmpty(clientIdStr) && !StringUtils.isEmpty(scopeStr) &&
                !StringUtils.isEmpty(redirectUri) && "code".equals(responseType)) {
            params = params + "&client_id=" + clientIdStr + "&scope=" + scopeStr;

            //1. 查询是否存在授权信息

            //2. 查询用户给接入的客户端是否已经授权
            AuthClientUser clientUser = null;
            // 这里只是为了模拟所以使用了index变量
            if (index == 1) {
                return true;
            } else {
                //如果没有授权，则跳转到授权页面
                ++index;
                response.sendRedirect(request.getContextPath() + "/oauth2.0/authorizePage" + params);
                return false;
            }
        } else {
            return this.generateErrorResponse(response, "invalid_request", "请求缺少某个必需参数");
        }
    }

    /**
     * 组装错误请求的返回
     */
    private boolean generateErrorResponse(HttpServletResponse response, String error, String error_description) throws Exception {
        response.setCharacterEncoding("UTF-8");
        response.setHeader("Content-type", "application/json;charset=UTF-8");
        Map<String, String> result = new HashMap<>(2);
        result.put("error", error);
        result.put("error_description", error_description);

        response.getWriter().write(JsonUtil.parseObjToJson(result));
        return false;
    }

}
