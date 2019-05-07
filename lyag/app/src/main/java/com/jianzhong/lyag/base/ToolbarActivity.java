package com.jianzhong.lyag.base;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.util.ListUtils;
import com.jianzhong.lyag.R;

import java.util.List;

import butterknife.BindView;

public abstract class ToolbarActivity extends BaseActivity {

    @Nullable
    @BindView(R.id.ll_no_data)
    LinearLayout mLlNoData;
    @Nullable
    @BindView(R.id.ll_error)
    LinearLayout mLlError;
    @Nullable
    @BindView(R.id.head_ll_back)
    public LinearLayout mHeadLlBack;
    @Nullable
    @BindView(R.id.head_iv_right)
    public ImageView mHeadIvRight;
    @Nullable
    @BindView(R.id.head_title)
    public TextView mHeadTitle;
    @Nullable
    @BindView(R.id.head_iv_back)
    ImageView mHeadIvBack;
    @Nullable
    @BindView(R.id.head_left)
    TextView mHeadLeft;
    @Nullable
    @BindView(R.id.head_right)
    public TextView mHeadRight;
    @Nullable
    @BindView(R.id.rl_loading)
    RelativeLayout mRlLoading;
    @Nullable
    @BindView(R.id.rl_toolbar)
    public RelativeLayout mHeadRl;
    @Nullable
    @BindView(R.id.head_ll_title)
    public LinearLayout mHeadLlTitle;
    @Nullable
    @BindView(R.id.head_iv_title_arrow_down)
    ImageView mHeadIvTitleArrowDown;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
    }

    @Override
    public void initData() {
        super.initData();
        showCommonLoading();

        if(mHeadLlBack != null)
            mHeadLlBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    public void setHeadTitle(String title){
        if(mHeadTitle != null && title != null){
            mHeadTitle.setText(title);
        }
    }

    public void showHeadTitle(){
        if(mHeadTitle != null){
            mHeadTitle.setVisibility(View.VISIBLE);
        }
    }

    public void hideHeadTitle(){
        if(mHeadTitle != null){
            mHeadTitle.setVisibility(View.GONE);
        }
    }

    public void showHeadIvRight(){
        if(mHeadIvRight != null){
            mHeadIvRight.setVisibility(View.VISIBLE);
        }
    }

    public void hideHeadIvRight(){
        if(mHeadIvRight != null){
            mHeadIvRight.setVisibility(View.GONE);
        }
    }

    public void setHeadIvRightBackground(int res){
        if(mHeadIvRight != null){
            mHeadIvRight.setBackgroundResource(res);
        }
    }

    public void showHeadBack(){
        if(mHeadIvBack != null){
            mHeadIvBack.setVisibility(View.VISIBLE);
        }
    }

    public void hideHeadBack(){
        if(mHeadIvBack != null){
            mHeadIvBack.setVisibility(View.GONE);
        }
    }

    public void showHeadLeft(){
        if(mHeadLeft != null){
            mHeadLeft.setVisibility(View.VISIBLE);
        }
    }

    public void hideHeadLeft(){
        if(mHeadLeft != null){
            mHeadLeft.setVisibility(View.GONE);
        }
    }

    public void hideHeadLeftInvisible(){
        if(mHeadLeft != null){
            mHeadLeft.setVisibility(View.INVISIBLE);
        }
    }

    public void showHeadRight(){
        if(mHeadRight != null){
            mHeadRight.setVisibility(View.VISIBLE);
        }
    }

    public void hideHeadRight(){
        if(mHeadRight != null){
            mHeadRight.setVisibility(View.GONE);
        }
    }

    public void setHeadRight(String name){
        if(mHeadRight != null){
            mHeadRight.setText(name);
        }
    }

    public void showHeadTitleArrowDown(){
        if(mHeadIvTitleArrowDown != null){
            mHeadIvTitleArrowDown.setVisibility(View.VISIBLE);
        }
    }

    public void hideHeadTitleArrowDown(){
        if(mHeadIvTitleArrowDown != null){
            mHeadIvTitleArrowDown.setVisibility(View.GONE);
        }
    }

    public void hideCommonLoading(){
        if (mRlLoading != null) {
            mRlLoading.setVisibility(View.GONE);
        }
    }

    public void showCommonLoading(){
        if (mRlLoading != null) {
            mRlLoading.setVisibility(View.VISIBLE);
        }
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
