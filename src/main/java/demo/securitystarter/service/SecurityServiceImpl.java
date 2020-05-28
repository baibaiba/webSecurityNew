package demo.securitystarter.service;

import demo.securitystarter.dto.Base;
import demo.securitystarter.dto.User;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SecurityServiceImpl implements SecurityService {



    private static Map<String, User> map = new HashMap<>();

    private static List<User> users = new ArrayList<>();

    static {
        map.put("", new User("tony", "123456"));
        map.put("", new User("tom", "123456"));


    }

    static {
        users.add(new User("tony", "123456"));
        users.add(new User("tom", "654321"));
    }

    @Override
    public Object getByToken(HttpServletResponse response, String token) {
        // 验证token的有效性
        return null;
    }

    @Override
    public Base<String> login(User user, HttpSession session) throws IOException {
        User user1 = users.stream().filter(item -> item.getUsername().equals(user.getUsername())).findFirst().orElse(null);
        if (user1 == null) {
            return new Base<>(0, "无效用户");
        }

        User user2 = map.get(session.getId());
        if (user2 != null && user2.getUsername().equals(user.getUsername())) {
            return new Base<>(2, "success");
        }

        if (user.getPassword().equals(user1.getPassword())) {
            // 保存用户session,实际
            map.put(session.getId(), user1);
            return new Base<>(1, "success");
        }

        return new Base<>(0, "账号或密码错误");
    }
}
