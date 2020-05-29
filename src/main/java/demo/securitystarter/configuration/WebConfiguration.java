package demo.securitystarter.configuration;

import demo.securitystarter.interceptor.client.ClientAuthInterceptor;
import demo.securitystarter.interceptor.client.ClientLoginInterceptor;
import demo.securitystarter.interceptor.authserver.LoginInterceptor;
import demo.securitystarter.interceptor.authserver.OauthInterceptor;
import demo.securitystarter.interceptor.SecurityUserArgumentResolver;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfiguration implements WebMvcConfigurer {
    /**
     * 注册拦截器，对请求做拦截，它执行在HandlerAdapter（真正处理请求）前后
     *
     * @param registry InterceptorRegistry
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 授权服务端
        registry.addInterceptor(new LoginInterceptor()).addPathPatterns("/user/**", "/oauth2.0/authorizePage", "/oauth2.0/authorize");
        registry.addInterceptor(new OauthInterceptor()).addPathPatterns("/oauth2.0/authorize");
        // 客户端
        registry.addInterceptor(new ClientLoginInterceptor()).addPathPatterns("/user/**");
        registry.addInterceptor(new ClientAuthInterceptor()).addPathPatterns("/clientLogin");
    }

    /**
     * 注册消息转换器，对请求参数进行映射
     *
     * @param resolvers HandlerMethodArgumentResolver
     */
    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(new SecurityUserArgumentResolver());
    }
}
