package demo.securitystarter.controller;

import demo.securitystarter.constants.CommonConstants;
import demo.securitystarter.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.thymeleaf.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Controller
public class ThirdLoginController {
    @RequestMapping("/login")
    public ModelAndView loginPage(HttpServletRequest request) {
        String redirectUrl = request.getParameter("redirectUri");
        if (!StringUtils.isEmpty(redirectUrl)) {
            HttpSession session = request.getSession();
            //将回调地址添加到session中
            session.setAttribute(CommonConstants.SESSION_LOGIN_REDIRECT_URL, redirectUrl);
        }

        return new ModelAndView("login");
    }

    @ResponseBody
    @PostMapping("/check")
    public Map<String, Object> check(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);
        //用户名
        String username = request.getParameter("username");
        //密码
        String password = request.getParameter("password");
        if (!StringUtils.isEmpty(username) && !StringUtils.isEmpty(password)) {
            //1. 登录验证
            // 获取用户信息，并走逻辑密码验证
            User user = new User();
            user.setCreateTime(new Date());
            user.setPassword("123456");
            user.setStatus(1);
            user.setEmail("af@qq.com");
            user.setId(1248176382);
            user.setMobile("18113769027");
            user.setUsername("test");
            // 模拟登录验证通过
            if (password.equals(user.getPassword())) {
                //2. session中添加用户信息
                HttpSession session = request.getSession();
                session.setAttribute(CommonConstants.SESSION_USER, user);

                //3. 返回给页面的数据
                result.put("code", 200);
                //登录成功之后的回调地址
                String redirectUrl = (String) session.getAttribute(CommonConstants.SESSION_LOGIN_REDIRECT_URL);
                session.removeAttribute(CommonConstants.SESSION_LOGIN_REDIRECT_URL);
                if (!StringUtils.isEmpty(redirectUrl)) {
                    result.put("redirect_uri", redirectUrl);
                }
            } else {
                result.put("msg", "用户名或密码错误！");
            }
        } else {
            result.put("msg", "请求参数不能为空！");
        }

        return result;
    }
}
