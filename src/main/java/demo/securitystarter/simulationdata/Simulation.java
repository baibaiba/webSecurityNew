package demo.securitystarter.simulationdata;

import demo.securitystarter.dto.ClientDetail;

import java.util.HashMap;
import java.util.Map;

public class Simulation {
    public static Map<String, ClientDetail> clients = new HashMap<>();

    static {
        // 模拟注册，添加客户端id和密钥
        clients.put("aaa111bbb", new ClientDetail("aaa111bbb", "模拟客户端",
                "fegr&$453IYIYfs", "一个模拟数据"));
    }
}
