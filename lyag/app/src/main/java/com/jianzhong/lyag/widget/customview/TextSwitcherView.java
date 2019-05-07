package com.jianzhong.lyag.widget.customview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.listener.OnImporetInfoListener;
import com.jianzhong.lyag.model.NoticeMsgModel;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by zhengwencheng on 2018/4/12 0012.
 * package com.jianzhong.bs.widget.customview
 */

public class TextSwitcherView extends TextSwitcher implements ViewSwitcher.ViewFactory{

    private List<NoticeMsgModel> reArrayList = new ArrayList<>();
    private int resIndex = 0;
    private final int UPDATE_TEXTSWITCHER = 1;
    private int timerStartAgainCount = 0;
    private Context mContext;
    private OnImporetInfoListener onImporetInfoListener;
    private NoticeMsgModel item;
    public TextSwitcherView(Context context) {
        super(context);

        mContext = context;
        init();
    }

    public TextSwitcherView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();

    }

    private void init(){
        this.setFactory(this);
        this.setInAnimation(getContext(), R.anim.vertical_in);
        this.setOutAnimation(getContext(), R.anim.vertical_out);
        Timer timer = new Timer();
        timer.schedule(timerTask, 1,3000);
    }
    TimerTask timerTask =  new TimerTask() {

        @Override
        public void run() {   //不能在这里创建任何UI的更新，toast也不行
            Message msg = new Message();
            msg.what = UPDATE_TEXTSWITCHER;
            handler.sendMessage(msg);
        }
    };

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXTSWITCHER:
                    updateTextSwitcher();
                    break;
                default:
                    break;
            }

        };
    };
    /**
     * 需要传递的资源
     * @param reArrayList
     */
    public void getResource(List<NoticeMsgModel> reArrayList) {
        this.reArrayList = reArrayList;
    }

    public void updateTextSwitcher() {
        if (this.reArrayList != null && this.reArrayList.size()>0) {
            item = reArrayList.get(resIndex);
            this.setText(this.reArrayList.get(resIndex++).getTitle());
            if (resIndex > this.reArrayList.size()-1) {
                resIndex = 0;
            }

        }

    }
    @Override
    public View makeView() {
        TextView tView = new TextView(getContext());
        tView.setSingleLine(true);
        tView.setEllipsize(TextUtils.TruncateAt.END);
        tView.setTextSize(14);
        tView.setTextColor(mContext.getResources().getColor(R.color.color_333333));

//        WaterTextView tView = new WaterTextView(getContext());
//        tView.setSingleLine(true);
//        tView.setEllipsize(TextUtils.TruncateAt.MARQUEE);
//        tView.setPadding(getResources().getDimensionPixelOffset(R.dimen.default_margin_10),
//                getResources().getDimensionPixelOffset(R.dimen.default_margin_10),
//                getResources().getDimensionPixelOffset(R.dimen.default_margin_10),
//                getResources().getDimensionPixelOffset(R.dimen.default_margin_10));
//        tView.setTextSize(14);
//        tView.setMarqueeRepeatLimit(1);
        tView.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                onImporetInfoListener.OnImportInfo(item);
            }
        });
        return tView;
    }

    public void setOnImportInfoClick(OnImporetInfoListener onImporetInfoListener){
        this.onImporetInfoListener = onImporetInfoListener;
    }
}
