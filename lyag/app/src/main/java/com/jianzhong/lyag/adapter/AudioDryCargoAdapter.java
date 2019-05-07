package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.view.View;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.AudioDetailModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 每日音频列表适配器
 * Created by zhengwencheng on 2018/1/16 0016.
 * package com.jianzhong.bs.adapter
 */

public class AudioDryCargoAdapter extends CommonAdapter<AudioDetailModel> {
//    private OnAudioListener onAudioListener;
    public AudioDryCargoAdapter(Context context, List<AudioDetailModel> datas) {
        super(context, R.layout.item_dry_cargo, datas);

    }

    @Override
    protected void convert(ViewHolder holder, final AudioDetailModel item, final int position) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_duration_sec, CommonUtils.secToTime2(Integer.valueOf(item.getDuration_sec())));
        if(item.getIsPlay() == 0){
            holder.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.color_333333));
            holder.setImageResource(R.id.iv_play,R.drawable.homepage_plays);
        }else {
            holder.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.color_theme));
            holder.setImageResource(R.id.iv_play,R.drawable.homepage_stops);
        }

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mDatas.size(); i++) {
                    if(i != position){
                        mDatas.get(i).setIsPlay(0);
                    }else {
                        if(mDatas.get(position).getIsPlay() == 1){
                            mDatas.get(position).setIsPlay(0);
                        }else {
                            mDatas.get(position).setIsPlay(1);
                        }
                    }
                }
                notifyDataSetChanged();

                EventBus.getDefault().post(mDatas.get(position));
            }
        });
    }
}
