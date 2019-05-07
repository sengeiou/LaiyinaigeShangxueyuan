package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.AudioModel;
import com.jianzhong.lyag.ui.exam.DryCargoDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.io.Serializable;
import java.util.List;

/**
 * 每日音频列表适配器
 * Created by zhengwencheng on 2018/1/16 0016.
 * package com.jianzhong.bs.adapter
 */

public class AudioSearchAdapter extends CommonAdapter<AudioModel> {
    public AudioSearchAdapter(Context context, List<AudioModel> datas) {
        super(context, R.layout.item_drycargo_collect, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final AudioModel item, int position) {

        holder.setText(R.id.tv_dry_title, item.getTitle())
                .setText(R.id.tv_dry_time, CommonUtils.getDryTime(item.getCreate_at()))
                .setText(R.id.tv_dry_duration, "时长：" + CommonUtils.secToTime2(Integer.valueOf(item.getDuration_sec())));

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DryCargoDetailActivity.class);
                intent.putExtra("audio_id", item.getAudio_id());
                intent.putExtra("mDatas", (Serializable) mDatas);
                mContext.startActivity(intent);
            }
        });
    }
}
