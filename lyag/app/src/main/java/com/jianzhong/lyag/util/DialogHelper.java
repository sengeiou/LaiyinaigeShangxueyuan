package com.jianzhong.lyag.util;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;

import com.baselib.widget.CustomLoadingDialog;


public class DialogHelper {
	
	private static CustomLoadingDialog mLoadingDialog;
		
	// 弹出dialog
	public static void showLoadingDialog(Context context) {
		showLoadingDialog(context, null);
	}

	public static void showLoadingDialog(Context context, String msg) {
		mLoadingDialog = new CustomLoadingDialog(context, msg);
		mLoadingDialog.show();
	}

	// 取消dialog
	public static void dismissLoadingDialog() {
		if (mLoadingDialog != null) {
			mLoadingDialog.dismiss();
			mLoadingDialog = null;
		}
	}
	
	private static ProgressDialog mProgressDialog;
	
	// 显示进度条
	public static void showDialog(Context context) {
		showDialog(context, null);
	}
	
	public static void showDialog(Context context, String msg) {
		if (null == msg) {
    		msg = "加载中...";
    	}
		mProgressDialog = new ProgressDialog(context);
		mProgressDialog.setCanceledOnTouchOutside(false);
    	mProgressDialog.setMessage(msg);
    	mProgressDialog.show();
	}

    // 关闭进度条
    public static void dismissDialog() {
        if (null != mProgressDialog && mProgressDialog.isShowing()) {
        	mProgressDialog.dismiss();
        	mProgressDialog = null;
        }
    }

	/**
	 * 提示对话框
	 * @param context
	 * @param title
	 * @param message
	 * @param positive
	 */
	public static void showDialogPositive(Context context, String title, String message, DialogInterface.OnClickListener positive){
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setCancelable(false);
		builder.setPositiveButton("确定", positive);
		builder.show();
	}

	/**
	 * 提示对话框
	 * @param context
	 * @param title
	 * @param message
	 * @param positive
     */
	public static void showDialog(Context context, String title, String message, DialogInterface.OnClickListener positive){
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialogInterface, int i) {
				dialogInterface.dismiss();
			}
		});
		builder.setPositiveButton("确定", positive);
		builder.show();
	}

	/**
	 * 提示对话框
	 * @param context
	 * @param title
	 * @param message
	 * @param positive
     * @param negative
     */
	public static void showDialog(Context context, String title, String message, DialogInterface.OnClickListener positive, DialogInterface.OnClickListener negative){
		android.support.v7.app.AlertDialog.Builder builder = new android.support.v7.app.AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setMessage(message);
		builder.setNegativeButton("取消", negative);
		builder.setPositiveButton("确定", positive);
		builder.show();
	}


}
