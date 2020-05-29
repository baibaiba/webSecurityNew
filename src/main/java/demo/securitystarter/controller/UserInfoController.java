package demo.securitystarter.controller;

import demo.securitystarter.annotation.HasRole;
import demo.securitystarter.annotation.SecurityUser;
import demo.securitystarter.dto.Base;
import demo.securitystarter.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "api")
public class UserInfoController {
    /**
     * 管理员可操作，其它角色抛出异常
     *
     * @return
     */
    @HasRole(name = HasRole.Role.ADMIN)
    @GetMapping("admin")
    public Base admin() {
        return new Base(1, "success");
    }

    @HasRole(name = HasRole.Role.STAFF)
    @GetMapping("staff")
    public Base staff() {
        return new Base(1, "success");
    }

    @PostMapping("body")
    public Base body(@SecurityUser User user) {
        return new Base<>(1, "success", user);
    }
}
