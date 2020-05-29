package demo.securitystarter.controller;

import demo.securitystarter.constants.CommonConstants;
import demo.securitystarter.dto.AuthClientDetails;
import demo.securitystarter.dto.User;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("oauth2.0")
public class ThirdAuthorizationController {
    /**
     * 第二步获取code
     *
     * @param request
     * @return
     */
    @RequestMapping("/authorize")
    public ModelAndView authorize(HttpServletRequest request) {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute(CommonConstants.SESSION_USER);
        //客户端ID
        String clientIdStr = request.getParameter("client_id");
        //权限范围
        String scopeStr = request.getParameter("scope");
        //回调URL
        String redirectUri = request.getParameter("redirect_uri");
        //status，用于防止CSRF攻击（非必填）
        String status = request.getParameter("status");
        //生成Authorization Code
        String authorizationCode = "gwrut&234";
        String params = "?code=" + authorizationCode;
        if (!StringUtils.isEmpty(status)) {
            params = params + "&status=" + status;
        }

        return new ModelAndView("redirect:" + redirectUri + params);
    }

    @RequestMapping("/authorizePage")
    public ModelAndView authorizePage(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("authorize");
        //在页面同意授权后的回调地址
        String redirectUrl = request.getParameter("redirectUri");
        //客户端ID
        String clientId = request.getParameter("client_id");
        //权限范围
        String scope = request.getParameter("scope");
        if (!StringUtils.isEmpty(redirectUrl)) {
            HttpSession session = request.getSession();
            //将回调地址添加到session中
            session.setAttribute(CommonConstants.SESSION_AUTH_REDIRECT_URL, redirectUrl);
        }

        //查询请求授权的客户端信息
        AuthClientDetails clientDetails = new AuthClientDetails();
        clientDetails.setClientName("test");
        modelAndView.addObject("clientId", clientId);
        modelAndView.addObject("clientName", clientDetails.getClientName());
        modelAndView.addObject("scope", scope);

        return modelAndView;
    }

    @PostMapping(value = "/agree")
    @ResponseBody
    public Map<String, Object> agree(HttpServletRequest request) {
        Map<String, Object> result = new HashMap<>(2);
        HttpSession session = request.getSession();
        //客户端ID
        String clientIdStr = request.getParameter("client_id");
        //权限范围
        String scopeStr = request.getParameter("scope");

        if (!StringUtils.isEmpty(clientIdStr) && !StringUtils.isEmpty(scopeStr)) {
            User user = (User) session.getAttribute(CommonConstants.SESSION_USER);
            //1. 向数据库中保存授权信息
            //2. 返回给页面的数据
            result.put("code", 200);
            //授权成功之后的回调地址
            String redirectUrl = (String) session.getAttribute(CommonConstants.SESSION_AUTH_REDIRECT_URL);
            session.removeAttribute(CommonConstants.SESSION_AUTH_REDIRECT_URL);

            if (!StringUtils.isEmpty(redirectUrl)) {
                result.put("redirect_uri", redirectUrl);
            }
        } else {
            result.put("msg", "请求参数不能为空！");
        }

        return result;
    }
}
