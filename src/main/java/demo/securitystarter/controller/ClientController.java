package demo.securitystarter.controller;

import demo.securitystarter.constants.CommonConstants;
import demo.securitystarter.dto.AuthClientDetails;
import demo.securitystarter.dto.AuthorizationResponse;
import demo.securitystarter.dto.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

@Controller
public class ClientController {
    @Value("${test.oauth2.client-id}")
    private String clientId;

    @Value("${test.oauth2.scope}")
    private String scope;

    @Value("${test.oauth2.client-secret}")
    private String clientSecret;

    @Value("${test.oauth2.user-authorization-uri}")
    private String authorizationUri;

    @Value("${test.oauth2.access-token-uri}")
    private String accessTokenUri;

    @Value("${test.oauth2.resource.userInfoUri}")
    private String userInfoUri;

    @Autowired
    private RestTemplate restTemplate;

    /**
     * 获取参数auth验证方法(第一步)
     *
     * @param request request
     * @return 跳转用户授权页面
     */
    @RequestMapping("/clientLogin")
    public ModelAndView clientLogin(HttpServletRequest request) {
        HttpSession session = request.getSession();
        String accessToken = (String) session.getAttribute(CommonConstants.SESSION_ACCESS_TOKEN);
        if (!org.thymeleaf.util.StringUtils.isEmpty(accessToken)) {
            return new ModelAndView("redirect:/user/userIndex");
        }
        // 当前系统登录成功之后的回调URL(前端传)
        String redirectUrl = request.getParameter("redirectUrl");
        // 当前系统请求认证服务器成功之后返回的Authorization Code
        String code = request.getParameter("code");
        // 最后重定向的URL
        String resultUrl = "redirect:";
        //当前请求路径
        String currentUrl = request.getRequestURL().toString();
        // code为空，则说明当前请求不是认证服务器的回调请求，则重定向URL到百度OAuth2.0登录
        if (StringUtils.isEmpty(code)) {
            //如果存在回调URL，则将这个URL添加到session
            if (!StringUtils.isEmpty(redirectUrl)) {
                session.setAttribute("redirectUrl", redirectUrl);
            }

            //生成随机的状态码，这里暂时写死，用于防止CSRF攻击
            resultUrl += MessageFormat.format(authorizationUri, clientId, "gsfsdg", currentUrl);
        } else {
            // 1. 通过Authorization Code获取Access Token
            AuthorizationResponse token = this.token(request);
            // 2. 如果正常返回
            String access_token;
            if (token != null && !StringUtils.isEmpty(access_token = token.getAccess_token())) {
                // 2.1 将Access Token存到session
                session.setAttribute(CommonConstants.SESSION_ACCESS_TOKEN, access_token);
                // 2.2 再次查询用户基础信息，并将用户ID存到session
                // session.setAttribute(Constants.SESSION_USER_ID,userId);
            }

            // 3. 从session中获取回调URL，并返回
            redirectUrl = (String) session.getAttribute("redirectUrl");
            session.removeAttribute("redirectUrl");
            if (!StringUtils.isEmpty(redirectUrl)) {
                resultUrl += redirectUrl;
            } else {
                resultUrl += "/user/userIndex";
            }
        }

        return new ModelAndView(resultUrl);
    }

    @ResponseBody
    @RequestMapping(value = "/token")
    public AuthorizationResponse token(HttpServletRequest request) {
        AuthorizationResponse result = new AuthorizationResponse();
        // 一些验证
        try {
            // 模拟查询到消息
            //校验请求的客户端秘钥和已保存的秘钥是否匹配
            //校验回调URL
            //从Redis获取允许访问的用户权限范围
            //从Redis获取对应的用户信息,此处模拟
            User user = new User();
            //如果能够通过Authorization Code获取到对应的用户信息，则说明该Authorization Code有效
            if (!StringUtils.isEmpty(scope) && user != null) {
                //过期时间
                //生成Access Token
                //生成Refresh Token
                //返回数据
                result.setAccess_token("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxODEwMjkxNjEwNDY3NjMwYzEyMDAyMzI5NjMiLCJ1c2VySUQiOiIxODEwMjkxNjEwNDY3NjMwYzEyMDAyMzI5NjMwIiwiY29tcGFueUlEIjoiMTkwMTA4MDkzMTU5MzM1MGMwNDAwMjM0MTU5OSIsInVzZXJBY2NvdW50IjoiMTgxMTM3NjkwMjcwIiwicGFydG5lckNvZGUiOiIwNCIsImlhdCI6MTU5MDcxNDQ0OCwiZXhwIjoxNTkzMzA2NDQ4fQ.AmbEIItN9AMQ7kSxYmETH3uPUU435y8dqpn2JGtnjeA");
                result.setRefresh_token("Bearer eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiIxODEwMjkxNjEwNDY3NjMwYzEyMDAyMzI5NjMiLCJ1c2VySUQiOiIxODEwMjkxNjEwNDY3NjMwYzEyMDAyMzI5NjMwIiwiY29tcGFueUlEIjoiMTkwMTA4MDkzMTU5MzM1MGMwNDAwMjM0MTU5OSIsInVzZXJBY2NvdW50IjoiMTgxMTM3NjkwMjcwIiwicGFydG5lckNvZGUiOiIwNCIsImlhdCI6MTU5MDcxNDQ0OCwiZXhwIjoxNTkzMzA2NDQ4fQ.AmbEIItN9AMQ7kSxYmETH3uPUU435y8dqpn2JGtnjeA");
                result.setExpires_in(122344235L);
                result.setScope(scope);
                return result;
            } else {
                return result;
            }
        } catch (Exception e) {
            return result;
        }
    }
}
