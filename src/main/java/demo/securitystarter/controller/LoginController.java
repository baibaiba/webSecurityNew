package demo.securitystarter.controller;

import demo.securitystarter.annotation.SecurityUser;
import demo.securitystarter.dto.Base;
import demo.securitystarter.dto.LoginUser;
import demo.securitystarter.service.SecurityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpSession;
import java.io.IOException;

@RestController
@RequestMapping("")
public class LoginController {

    private final SecurityService securityService;

    public LoginController(SecurityService securityService) {
        this.securityService = securityService;
    }

    @RequestMapping("login")
    public String login() {
        return "login";
    }

    @PostMapping("loginIn")
    public Base<String> login(@SecurityUser LoginUser loginUser, HttpSession session) throws IOException {
        return this.securityService.login(loginUser, session);
    }
}
