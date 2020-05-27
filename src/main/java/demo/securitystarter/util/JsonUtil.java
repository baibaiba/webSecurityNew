package demo.securitystarter.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public final class JsonUtil {

    /**
     * 把Java对象转换成json字符串，为null的属性将会忽略
     *
     * @param object 待转化为JSON字符串的Java对象
     * @return json 串 or null
     */
    public static String parseObjToJson(Object object) {
        return JSONObject.toJSONString(object, SerializerFeature.WriteDateUseDateFormat);
    }

    /**
     * 将Json字符串信息转换成对应的Java对象
     * 注意反序列化时为对象时，必须要有默认无参的构造函数，否则会报异常
     * com.alibaba.fastjson.JSONException: default constructor not found
     *
     * @param json json字符串对象
     * @param c    对应的类型
     */
    public static <T> T parseJsonToObj(String json, Class<T> c) {
        JSONObject jsonObject = JSON.parseObject(json);
        return JSON.toJavaObject(jsonObject, c);
    }
}
