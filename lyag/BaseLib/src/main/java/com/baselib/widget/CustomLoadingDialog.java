package com.baselib.widget;



import android.app.AlertDialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.baselib.R;

/**
 * 网络请求菊花进度条，呵呵
 * @author chenzhikai
 *
 */
public class CustomLoadingDialog extends AlertDialog {

	private String message;
	private TextView messageText;

	public CustomLoadingDialog(Context context) {
		super(context);
	}

	public CustomLoadingDialog(Context context, String message) {
		super(context);
		this.message = message;
	}

	public CustomLoadingDialog(Context context, int theme) {
		super(context, theme);
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.layout_loading_dialog);
		setCanceledOnTouchOutside(false);
		messageText = (TextView) findViewById(R.id.tv_loading);
		if (message != null) {
			messageText.setText(message);
		}

	}

}
