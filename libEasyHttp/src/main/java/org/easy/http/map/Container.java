package org.easy.http.map;

import com.alibaba.fastjson.JSON;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class Container {

    private Map<String, Object> map = new HashMap<>();

    public Container() {

    }

    public Container(Container container) {
        for (Map.Entry<String, Object> item : container.entrySet()) {
            map.put(item.getKey(), item.getValue());
        }
    }

    public Set<Map.Entry<String, Object>> entrySet() {
        return map.entrySet();
    }

    public void add(String key, Object value) {
        map.put(key, value);
    }

    public void addAll(Container container) {
        map.putAll(container.map);
    }

    public void remove(String key) {
        map.remove(key);
    }

    public boolean containsKey(String key) {
        return map.containsKey(key);
    }

    public String toJson() {
        return JSON.toJSONString(map);
    }
}
