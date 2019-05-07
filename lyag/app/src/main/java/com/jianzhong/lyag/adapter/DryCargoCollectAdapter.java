package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.CollectModel;
import com.jianzhong.lyag.ui.exam.DryCargoDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 专栏收藏列表适配器
 * Created by zhengwencheng on 2018/1/29 0029.
 * package com.jianzhong.bs.adapter
 */

public class DryCargoCollectAdapter extends CommonAdapter<CollectModel> {
    private int isEdit;
    public DryCargoCollectAdapter(Context context, List<CollectModel> datas,int isEdit) {
        super(context, R.layout.item_drycargo_collect, datas);
        this.isEdit = isEdit;
    }

    @Override
    protected void convert(ViewHolder holder, final CollectModel item, int position) {

        if(item.getIsSelected() == 1){
            holder.setChecked(R.id.cb_check,true);
        }else {
            holder.setChecked(R.id.cb_check,false);
        }

        if(isEdit == 1){
            holder.setVisible(R.id.cb_check,true);
        }else {
            holder.setVisible(R.id.cb_check,false);
        }

        if(item.getAudio() != null){
            holder.setText(R.id.tv_dry_title, item.getAudio().getTitle())
                    .setText(R.id.tv_dry_time, CommonUtils.getDryTime(item.getCreate_at()))
                    .setText(R.id.tv_dry_duration, "时长：" + CommonUtils.secToTime2(Integer.valueOf(item.getAudio().getDuration_sec())));
        }

        //分享
        holder.setOnClickListener(R.id.tv_dry_share, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(mContext, "分享一下");
            }
        });

        //播放
        holder.setOnClickListener(R.id.iv_play, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ToastUtils.show(mContext, "播放一下");
            }
        });

        //查看详情
        holder.setOnClickListener(R.id.ll_dry_cargo, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit == 1){
                    if(item.getIsSelected() == 1){
                        item.setIsSelected(0);
                        for (String s: GroupVarManager.getInstance().mCollectList) {
                            if(s.equals(item.getItem_id())){
                                GroupVarManager.getInstance().mCollectList.remove(s);
                                break;
                            }
                        }
                    }else {
                        item.setIsSelected(1);
                        GroupVarManager.getInstance().mCollectList.add(item.getItem_id());
                    }
                    EventBus.getDefault().post(AppConstants.TAG_AUDIO);
                    notifyDataSetChanged();
                }else {
                    Intent intent = new Intent(mContext, DryCargoDetailActivity.class);
                    intent.putExtra("audio_id",item.getForeign_id());
                    mContext.startActivity(intent);
                }
            }
        });

    }

    public void setIsEdit(int isEdit){
        this.isEdit = isEdit;
    }
}
