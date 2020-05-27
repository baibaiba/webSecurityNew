package demo.securitystarter.controller;

import demo.securitystarter.annotation.SecurityUser;
import demo.securitystarter.dto.Base;
import demo.securitystarter.dto.LoginUser;
import demo.securitystarter.service.SecurityService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

@Controller
@RequestMapping("")
public class LoginController {

    @Autowired
    private SecurityService securityService;

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("loginin")
    public Base<String> logout(@SecurityUser LoginUser loginUser, HttpSession session, HttpServletRequest request,
                               HttpServletResponse response) throws IOException {
        return this.securityService.login(loginUser, response);
    }
}
