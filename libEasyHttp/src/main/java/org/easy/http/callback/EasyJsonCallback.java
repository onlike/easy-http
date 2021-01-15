package org.easy.http.callback;

/**
 * 直接回调json到应用层
 */
public abstract class EasyJsonCallback implements EasyCallback {
    @Override
    public void failed(int code, String string, Throwable t) {

    }
}
