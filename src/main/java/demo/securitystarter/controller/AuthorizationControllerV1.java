package demo.securitystarter.controller;

import demo.securitystarter.dto.ClientDetail;
import demo.securitystarter.simulationdata.Simulation;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@Controller
public class AuthorizationControllerV1 {
    private static final String SESSION_AUTH_REDIRECT_URL = "SESSION_AUTH_REDIRECT_URL";
    private static final String SESSION_USER = "SESSION_USER";

    /**
     * 授权页面
     *
     * @return org.springframework.web.servlet.ModelAndView
     * @author zifangsky
     * @date 2018/8/3 16:31
     * @since 1.0.0
     */
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
            session.setAttribute(SESSION_AUTH_REDIRECT_URL, redirectUrl);
        }

        //查询请求授权的客户端信息
        ClientDetail clientDetail = Simulation.clients.get(clientId);
//        AuthClientDetails clientDetails = authorizationService.selectClientDetailsByClientId(clientId);
        modelAndView.addObject("clientId", clientId);
        modelAndView.addObject("clientName", clientDetail.getClientName());
        modelAndView.addObject("scope", scope);
        return modelAndView;
    }

//    /**
//     * 获取Authorization Code
//     * @author zifangsky
//     * @date 2018/8/6 17:40
//     * @since 1.0.0
//     * @param request HttpServletRequest
//     * @return org.springframework.web.servlet.ModelAndView
//     */
//    @RequestMapping("/authorize")
//    public ModelAndView authorize(HttpServletRequest request){
//        HttpSession session = request.getSession();
//        User user = (User) session.getAttribute(SESSION_USER);
//
//        //客户端ID
//        String clientIdStr = request.getParameter("client_id");
//        //权限范围
//        String scopeStr = request.getParameter("scope");
//        //回调URL
//        String redirectUri = request.getParameter("redirect_uri");
//        //status，用于防止CSRF攻击（非必填）
//        String status = request.getParameter("status");
//
//        //生成Authorization Code
//        String authorizationCode = authorizationService.createAuthorizationCode(clientIdStr, scopeStr, user);
//
//        String params = "?code=" + authorizationCode;
//        if(StringUtils.isNoneBlank(status)){
//            params = params + "&status=" + status;
//        }
//
//        return new ModelAndView("redirect:" + redirectUri + params);
//    }
}
