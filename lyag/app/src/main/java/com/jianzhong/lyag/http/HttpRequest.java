package com.jianzhong.lyag.http;

import com.baselib.util.GsonUtils;
import com.baselib.util.LogUtil;
import com.baselib.util.PreferencesUtils;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.util.CommonUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.model.HttpParams;
import com.lzy.okgo.request.BaseRequest;

import java.io.File;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by zhengwencheng on 2017/2/6.
 * package com.jianzhong.ys.http
 */

public class HttpRequest {
    private static volatile HttpRequest instance = null;

    public HttpRequest() {

    }

    public static HttpRequest getInstance() {
        if (instance == null) {
            synchronized (HttpRequest.class) {
                if (instance == null) {
                    instance = new HttpRequest();
                }
            }
        }
        return instance;
    }

    /***
     * 无参数网络请求
     *
     * @param url
     * @param httpCallBack
     */
    public void post(String url, HttpCallBack httpCallBack) {
        post(url, null, httpCallBack);
    }

    /**
     * 有参数请求
     *
     * @param url
     * @param params
     * @param httpCallBack
     */
    public void post(String url, Map<String, Object> params, final HttpCallBack httpCallBack) {
        //增加参数
        HttpParams reqParams = new HttpParams();
        if (!url.equals(HttpConfig.URL_BASE + HttpConfig.URL_LOGIN)) {
            reqParams.put("access_token", PreferencesUtils.getString(BaseApplication.getInstance(), AppConstants.KEY_ACCESS_TOKEN));
        }
        if (params != null) {
            for (Map.Entry<String, Object> entry : params.entrySet()) {
                if (entry.getValue() instanceof File) {
                    reqParams.put(entry.getKey(), (File) entry.getValue());
                } else if (entry.getValue() instanceof List) {
                    reqParams.putFileParams(entry.getKey(), (List<File>) entry.getValue());
                } else if (entry.getValue() instanceof Integer) {
                    reqParams.put(entry.getKey(), entry.getValue() + "");
                } else {
                    reqParams.put(entry.getKey(), (String) entry.getValue());
                }
            }
        }

        LogUtil.e("postInfo",GsonUtils.toJson(reqParams));
        //Http请求
        OkGo.post(url)
                .tag(this)
                .params(reqParams)
                .execute(new StringCallback() {
                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        try {
//                            Logger.d(s);
                            if (httpCallBack != null) {
                                CommonUtils.checkLogin(s);
                                httpCallBack.onSuccess(s);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Call call, Response response, Exception e) {
                        try {
                            if (httpCallBack != null) {
                                if (e.toString().contains("Unable to resolve host") || e.toString().contains("Failed to connect")) {
                                    httpCallBack.onFailure("网络异常，请检查网络是否连通");
                                } else {
                                    httpCallBack.onFailure(e.toString());
                                }
                            }
                        } catch (Exception exception) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onBefore(BaseRequest request) {
                        if (httpCallBack != null)
                            httpCallBack.onStart();
                    }

                    @Override
                    public void onAfter(String s, Exception e) {
                        if (httpCallBack != null)
                            httpCallBack.onFinish();
                    }

                });
    }
}
