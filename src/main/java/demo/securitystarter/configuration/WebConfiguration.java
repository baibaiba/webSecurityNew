//package demo.securitystarter.configuration;
//
//import demo.securitystarter.interceptor.LoginInterceptor;
//import demo.securitystarter.interceptor.SecurityUserArgumentResolver;
//import demo.securitystarter.service.SecurityService;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.web.method.support.HandlerMethodArgumentResolver;
//import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//import java.util.List;
//
//@Configuration
//public class WebConfiguration implements WebMvcConfigurer {
//    private final SecurityService securityTestService;
//
//    public WebConfiguration(SecurityService securityTestService) {
//        this.securityTestService = securityTestService;
//    }
//
//    /**
//     * 注册拦截器，对请求做拦截，它执行在HandlerAdapter（真正处理请求）前后
//     *
//     * @param registry InterceptorRegistry
//     */
//    @Override
//    public void addInterceptors(InterceptorRegistry registry) {
//        registry.addInterceptor(new LoginInterceptor(securityTestService)).excludePathPatterns("/login");
//    }
//
//    /**
//     * 注册消息转换器，对请求参数进行映射
//     *
//     * @param resolvers HandlerMethodArgumentResolver
//     */
//    @Override
//    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
//        resolvers.add(new SecurityUserArgumentResolver());
//    }
//}
