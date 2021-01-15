package org.easy.http.config;

import android.annotation.SuppressLint;
import android.content.Context;

import org.easy.http.callback.EasyContainerCallBack;
import org.easy.http.callback.Encoder;
import org.easy.http.callback.Encoder2;
import org.easy.http.cookie.EasyCookie;
import org.easy.http.map.Container;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Cookie;
import okhttp3.OkHttpClient;

public class EasyOptions {
    /**
     * 默认超时时间
     */
    public static final int TIMEOUT = 30;

    /**
     * 缓存大小
     */
    private static final long DEFAULT_CACHE_SIZE = 5 * 1024 * 1024;

    private static final String CACHE_DIR = "EasyCache";

    @SuppressLint("StaticFieldLeak")
    private static EasyOptions options = new EasyOptions();
    /**
     * header 加密回调
     */
    private Encoder globalHeaderEnCoder;
    /**
     * 参数加密回调
     */
    private Encoder globalParamsEncoder;

    /**
     * header和参数的一起回调
     */
    private Encoder2 globalParamsAndHeaderEncoder;

    /**
     * 获取全局参数列表
     */
    private EasyContainerCallBack globalParamsCallBack;

    /**
     * 获取全局Header列表
     */
    private EasyContainerCallBack globalHeaderCallBack;
    /**
     * 扩展的httpClient对象
     */
    private OkHttpClient globalHttpClient;
    /**
     * 全局参数
     */
    private Container globalParams = new Container();
    /**
     * 全局Header头信息
     */
    private Container globalHeaders = new Container();

    /**
     * Application Context
     */
    private Context mContext;

    /**
     * 追加cookie
     */
    private List<Cookie> extraCookies = new ArrayList<>();

    public static EasyOptions get() {
        options.defaultHttpClient();
        return options;
    }

    /**
     * 配置全局参数
     *
     * @param key   参数key
     * @param value 参数value
     */
    public void addGlobalParam(String key, Object value) {
        globalParams.add(key, value);
    }

    /**
     * 追加cookie
     */
    public void setExtraCookies(List<Cookie> cookies) {
        this.extraCookies.clear();
        this.extraCookies.addAll(cookies);
    }

    /**
     * 获取额外cookie信息
     */
    public List<Cookie> getExtraCookies() {
        return extraCookies;
    }

    /**
     * 配置全局Header头信息
     *
     * @param key   参数key
     * @param value 参数value
     */
    public void addGlobalHeader(String key, Object value) {
        globalHeaders.add(key, value);
    }

    /**
     * 设置全局参数加密器
     *
     * @param globalParamsEncoder 加密器
     */
    public void setGlobalParamsEncoder(Encoder globalParamsEncoder) {
        this.globalParamsEncoder = globalParamsEncoder;
    }

    /**
     * 获取动态全局请求头
     */
    public EasyContainerCallBack getGlobalHeaderCallBack() {
        return globalHeaderCallBack;
    }

    /**
     * 获取动态全局请求参数
     */
    public EasyContainerCallBack getGlobalParamsCallBack() {
        return globalParamsCallBack;
    }

    /**
     * 动态设置全局的请求参数
     */
    public void setGlobalParamsCallBack(EasyContainerCallBack globalParamsCallBack) {
        this.globalParamsCallBack = globalParamsCallBack;
    }

    /**
     * 动态设置全局请求头
     */
    public void setGlobalHeaderCallBack(EasyContainerCallBack globalHeaderCallBack) {
        this.globalHeaderCallBack = globalHeaderCallBack;
    }

    /**
     * 参数和header头的一起回调
     */
    public void setGlobalParamsAndHeaderEncoder(Encoder2 globalParamsAndHeaderEncoder) {
        this.globalParamsAndHeaderEncoder = globalParamsAndHeaderEncoder;
    }

    /**
     * 参数和header的回调
     */
    public Encoder2 getGlobalParamsAndHeaderEncoder() {
        return globalParamsAndHeaderEncoder;
    }

    /**
     * 设置全局header 头加密器
     *
     * @param globalHeaderEnCoder 加密器
     */
    public void setGlobalHeaderEnCoder(Encoder globalHeaderEnCoder) {
        this.globalHeaderEnCoder = globalHeaderEnCoder;
    }

    /**
     * 获取全局头信息
     */
    public Container getGlobalHeaders() {
        return globalHeaders;
    }

    /**
     * 获取全局参数信息
     */
    public Container getGlobalParams() {
        return globalParams;
    }

    /**
     * 获取全局header加密器
     */
    public Encoder getGlobalHeaderEnCoder() {
        return globalHeaderEnCoder;
    }

    /**
     * 获取全局参数加密器
     */
    public Encoder getGlobalParamsEncoder() {
        return globalParamsEncoder;
    }

    /**
     * 设置扩展httpClient
     */
    public void setGlobalHttpClient(OkHttpClient globalHttpClient) {
        this.globalHttpClient = globalHttpClient;
    }

    /**
     * 获取扩展httpClient
     */
    public OkHttpClient getGlobalHttpClient() {
        return globalHttpClient;
    }

    /**
     * 设置默认的httpClient
     */
    public void defaultHttpClient() {
        if (globalHttpClient == null) {
            globalHttpClient = new OkHttpClient.Builder()
                    .connectTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .readTimeout(TIMEOUT, TimeUnit.SECONDS)
                    .cookieJar(new EasyCookie())
//                    .cache(new Cache(new File(getContext().getExternalCacheDir(), CACHE_DIR), DEFAULT_CACHE_SIZE))
                    .build();
        }
    }

    /**
     * 获取上下文对象
     */
    public Context getContext() {
        return mContext;
    }

    /**
     * 设置上下文，取Application的上下文
     */
    public void setContext(Context context) {
        if (context == null) return;
        this.mContext = context;
        if (context != context.getApplicationContext()) {
            this.mContext = context.getApplicationContext();
        }
    }
}
