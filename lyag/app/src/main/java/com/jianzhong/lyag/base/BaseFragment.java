package com.jianzhong.lyag.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.baselib.util.AppEventBus;
import com.baselib.util.ListUtils;
import com.jianzhong.lyag.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseFragment extends Fragment implements AppEventBus.Subject {

    @Nullable
    @BindView(R.id.ll_no_data)
    LinearLayout mLlNoData;
    @Nullable
    @BindView(R.id.ll_error)
    LinearLayout mLlError;
    @Nullable
    @BindView(R.id.rl_loading)
    RelativeLayout mRlLoading;

    public Unbinder unbinder;
    public Context mContext;
    public Activity mActivity;
    public LayoutInflater mInflater;

    public View mRootView;
    public View mActivityRootView;

    protected abstract View initView(LayoutInflater inflater);

    protected void initData() {
    }

    ;

    protected void getData() {
    }

    ;

    protected void bindData() {
    }

    ;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getActivity();
        mActivity = getActivity();
        mInflater = LayoutInflater.from(getActivity());
        AppEventBus.getDefault().register("all", this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        super.onDestroy();
        AppEventBus.getDefault().register("all", this);
        if (unbinder != null) unbinder.unbind();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (mRootView == null) {
            mRootView = initView(inflater);
            unbinder = ButterKnife.bind(this, mRootView);
            initCommonView();
            initData();
        }
        ViewGroup parent = (ViewGroup) mRootView.getParent();
        if (parent != null) {
            parent.removeView(mRootView);
        }
        return mRootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
//		initData();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        //获取activity根视图,rootView设为全局变量
        mActivityRootView = activity.getWindow().getDecorView();
    }

    @Override
    public void onPostAccept(String tag, Object... content) {
    }

    public void initCommonView() {
    }

    public void showCommonLoading() {
        if (mRlLoading != null) {
            mRlLoading.setVisibility(View.VISIBLE);
        }
    }

    public void hideCommonLoading() {
        if (mRlLoading != null) {
            mRlLoading.setVisibility(View.GONE);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
//		StatService.onResume(this);
    }

    @Override
    public void onPause() {
        super.onPause();
//		StatService.onPause(this);
    }

    /**
     * 是否显示无数据页面
     */
    public void isShowNoDataView(Object obj) {
        if (obj == null) {
            showNoData();
            hideError();
            hideCommonLoading();
        } else {
            hideNoData();
        }
        hideError();
    }

    /**
     * 是否显示无数据页面
     */
    public void isShowNoDataView(List list) {
        if (ListUtils.isEmpty(list)) {
            showNoData();
            hideError();
            hideCommonLoading();
        } else {
            hideNoData();
        }
        hideError();
    }

    /**
     * 显示服务器出错页面
     */
    public void showErrorView() {
        showError();
        hideNoData();
        hideCommonLoading();
    }

    /**
     * 显示没数据页面
     */
    public void showNoData() {
        if (mLlNoData != null) {
            mLlNoData.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏没数据页面
     */
    public void hideNoData() {
        if (mLlNoData != null) {
            mLlNoData.setVisibility(View.GONE);
        }
    }

    /**
     * 显示错误页面
     */
    public void showError() {
        if (mLlError != null) {
            mLlError.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 隐藏错误页面
     */
    public void hideError() {
        if (mLlError != null) {
            mLlError.setVisibility(View.GONE);
        }
    }

}
