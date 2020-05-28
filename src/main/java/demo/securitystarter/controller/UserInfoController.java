package demo.securitystarter.controller;

import demo.securitystarter.annotation.HasRole;
import demo.securitystarter.annotation.SecurityUser;
import demo.securitystarter.dto.Base;
import demo.securitystarter.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "api")
public class UserInfoController {
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
    public Base list(@SecurityUser User user) {
        return new Base<>(1, "success", user);
    }
}
