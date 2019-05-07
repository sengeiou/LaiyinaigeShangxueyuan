package com.jianzhong.lyag.http;

/**
 * Created by zhengwencheng on 2017/2/6.
 * package com.jianzhong.ys.http
 */

public abstract class HttpCallBack {
    public void onSuccess(String s){};
    public void onFailure(String msg){};
    public void onStart(){};
    public void onFinish(){};
}
