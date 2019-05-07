package com.jianzhong.lyag.base;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.LayoutRes;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;

import com.baselib.util.ActivityManager;
import com.baselib.util.AppEventBus;
import com.baselib.widget.CustomLoadingDialog;

import butterknife.ButterKnife;
import butterknife.Unbinder;

public abstract class BaseActivity extends AppCompatActivity implements AppEventBus.Subject {

	public Unbinder unbinder;
	public Context mContext;
	public Activity mActivity;
	public LayoutInflater mInflater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		mActivity = this;
		mInflater = LayoutInflater.from(this);
		AppEventBus.getDefault().register("all", this);
		ActivityManager.getActivityManager().addActivity(this);
	}

	@Override
	public void setContentView(@LayoutRes int layoutResID) {
		super.setContentView(layoutResID);
		unbinder = ButterKnife.bind(this);
		initView();
		initData();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	protected void onResume() {
		super.onResume();
//		StatService.onResume(this);
	}

	@Override
	protected void onPause() {
		super.onPause();
//		StatService.onPause(this);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		unbinder.unbind();
		AppEventBus.getDefault().register("all", this);
		ActivityManager.getActivityManager().finishActivity(this);
	}

	public void initView(){}
	public void initData(){}
	public void bindData(){}
	public void getData(){}

	@Override
	public void onPostAccept(String tag, Object... content) {}

	public void showLoadingDialog(){
		CustomLoadingDialog dialog = new CustomLoadingDialog(this);
		dialog.show();
	}



}
