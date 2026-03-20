package com.example.utils;

public class Result {
    private int code;
    private String message;
    private Object data;

    public Result(int code, String message, Object data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    // 成功响应
    public static Result success(Object data) {
        return new Result(200, "Success", data);
    }

    // 错误响应
    public static Result error(String message) {
        return new Result(500, message, null);
    }

    // 获取状态码
    public int getCode() {
        return code;
    }

    // 设置状态码
    public void setCode(int code) {
        this.code = code;
    }

    // 获取消息
    public String getMessage() {
        return message;
    }

    // 设置消息
    public void setMessage(String message) {
        this.message = message;
    }

    // 获取数据
    public Object getData() {
        return data;
    }

    // 设置数据
    public void setData(Object data) {
        this.data = data;
    }
}