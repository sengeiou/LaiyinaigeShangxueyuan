package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.view.View;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.RecordUrlModel;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 课时分段列表适配器
 * Created by Max on 2018/2/27.
 */

public class LiveRecordAdapter extends CommonAdapter<RecordUrlModel> {
    public LiveRecordAdapter(Context context, List<RecordUrlModel> datas) {
        super(context, R.layout.item_section, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final RecordUrlModel item, final int position) {

        holder.setVisible(R.id.iv_download,false);
        holder.setText(R.id.tv_title,item.getTitle());
//        holder.setText(R.id.tv_duration_sec, CommonUtils.secToTime(Integer.valueOf(item.getDuration_sec())));

        if(item.getIsPlay() == 1){
            holder.setImageDrawable(R.id.iv_play,mContext.getResources().getDrawable(R.drawable.spbf_stop2));
        }else {
            holder.setImageDrawable(R.id.iv_play,mContext.getResources().getDrawable(R.drawable.spbf_play2));
        }


        holder.setOnClickListener(R.id.iv_play, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EventBus.getDefault().post(item);
            }
        });

    }
}
