package com.baselib.util;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/2/8.
 */

public class ResultList<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    private int code;
    private String message;
    private List<T> data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<T> getData() {
        return data;
    }

    public void setData(List<T> data) {
        this.data = data;
    }

    @Override
    public String toString() {
        return "ResultList{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", data=" + data +
                '}';
    }

}