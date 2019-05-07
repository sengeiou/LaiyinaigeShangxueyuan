package com.jianzhong.lyag.http;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.model.HttpHeaders;
import com.lzy.okgo.model.HttpParams;

/**
 * Created by zhengwencheng on 2017/2/6.
 * package com.jianzhong.ys.http
 */

public class HttpUtils {

    private static volatile HttpUtils instance = null;

    public HttpUtils(){

    }

    public synchronized static HttpUtils getInstance(){
        if(instance == null){
            synchronized (HttpUtils.class){
                if(instance == null){
                    instance = new HttpUtils();
                }
            }
        }
        return instance;
    }

    public void init(Application application){
        HttpHeaders headers = new HttpHeaders();
//        headers.put("commonHeaderKey1", "commonHeaderValue1");    //所有的 header 都 不支持 中文
//        headers.put("commonHeaderKey2", "commonHeaderValue2");
        HttpParams params = new HttpParams();
//        params.put("commonParamsKey1", "commonParamsValue1");     //所有的 params 都 支持 中文
//        params.put("commonParamsKey2", "这里支持中文参数");

        //初始化
        OkGo.init(application);
        //以下都不是必须的，根据需要自行选择
        OkGo.getInstance()//
                .setCertificates()
                .debug("HttpUtils")                                              //是否打开调试
                .setConnectTimeout(OkGo.DEFAULT_MILLISECONDS)               //全局的连接超时时间
                .setReadTimeOut(OkGo.DEFAULT_MILLISECONDS)                  //全局的读取超时时间
                .setWriteTimeOut(OkGo.DEFAULT_MILLISECONDS)                 //全局的写入超时时间
                //.setCookieStore(new MemoryCookieStore())                           //cookie使用内存缓存（app退出后，cookie消失）
                //.setCookieStore(new PersistentCookieStore())                       //cookie持久化存储，如果cookie不过期，则一直有效
                .addCommonHeaders(headers)                                         //设置全局公共头
                .addCommonParams(params);                                          //设置全局公共参数
    }
}
