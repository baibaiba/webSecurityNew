package demo.securitystarter.annotation;

import java.lang.reflect.Method;

public class AnnotationParse {
    /**
     * 解析注解
     *
     * @param targetClassName 目标类（Class形式）
     * @param methodName      目标方法（在客户端调用哪个方法，methodName就代表哪个方法）
     * @return
     * @throws Exception
     */
    public static HasRole.Role parse(Class targetClassName, String methodName) throws Exception {
        //获得目标方法
        Method method = targetClassName.getMethod(methodName);
        HasRole.Role methodAccess = null;
        //判断目标方法上面是否存在@PrivilegeInfo注解
        //@Privilege（name="savePerson"）
        if (method.isAnnotationPresent(HasRole.class)) {
            // 得到方法上的注解
            HasRole hasRole = method.getAnnotation(HasRole.class);
            // 得到注解中的name值
            methodAccess = hasRole.name();
        }

        return methodAccess;
    }
}
