package demo.securitystarter.controller;

import demo.securitystarter.annotation.HasRole;
import demo.securitystarter.annotation.SecurityUser;
import demo.securitystarter.dto.Base;
import demo.securitystarter.dto.LoginUser;
import demo.securitystarter.service.SecurityTestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping(value = "api")
public class SecurityTestController {
    @Autowired
    private SecurityTestService securityTestService;

    /**
     * 管理员可操作，其它角色抛出异常
     *
     * @return
     */
    @HasRole(name = HasRole.Role.ADMIN)
    @GetMapping("roles")
    public Base get() {
        return new Base(1, "success");
    }

    @PostMapping("list")
    public Base list(@SecurityUser LoginUser loginUser) {
        return new Base<>(1, "success", loginUser);
    }

    @RequestMapping("logout")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {//遍历所有Cookie
            if (cookie.getName().equals("loginname")) {//找到对应的cookie
                cookie.setMaxAge(0);//Cookie并不能根本意义上删除，只需要这样设置为0即可
                cookie.setPath("/");//很关键，设置成跟写入cookies一样的，全路径共享Cookie
                response.addCookie(cookie);//重新响应
                return "login.jsp";
            }
        }
        return "login.jsp";
    }
}
