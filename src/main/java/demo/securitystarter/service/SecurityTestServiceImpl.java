package demo.securitystarter.service;

import demo.securitystarter.dto.LoginUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

@Service
public class SecurityTestServiceImpl implements SecurityTestService{
    private static Map<String, LoginUser> map = new HashMap<>();

    static {
        map.put("", new LoginUser());
    }

    @Override
    public Object getByToken(HttpServletResponse response, String token) {
        // 验证token的有效性
        return null;
    }
}
