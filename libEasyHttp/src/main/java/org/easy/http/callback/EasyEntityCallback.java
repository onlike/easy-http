package org.easy.http.callback;

import com.alibaba.fastjson.JSON;

import org.easy.http.utils.ReflectUtils;

import java.util.List;

/**
 * 根据泛型解析返回的数据格式
 *
 * @param <T> 实体
 */
public abstract class EasyEntityCallback<T> extends EasyJsonCallback {

    @SuppressWarnings("unchecked")
    @Override
    public void success(String json) {
        List<Class<?>> rClass = ReflectUtils.reflectEntity(this);
        T t = JSON.parseObject(json, ReflectUtils.buildType(rClass));
        success(t);
    }

    /**
     * 回调方法
     *
     * @param entity 实体类
     */
    public abstract void success(T entity);

}
