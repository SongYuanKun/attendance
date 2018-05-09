package com.songyuankun.common;

/**
 * 响应
 *
 * @author Administrator
 */
public class ResponseObject {
    private int code;
    private String message;
    private Object data;

    public ResponseObject(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public ResponseObject() {
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResponseObject [code=" + this.code + ", message=" + this.message + ", data=" + this.data + "]";
    }
}