package com.example.hejianbin.bmc.setting;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by hejianbin on 6/1/15.
 */
public class Endpoints {
    private static final Map<String, Map<Product, String>> confs = new HashMap<>();

    static {
        Map<Product, String> sandbox = new TreeMap<>();
        sandbox.put(Product.BOS, "http://10.105.97.15/");
        sandbox.put(Product.BMC, "http://multimedia.bce-testinternal.baidu.com");
        confs.put("qa_sandbox", sandbox);

        Map<Product, String> online = new TreeMap<>();
        online.put(Product.BOS, "http://bj.bcebos.com");
        online.put(Product.BMC, "http://media.bj.baidubce.com");
        confs.put("online_bj", online);
    }

    public static String getEndpoint(String env, Product product) {
        return confs.get(env).get(product);
    }
}
