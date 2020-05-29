package demo.securitystarter.aspect;

import demo.securitystarter.annotation.HasRole;
import demo.securitystarter.constants.CommonConstants;
import demo.securitystarter.dto.Base;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.lang.reflect.Method;

@Aspect
@Component
@Order(99)
public class RoleAuthorityAspect {

    @Pointcut("@annotation(demo.securitystarter.annotation.HasRole)")
    public void pointCut() {
    }

    @Around("pointCut()")
    public Object doRound(ProceedingJoinPoint joinPoint) throws Throwable {
        /**
         * 获取当前http请求中的token
         * 解析token :
         * 1、token是否存在
         * 2、token格式是否正确
         * 3、token是否已过期（解析信息或者redis中是否存在）
         *
         */
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = requestAttributes.getRequest();
        HttpSession session = request.getSession();
        String accessToken = (String) session.getAttribute(CommonConstants.SESSION_ACCESS_TOKEN);
        /**
         * 解析token，获取用户角色信息
         */
        // 默认当前用户是管理员
        HasRole.Role userRole = HasRole.Role.ADMIN;

        /**
         * 获取注解的值，并进行权限验证:
         * redis 中是否存在对应的权限
         * redis 中没有则从数据库中获取权限
         * 数据空中没有，抛异常，非法请求，没有权限
         *
         */
        Method method = ((MethodSignature) joinPoint.getSignature()).getMethod();
        HasRole annotation = method.getAnnotation(HasRole.class);
        if (annotation != null) {
            HasRole.Role role = annotation.name();
            if (!role.equals(userRole)) {
                return new Base<>(0, "您无权限");
            }
        }

        return joinPoint.proceed();
    }
}
