package demo.securitystarter.service;

import demo.securitystarter.dto.Base;
import demo.securitystarter.dto.LoginUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SecurityServiceImpl implements SecurityService {
    private static Map<String, LoginUser> map = new HashMap<>();

    private static List<LoginUser> loginUsers = new ArrayList<>();

    static {
        map.put("", new LoginUser("tony", "123456"));
        map.put("", new LoginUser("tom", "123456"));
    }

    static {
        loginUsers.add(new LoginUser("tony", "123456"));
        loginUsers.add(new LoginUser("tom", "654321"));
    }

    @Override
    public Object getByToken(HttpServletResponse response, String token) {
        // 验证token的有效性
        return null;
    }

    @Override
    public Base<String> login(LoginUser loginUser, HttpServletResponse response) throws IOException {
        LoginUser loginUser1 = loginUsers.stream().filter(item -> item.getUsername().equals(loginUser.getUsername())).findFirst().orElse(null);
        if (loginUser1 == null) {
            return new Base<>(0, "无效用户");
        }

        if (loginUser.getPassword().equals(loginUser1.getPassword())) {
            response.sendRedirect("/index");
        }

        return new Base<>(0, "账号或密码错误");
    }
}
