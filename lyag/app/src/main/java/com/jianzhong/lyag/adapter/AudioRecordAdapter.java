package com.jianzhong.lyag.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.baselib.util.ActivityManager;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.AudioRecorModel;
import com.jianzhong.lyag.ui.exam.DryCargoDetailActivity;
import com.jianzhong.lyag.ui.user.history.HistoryAudioActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 专栏收藏列表适配器
 * Created by zhengwencheng on 2018/1/29 0029.
 * package com.jianzhong.bs.adapter
 */

public class AudioRecordAdapter extends CommonAdapter<AudioRecorModel> {
    public AudioRecordAdapter(Context context, List<AudioRecorModel> datas) {
        super(context, R.layout.item_audio_record, datas);

    }

    @Override
    protected void convert(ViewHolder holder, final AudioRecorModel item, int position) {


            holder.setText(R.id.tv_dry_title, item.getTitle())
                    .setText(R.id.tv_dry_time, CommonUtils.getDryTime(item.getCreate_at()))
                    .setText(R.id.tv_dry_duration, "时长：" + CommonUtils.secToTime2(Integer.valueOf(item.getDuration_sec())));


        //查看详情
        holder.setOnClickListener(R.id.ll_dry_cargo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(GroupVarManager.getInstance().mAudioDetailModel != null && GroupVarManager.getInstance().mAudioDetailModel.getAudio_id().equals(item.getAudio_id())){
                    ((Activity)mContext).finish();
                }else {

                    Intent intent = new Intent(mContext, DryCargoDetailActivity.class);
                    intent.putExtra("audio_id",item.getAudio_id());
                    mContext.startActivity(intent);
                    ActivityManager.getActivityManager().finishActivity(HistoryAudioActivity.class);
                    ActivityManager.getActivityManager().finishActivity(DryCargoDetailActivity.class);
                }

            }
        });

    }
}
