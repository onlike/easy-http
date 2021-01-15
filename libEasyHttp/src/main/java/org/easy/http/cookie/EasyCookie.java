package org.easy.http.cookie;

import com.alibaba.fastjson.JSON;

import org.easy.http.config.EasyOptions;
import org.easy.http.utils.Sp;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class EasyCookie implements CookieJar {
    @Override
    public List<Cookie> loadForRequest(HttpUrl httpUrl) {
//        String json = Sp.read(httpUrl.topPrivateDomain());
//        if (TextUtils.isEmpty(json)) {
//            return Collections.emptyList();
//        }
//        List<LocalCookie> localCookies;
//        try {
//            localCookies = JSON.parseArray(json, LocalCookie.class);
//            if (localCookies == null || localCookies.isEmpty()) {
//                return Collections.emptyList();
//            }
//        } catch (Exception ignore) {
//            return Collections.emptyList();
//        }
        //        for (LocalCookie localCookie : localCookies) {
//            list.add(
//                    new Cookie.Builder()
//                            .name(localCookie.name)
//                            .domain(localCookie.domain)
//                            .expiresAt(localCookie.expiresAt)
//                            .path(localCookie.path)
//                            .value(localCookie.value)
//                            .build()
//            );
//        }
        return new ArrayList<>(EasyOptions.get().getExtraCookies());
    }

    @Override
    public void saveFromResponse( HttpUrl httpUrl,  List<Cookie> list) {
//        List<LocalCookie> localCookies = new ArrayList<>();
//        for (Cookie cookie : list) {
//            LocalCookie localCookie = new LocalCookie();
//            localCookie.domain = cookie.domain();
//            localCookie.name = cookie.name();
//            localCookie.path = cookie.path();
//            localCookie.value = cookie.value();
//            localCookie.expiresAt = cookie.expiresAt();
//            localCookies.add(localCookie);
//        }
//        Sp.write(httpUrl.topPrivateDomain(), JSON.toJSONString(localCookies));
    }

    private static class LocalCookie {
        public String name;
        public String path;
        public String value;
        public long expiresAt;
        public String domain;
    }
}
