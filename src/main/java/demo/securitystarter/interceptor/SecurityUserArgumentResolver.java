package demo.securitystarter.interceptor;

import com.alibaba.fastjson.JSONObject;
import demo.securitystarter.annotation.SecurityUser;
import demo.securitystarter.service.SecurityTestService;
import demo.securitystarter.util.JsonUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.Date;

public class SecurityUserArgumentResolver implements HandlerMethodArgumentResolver {
    private static final String CONTENT_TYPE_JSON = "application/json";

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        // 仅作用添加了@SecurityUser注解的方法
        return methodParameter.hasParameterAnnotation(SecurityUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter methodParameter, ModelAndViewContainer modelAndViewContainer, NativeWebRequest nativeWebRequest, WebDataBinderFactory webDataBinderFactory) throws Exception {
        HttpServletRequest request = nativeWebRequest.getNativeRequest(HttpServletRequest.class);
        HttpServletResponse response = nativeWebRequest.getNativeResponse(HttpServletResponse.class);
        if (request == null || response == null) {
            throw new Exception("请求无效");
        }

        // 获取body中的json
        BufferedReader br = request.getReader();
        String str;
        StringBuilder wholeStr = new StringBuilder();
        while ((str = br.readLine()) != null) {
            wholeStr.append(str);
        }

        String requestJson = wholeStr.toString();
        String contentType = request.getHeader("Content-Type");
        JSONObject jsonObject = null;
        // 暂时仅支持post的json请求方式
        if (CONTENT_TYPE_JSON.equals(contentType)) {
            if (StringUtils.isEmpty(requestJson)) {
                throw new Exception("参数为空");
            }

            // 序列化
            jsonObject = JsonUtil.parseJsonToObj(requestJson, JSONObject.class);
        }

        // 利用反射给目标方法参数赋值
        Class<?> parameterType = methodParameter.getParameterType();
        Field[] declaredFields = parameterType.getDeclaredFields();
        Object object = parameterType.newInstance();
        for (Field declaredField : declaredFields) {
            declaredField.setAccessible(true);
            Class<?> type = declaredField.getType();
            declaredField.set(object, propertyValue(jsonObject, type, declaredField.getName()));
        }

        return object;
    }

    /**
     * 根据类型获取值，目前仅支持以下几种类型
     *
     * @param jsonObject
     * @param type
     * @param key
     * @return
     */
    private Object propertyValue(JSONObject jsonObject, Class<?> type, String key) {
        if (type.equals(String.class)) {
            return jsonObject.getString(key);
        }

        if (type.equals(Integer.class)) {
            return jsonObject.getInteger(key);
        }

        if (type.equals(Boolean.class)) {
            return jsonObject.getBoolean(key);
        }

        if (type.equals(BigDecimal.class)) {
            return jsonObject.getBigDecimal(key);
        }

        if (type.equals(Date.class)) {
            return jsonObject.getDate(key);
        }

        return null;
    }
}
