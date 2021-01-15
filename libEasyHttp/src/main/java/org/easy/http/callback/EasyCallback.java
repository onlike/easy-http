package org.easy.http.callback;

/**
 * 回调
 */
public interface EasyCallback {

    /**
     * 成功回调，HTTP CODE = 200的时候会触发这个回调
     *
     * @param json 返回json
     */
    void success(String json);

    /**
     * 失败回调，HTTP CODE 非200的情况
     *
     * @param code   HTTP 响应码
     * @param string 返回数据，有可能是json，有可能是html错误页面的标签
     * @param t      错误异常，可能为空
     */
    void failed(int code, String string, Throwable t);
}
