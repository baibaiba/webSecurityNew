package demo.securitystarter.service;

import demo.securitystarter.dto.Base;
import demo.securitystarter.dto.LoginUser;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface SecurityService {
    Object getByToken(HttpServletResponse response, String token);

    Base<String> login(LoginUser loginUser, HttpServletResponse response) throws IOException;
}
