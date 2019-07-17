package com.hpc.frame.network.response;

/**
 * @author : HPC
 * @description :  返回结果实体
 * @date : 2019/7/12 14:31
 */
public class Response<T> {
    private String code;
    private T data;
    private String msg;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
