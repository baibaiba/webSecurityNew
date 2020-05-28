package demo.securitystarter.service;

import demo.securitystarter.dto.Base;
import demo.securitystarter.dto.User;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

public interface SecurityService {
    Object getByToken(HttpServletResponse response, String token);

    Base<String> login(User user, HttpSession session) throws IOException;
}
