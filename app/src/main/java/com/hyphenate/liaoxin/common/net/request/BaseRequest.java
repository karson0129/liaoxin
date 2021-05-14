package com.hyphenate.liaoxin.common.net.request;

public class BaseRequest<T> {

    public int returnCode;
    public T data;
    public String message;

    @Override
    public String toString() {
        return "BaseRequest{" +
                "returnCode=" + returnCode +
                ", data=" + data +
                ", message='" + message + '\'' +
                '}';
    }
}
