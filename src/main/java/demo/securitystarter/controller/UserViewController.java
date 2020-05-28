package demo.securitystarter.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserViewController {
    /**
     * 用户首页
     *
     * @return view
     */
    @RequestMapping("/userIndex")
    public String userIndex() {
        return "index";
    }
}
