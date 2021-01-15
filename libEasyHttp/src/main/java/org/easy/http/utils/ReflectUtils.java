package org.easy.http.utils;

import android.text.TextUtils;

import com.alibaba.fastjson.util.ParameterizedTypeImpl;

import org.easy.http.callback.EasyEntityCallback;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.util.ArrayList;
import java.util.List;

public class ReflectUtils {

    /**
     * 反射获取所有泛型的class对象
     * <p>
     * 思路：
     * 这里有两种情况，一种是直接使用 EasyEntityCallback<T> 类，一种是继承 EasyEntityCallback<T> 类，做自己的子类
     * <p>
     * 1.直接使用EasyEntityCallback的话，相对来说简单一点，直接获取父类上的泛型信息，遍历添加到容器内即可
     * EasyEntityCallback<Base<Content>>
     * 这种情况直接遍历获取，泛型，添加 rClass 里，[0]=Base，[1]=Content
     * <p>
     * 2.继承EasyEntityCallback的话，会稍微复杂一点，需要先保存子类上的泛型，然后提升父类，放父类解析到 TypeVariable
     * 的时候，直接把子类所有类型合并过来
     * CustomCallBack<Content> extends EasyEntityCallback<Base<T>>
     * 这种情况，先获取 Content 存储在 childrenClass 容器内，然后升级父类到 EasyEntityCallback 上，继续解析泛型信息
     * 会得到Base，解析下一个节点是T类型就是TypeVariable，这时候需要把 childrenClass 上的内容拿过来替换 T
     * 最终得到 [0]=Base ，[1]=T (实际替换成 childrenClass 内的元素)
     */
    public static List<Class<?>> reflectEntity(EasyEntityCallback<?> callback) {
        List<Class<?>> rClass = new ArrayList<>();
        Class<?> tClass = callback.getClass();
        //子类的泛型容器
        List<Class<?>> childrenClass = new ArrayList<>();
        while (tClass != null && tClass != Object.class) {
            Type t = tClass.getGenericSuperclass();
            //判断是否是子类，如果是子类则先不添加到rClass内
            boolean isChild = !TextUtils.equals(tClass.getSuperclass() == null ? "" : tClass.getSuperclass().getName(),
                    EasyEntityCallback.class.getName());
            //处理类上的泛型信息
            while (t instanceof ParameterizedType) {
                t = ((ParameterizedType) t).getActualTypeArguments()[0];
                //代表还有其他泛型，不是最终节点
                if (t instanceof ParameterizedType) {
                    if(isChild){
                        childrenClass.add((Class<?>) ((ParameterizedType) t).getRawType());
                        continue;
                    }
                    rClass.add((Class<?>) ((ParameterizedType) t).getRawType());
                }
                //代表有子类，该类型取子类的所有泛型
                if (t instanceof TypeVariable) {
                    rClass.addAll(childrenClass);
                    childrenClass.clear();
                }
                //终节点，判断callback是否有继承
                if (t instanceof Class) {
                    if (isChild) {
                        childrenClass.add((Class<?>) t);
                        continue;
                    }
                    rClass.add((Class<?>) t);
                }
            }
            tClass = tClass.getSuperclass();
        }
        return rClass;
    }

    /**
     * 重新构建泛型结构
     */
    public static Type buildType(List<Class<?>> types) {
        ParameterizedTypeImpl beforeType = null;
        if (types == null) return null;
        if (types.size() == 1) {
            return new ParameterizedTypeImpl(new Type[]{types.get(0)}, null, null);
        }
        for (int i = types.size() - 1; i > 0; i--) {
            beforeType = new ParameterizedTypeImpl(new Type[]{beforeType == null ? types.get(i) : beforeType}, null, types.get(i - 1));
        }
        return beforeType;
    }
}
