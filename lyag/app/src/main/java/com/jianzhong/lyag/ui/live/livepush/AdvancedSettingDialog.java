package com.jianzhong.lyag.ui.live.livepush;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.TextView;

import com.jianzhong.lyag.R;

/**
 * Created by apple on 2016/12/16.
 */

public class AdvancedSettingDialog extends DialogFragment {
    private static final String TAG = AdvancedSettingDialog.class.getName();
    public static final int EVENT_BEAUTY_LEVEL_CHANGED = 1; //美颜级别切换

    public static final String KEY_BEAUTY_LEVEL = "beauty-level";

    private Spinner mBeautyLevelView;   //美颜级别选择

    private AdvancedSettingListener mAdvancedSettingListener;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_advance_setting, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mBeautyLevelView = (Spinner) view.findViewById(R.id.spinner_beauty_level);
        mBeautyLevelView.setOnItemSelectedListener(new BeautyLevelSelectListener());
    }

    private class BeautyLevelSelectListener implements Spinner.OnItemSelectedListener{

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if(view != null && view instanceof TextView) {
                TextView textView = (TextView) view;
                Integer value = Integer.parseInt(textView.getText().toString());
                Log.d(TAG, "selected value " + value);
                Bundle bundle = new Bundle();
                bundle.putInt(KEY_BEAUTY_LEVEL, value);
                if (mAdvancedSettingListener != null) {
                    mAdvancedSettingListener.onAdvancedSettingChange(EVENT_BEAUTY_LEVEL_CHANGED, bundle);
                }
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {
            mBeautyLevelView.setSelection(mBeautyLevelView.getCount() - 1); //默认选择最高级别
        }
    }

    public interface AdvancedSettingListener {
        void onAdvancedSettingChange(int event, Bundle bundle);
    }

    public void setAdvancedSettingListener(AdvancedSettingListener listener) {
        this.mAdvancedSettingListener = listener;
    }

}
