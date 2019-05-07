package com.jianzhong.lyag;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.LinearLayout;

import com.jianzhong.lyag.base.ToolbarActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 公共WebView
 * Created by zhengwencheng on 2018/3/29 0029.
 * package com.jianzhong.bs
 */

public class CommonWebActivity extends ToolbarActivity {

    @BindView(R.id.web_view)
    WebView mWebView;
    @BindView(R.id.head_ll_back)
    LinearLayout mHeadLlBack;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_web);
        ButterKnife.bind(this);
    }

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    public void initData() {
        super.initData();

        String title = getIntent().getStringExtra("title");
        String url = getIntent().getStringExtra("url");

        setHeadTitle(title);
        showHeadTitle();

        // 设置WebView属性，能够执行Javascript脚本
        mWebView.getSettings().setJavaScriptEnabled(true);
        //设置本地调用对象及其接口
        //mWebView.addJavascriptInterface(new JavaScriptObject(), "jsObj");
        // 设置Web视图
        mWebView.setWebViewClient(new PhotoWebViewClient());
        // 加载需要显示的网页
        mWebView.loadUrl(url);

        WebChromeClient wvcc = new WebChromeClient() {
            @Override
            public void onReceivedTitle(WebView view, String title) {
                super.onReceivedTitle(view, title);
            }

        };
        // 设置setWebChromeClient对象
        mWebView.setWebChromeClient(wvcc);

        //WebView的返回键
        mHeadLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mWebView.canGoBack()) {
                    mWebView.goBack();
                } else {
                    CommonWebActivity.this.finish();
                }
            }
        });
    }

    // Web视图
    private class PhotoWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(final WebView view, String url) {
            super.onPageFinished(view, url);
            view.loadUrl("javascript:hideComment()");
            hideCommonLoading();
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            System.out.println("onReceivedError===" + errorCode);
            System.out.println("onReceivedError===" + description);
            System.out.println("onReceivedError===" + failingUrl);
            if (errorCode == -2) {

            }
        }

    }
}
