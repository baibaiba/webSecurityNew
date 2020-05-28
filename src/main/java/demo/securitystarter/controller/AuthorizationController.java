package demo.securitystarter.controller;

import demo.securitystarter.dto.AuthorizationResponse;
import demo.securitystarter.dto.Authorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.text.MessageFormat;

@Controller
public class AuthorizationController {
    // 按照下面规则拼接请求授权地址，将会引导用户进行账户登录。登录后跳转到授权页面进行授权。
    // 用户授权接口，同意授权返回true，否则false
    public String authorize(Authorize authorize) {
        // 第一步应该是
        // 生成授权code
        String code = "";
        return authorize.getRedirect_uri() + "?code=" + code;
    }


    // 用户怎样授权呢？

    // 2申请令牌接口，认证服务器对客户端进行认证以后，确认无误，同意发放令牌。

    // 3客户端使用令牌，向资源服务器申请获取资源。资源服务器确认令牌无误，同意向客户端开放资源。

    @Autowired
    private RestTemplate restTemplate;

    //    @Value("${baidu.oauth2.client-id}")
    private String clientId = "zUAFlkaiEcvaB378oulGOffo";

    //    @Value("${baidu.oauth2.scope}")
    private String scope = "basic";

    //    @Value("${baidu.oauth2.client-secret}")
    private String clientSecret = "MIsIgQLZoccZHb00FBnvh4NP3wf35aNK";

    //    @Value("${baidu.oauth2.user-authorization-uri}")
    private String authorizationUri = "https://openapi.baidu.com/oauth/2.0/authorize";

    //    @Value("${baidu.oauth2.access-token-uri}")
    private String accessTokenUri = "https://openapi.baidu.com/oauth/2.0/token";

    //    @Value("${baidu.oauth2.resource.userInfoUri}")
    private String userInfoUri = "http://localhost:7080/login";

    private static final String SESSION_ACCESS_TOKEN = "SESSION_ACCESS_TOKEN";

    @RequestMapping("/loginNew")
    public ModelAndView login(HttpServletRequest request) {
        // 当前系统登录成功之后的回调URL
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

            resultUrl += authorizationUri + MessageFormat.format("?client_id={0}&response_type=code&scope=basic&display=popup&redirect_uri={1}"
                    , clientId, currentUrl);
        } else {
            // 1. 通过Authorization Code获取Access Token
            AuthorizationResponse response = restTemplate.getForObject(accessTokenUri + "?client_id={1}&client_secret={2}&grant_type=authorization_code&code={3}&redirect_uri={4}"
                    , AuthorizationResponse.class
                    , clientId, clientSecret, code, currentUrl);
            // 2. 如果正常返回
            String access_token;
            if (response != null && !StringUtils.isEmpty(access_token = response.getAccess_token())) {
                // 2.1 将Access Token存到session
                session.setAttribute(SESSION_ACCESS_TOKEN, access_token);
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
