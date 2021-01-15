package org.easy.http;

import android.app.Activity;
import android.text.TextUtils;

import org.easy.http.callback.EasyCallback;
import org.easy.http.callback.Encoder;
import org.easy.http.callback.Encoder2;
import org.easy.http.config.EasyOptions;
import org.easy.http.core.Http;
import org.easy.http.map.Container;

public class Easy {

    private String mUrl;
    private Container mParams;
    private Container mHeaders;
    private EasyCallback easyCallback;
    private Encoder globalHeaderEnCoder;
    private Encoder globalParamsEncoder;
    private Encoder2 globalHeaderAndParamsEncoder;
    private Activity activity;

    private Easy() {

    }

    /**
     * 发送 GET 请求
     */
    public void get() {
        performRequest(Http.REQUEST_TYPE_GET);
    }

    /**
     * 发送 POST 请求，form-data 方式， 表单提交
     */
    public void post() {
        performRequest(Http.REQUEST_TYPE_POST);
    }

    /**
     * 发送 POST 请求，application/json 方式，json提交
     */
    public void postJson() {
        performRequest(Http.REQUEST_TYPE_POST_JSON);
    }

    /**
     * 执行http请求
     */
    private void performRequest(int requestType) {
        Http http = new Http();
        http.setUrl(mUrl);
        http.setParams(mParams);
        http.setHeaders(mHeaders);
        http.setEasyCallback(easyCallback);
        http.setHeaderEncoder(globalHeaderEnCoder);
        http.setParamsEncoder(globalParamsEncoder);
        http.setParamsAndHeaderEncoder(globalHeaderAndParamsEncoder);
        http.setRequestType(requestType);
        http.setActivity(activity);
        http.performAsync();
    }

    public static class Builder {
        private String mUrl;
        private Container mHeader = new Container(EasyOptions.get().getGlobalHeaders());
        private Container mParams = new Container(EasyOptions.get().getGlobalParams());
        private Encoder globalHeaderEnCoder = EasyOptions.get().getGlobalHeaderEnCoder();
        private Encoder globalParamsEncoder = EasyOptions.get().getGlobalParamsEncoder();
        private EasyCallback easyCallback;
        private Activity activity;
        private Encoder2 globalHeaderAndParamsEncoder = EasyOptions.get().getGlobalParamsAndHeaderEncoder();

        public Builder(Activity activity) {
            this.activity = activity;
            EasyOptions.get().setContext(activity.getApplicationContext());
        }

        /**
         * 请求地址
         */
        public Builder url(String url) {
            this.mUrl = url;
            return this;
        }

        /**
         * 添加一组请求头
         */
        public Builder addHeaders(Container container) {
            this.mHeader.addAll(container);
            return this;
        }

        /**
         * 添加请求头
         */
        public Builder addHeader(String key, Object value) {
            this.mHeader.add(key, value);
            return this;
        }

        /**
         * 如果value为空，或者空字符串，则忽略该添加请求
         */
        public Builder addNotNullHeader(String key, Object value) {
            if (value == null || (value instanceof String && TextUtils.isEmpty(value.toString()))) {
                return this;
            }
            this.mHeader.add(key, value);
            return this;
        }

        /**
         * 添加一组参数
         */
        public Builder addParams(Container container) {
            this.mParams.addAll(container);
            return this;
        }

        /**
         * 添加普通参数
         */
        public Builder addParam(String key, Object value) {
            this.mParams.add(key, value);
            return this;
        }

        /**
         * 如果value为空，或者空字符串，则忽略该添加请求
         */
        public Builder addNotNullParam(String key, Object value) {
            if (value == null || (value instanceof String && TextUtils.isEmpty(value.toString()))) {
                return this;
            }
            this.mParams.add(key, value);
            return this;
        }

        /**
         * 请求头加密回调
         */
        public Builder globalHeaderEnCoder(Encoder encoder) {
            this.globalHeaderEnCoder = encoder;
            return this;
        }

        /**
         * 参数加密回调
         */
        public Builder globalParamsEncoder(Encoder encoder) {
            this.globalParamsEncoder = encoder;
            return this;
        }

        /**
         * 响应回调
         */
        public Builder response(EasyCallback callback) {
            this.easyCallback = callback;
            return this;
        }

        /**
         * 参数和头的混合加密回调
         */
        public Builder globalParamsAndHeaderEncoder(Encoder2 encoder2) {
            this.globalHeaderAndParamsEncoder = encoder2;
            return this;
        }

        public Easy build() {
            Easy easy = new Easy();
            easy.mUrl = this.mUrl;
            easy.mHeaders = this.mHeader;
            easy.mParams = this.mParams;
            easy.globalHeaderEnCoder = this.globalHeaderEnCoder;
            easy.globalParamsEncoder = this.globalParamsEncoder;
            easy.easyCallback = this.easyCallback;
            easy.globalHeaderAndParamsEncoder = this.globalHeaderAndParamsEncoder;
            easy.activity = this.activity;
            return easy;
        }
    }

}
