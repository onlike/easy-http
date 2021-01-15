package org.easy.http.core;

import android.app.Activity;

import org.easy.http.HttpCode;
import org.easy.http.callback.EasyCallback;
import org.easy.http.callback.Encoder;
import org.easy.http.callback.Encoder2;
import org.easy.http.config.EasyOptions;
import org.easy.http.map.Container;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

public class Http {

    public static final int REQUEST_TYPE_GET = 1;
    public static final int REQUEST_TYPE_POST = 2;
    public static final int REQUEST_TYPE_POST_JSON = 3;

    private static final MediaType JSON_MEDIA_TYPE = MediaType.parse("application/json; charset=utf-8");
    private String mUrl;
    private Container mParams;
    private Container mHeaders;
    private EasyCallback easyCallback;
    private Encoder headerEncoder;
    private Encoder paramsEncoder;
    private Encoder2 paramsAndHeaderEncoder;
    private int mRequestType;
    private Activity activity;

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public void setParams(Container mParams) {
        this.mParams = mParams;
    }

    public void setHeaders(Container mHeaders) {
        this.mHeaders = mHeaders;
    }

    public void setEasyCallback(EasyCallback easyCallback) {
        this.easyCallback = easyCallback;
    }

    public void setHeaderEncoder(Encoder headerEncoder) {
        this.headerEncoder = headerEncoder;
    }

    public void setParamsEncoder(Encoder paramsEncoder) {
        this.paramsEncoder = paramsEncoder;
    }

    public void setParamsAndHeaderEncoder(Encoder2 paramsAndHeaderEncoder) {
        this.paramsAndHeaderEncoder = paramsAndHeaderEncoder;
    }

    public void setRequestType(int mRequestType) {
        this.mRequestType = mRequestType;
    }

    public void setActivity(Activity activity) {
        this.activity = activity;
    }

    private Request.Builder getRequestBuilder() {
        Request.Builder builder = new Request.Builder();
        builder.url(mUrl);
        if (EasyOptions.get().getGlobalParamsCallBack() != null) {
            Container globalContainer = EasyOptions.get()
                    .getGlobalParamsCallBack().getGlobalContainer(mParams);
            if (globalContainer != null) {
                mParams.addAll(globalContainer);
            }
        }
        if (headerEncoder != null) {
            headerEncoder.encode(mHeaders);
        }
        if (EasyOptions.get().getGlobalHeaderCallBack() != null) {
            Container globalHeaders = EasyOptions.get()
                    .getGlobalHeaderCallBack().getGlobalContainer(mHeaders);
            if (globalHeaders != null) {
                mHeaders.addAll(globalHeaders);
            }
        }
        if (paramsEncoder != null) {
            paramsEncoder.encode(mParams);
        }
        if (paramsAndHeaderEncoder != null) {
            paramsAndHeaderEncoder.encode(mHeaders, mParams);
        }
        //处理请求头
        for (Map.Entry<String, Object> item : mHeaders.entrySet()) {
            builder.addHeader(item.getKey(), item.getValue() == null ? "" : item.getValue().toString());
        }
        //普通的get请求
        if (mRequestType == REQUEST_TYPE_GET) {
            StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Object> item : mParams.entrySet()) {
                try {
                    String key = URLEncoder.encode(item.getKey(), "UTF-8");
                    String value = URLEncoder.encode(item.getValue() == null ? "" : item.getValue().toString(), "UTF-8");
                    sb.append(key).append("=").append(value).append("&");
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
            mUrl = mUrl + (mUrl.contains("?") ? "&" : "?") + sb.toString();
            builder.url(mUrl);
        }

        //表单请求
        if (mRequestType == REQUEST_TYPE_POST) {
            FormBody.Builder formBody = new FormBody.Builder();
            for (Map.Entry<String, Object> item : mParams.entrySet()) {
                formBody.add(item.getKey(), item.getValue() == null ? "" : item.getValue().toString());
            }
            builder.post(formBody.build());
        }
        //json请求
        if (mRequestType == REQUEST_TYPE_POST_JSON) {
            RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, mParams.toJson());
            builder.post(body);
        }
        return builder;
    }

    /**
     * 执行异步请求
     */
    public void performAsync() {
        //发起请求
        EasyOptions.get().getGlobalHttpClient().newCall(getRequestBuilder().build()).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (easyCallback != null) {
                            easyCallback.failed(-1, null, e);
                        }
                    }
                });
            }

            @Override
            public void onResponse(Call call, final Response response) throws IOException {
                ResponseBody responseBody = response.body();
                final int code = response.code();
                final String string = responseBody == null ? null : responseBody.string();
                activity.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (code == HttpCode.HTTP_200) {
                            if (easyCallback != null) {
                                easyCallback.success(string);
                            }
                        } else {
                            if (easyCallback != null) {
                                easyCallback.failed(response.code(), string, null);
                            }
                        }
                    }
                });

            }
        });
    }

    /**
     * 同步请求
     */
    public String perform() {
        Response response = null;
        try {
            response = EasyOptions.get().getGlobalHttpClient().newCall(getRequestBuilder().build()).execute();
            return response.body().string();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
