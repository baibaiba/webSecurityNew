package demo.securitystarter.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LoginInterceptor implements HandlerInterceptor {
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
        Cookie[] cookies = request.getCookies();

        // 判断Cookie是否为空
        if ((cookies != null)) {
            //遍历Cookie判断有没有对应的name
            for (Cookie cookie : cookies) {
                //有就直接return true
                if (cookie.getName().equals("loginname")) {
                    return true;
                }
            }
        }
        //request.getRequestDispatcher("/WEB-INF/jsp/login.jsp").forward(request, response);
        //response.sendError(407,"无权限");
        // false不会继续向后执行
        return false;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
