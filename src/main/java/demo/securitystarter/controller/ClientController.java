package demo.securitystarter.controller;

import demo.securitystarter.constants.CommonConstants;
import demo.securitystarter.dto.AuthorizationResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
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
        // 当前系统登录成功之后的回调URL(前端传)
        String redirectUrl = request.getParameter("redirectUrl");
        // 当前系统请求认证服务器成功之后返回的Authorization Code
        String code = request.getParameter("code");
        // 最后重定向的URL
        String resultUrl = "redirect:";
        HttpSession session = request.getSession();
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
            AuthorizationResponse response = restTemplate.getForObject(accessTokenUri
                    , AuthorizationResponse.class
                    , clientId, clientSecret, code, currentUrl);
            // 2. 如果正常返回
            String access_token;
            if (response != null && !StringUtils.isEmpty(access_token = response.getAccess_token())) {
                // 2.1 将Access Token存到session
                session.setAttribute(CommonConstants.SESSION_ACCESS_TOKEN, access_token);
                // 2.2 再次查询用户基础信息，并将用户ID存到session
//                BaiduUser baiduUser = restTemplate.getForObject(userInfoUri + "?access_token={1}"
//                        ,BaiduUser.class
//                        ,response.getAccess_token());
//                if(baiduUser != null &&  StringUtils.isNoneBlank(baiduUser.getUserid())){
//                    session.setAttribute(Constants.SESSION_USER_ID,baiduUser.getUserid());
//                }
            }

            // 3. 从session中获取回调URL，并返回
            redirectUrl = (String) session.getAttribute("redirectUrl");
            session.removeAttribute("redirectUrl");
            if (!StringUtils.isEmpty(redirectUrl)) {
                resultUrl += redirectUrl;
            } else {
                resultUrl += "/index";
            }
        }

        return new ModelAndView(resultUrl);
    }
}
