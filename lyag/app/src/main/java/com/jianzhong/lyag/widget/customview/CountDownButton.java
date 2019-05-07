package com.jianzhong.lyag.widget.customview;

import android.content.Context;
import android.os.CountDownTimer;
import android.widget.Button;

import com.jianzhong.lyag.R;


public class CountDownButton extends CountDownTimer {
	public static final int TIME_COUNT_FUTURE = 60000;
	public static final int TIME_COUNT_INTERVAL = 1000;
	
	private Context mContext;
	private Button mButton;
	private String mOriginalText;
	
	public CountDownButton() {
		super(TIME_COUNT_FUTURE, TIME_COUNT_INTERVAL);
	}
	
	public CountDownButton(long millisInFuture, long countDownInterval) {
		super(millisInFuture, countDownInterval);
	}
	
	public void init(Context context, Button button) {
		this.mContext = context;
		this.mButton = button;
		this.mOriginalText = mButton.getText().toString();
	}
	
	@Override
	public void onFinish() {
		if (mContext != null && mButton != null) {
			mButton.setClickable(true);
			mButton.setText(mOriginalText);
			mButton.setBackground(mContext.getResources().getDrawable(R.drawable.shape_item_theme_white_16));
		}
	}

	@Override
	public void onTick(long millisUntilFinished) {
		if (mContext != null && mButton != null) {
			mButton.setClickable(false);
			mButton.setText(millisUntilFinished / 1000 + "");
			mButton.setBackground(mContext.getResources().getDrawable(R.drawable.shape_item_theme_white_16));
		}
	}
}