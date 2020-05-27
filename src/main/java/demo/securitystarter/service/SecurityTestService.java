package demo.securitystarter.service;

import javax.servlet.http.HttpServletResponse;

public interface SecurityTestService {
    Object getByToken(HttpServletResponse response, String token);
}
