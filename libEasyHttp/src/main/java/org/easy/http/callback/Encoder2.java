package org.easy.http.callback;

import org.easy.http.map.Container;

/**
 * 请求header和参数的一起回调
 */
public interface Encoder2 {
    /**
     * 加密回调
     *
     * @param headers 请求头
     * @param params  参数列表
     */
    void encode(Container headers, Container params);
}
