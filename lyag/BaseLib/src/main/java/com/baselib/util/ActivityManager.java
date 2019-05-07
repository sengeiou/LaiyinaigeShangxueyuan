package com.baselib.util;

import android.app.Activity;
import android.content.Context;

import java.util.Stack;


/**
 * App管理器
 */
public class ActivityManager {

	private static final String TAG = "ActivityManager";

	// 自定义的Activity堆栈
	private static Stack<Activity> activityStack;

	private static ActivityManager appManager;

	/**
	 * 单例模式 将构造方法私有化，然后再向外公共一个获取实例的方 法
	 */
	private ActivityManager() {

	}

	/**
	 * 单例模式 获取当前实例
	 */
	public static ActivityManager getActivityManager() {

		if (appManager == null) {
			appManager = new ActivityManager();
		}
		return appManager;
	}

	/***
	 * 添加Activity到堆栈
	 * 
	 * @param activity
	 */
	public void addActivity(Activity activity) {

		if (activityStack == null) {
			activityStack = new Stack<Activity>();
		}
		activityStack.add(activity);
	}

	/**
	 * 获取当前Activity（堆栈中最后一个压入的）
	 * 
	 * @return
	 */
	public Activity getCurrentActivity() {
		Activity activity = activityStack.lastElement();
		return activity;
	}

	/**
	 * 结束当前Activity（堆栈中最后一个压入的）
	 */
	public void finishActivity() {
		Activity activity = activityStack.lastElement();
		if (activity != null) {

			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定的Activity
	 * 
	 * @param activity
	 */
	public void finishActivity(Activity activity) {
		if (activity != null) {
			activityStack.remove(activity);
			activity.finish();
			activity = null;
		}
	}

	/**
	 * 结束指定类名的Activity
	 * 
	 * @param cls
	 */
	public void finishActivity(Class<?> cls) {
		for (Activity activity : activityStack) {
			if (activity.getClass().equals(cls)) {
				finishActivity(activity);
			}
		}
	}

	/**
	 * 结束所有Activity
	 */
	public void finishAllActivity() {
		for (int i = 0, size = activityStack.size(); i < size; i++) {
			if (null != activityStack.get(i)) {
				activityStack.get(i).finish();
			}
		}
		activityStack.clear();
	}

	/**
	 * 退出应用程序
	 * 
	 * @param context
	 */
	public void exitApp(Context context) {
		try {
			finishAllActivity();
            // 清空数据的操作
			System.exit(0);
		} catch (Exception e) {
		}
	}

}

