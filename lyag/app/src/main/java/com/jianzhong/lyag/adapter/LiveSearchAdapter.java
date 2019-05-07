package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.LiveModel;
import com.jianzhong.lyag.ui.live.LiveDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 直播列表的适配器
 * Created by zhengwencheng on 2018/1/17 0017.
 * package com.jianzhong.bs.adapter
 */

public class LiveSearchAdapter extends CommonAdapter<LiveModel> {
    public LiveSearchAdapter(Context context, List<LiveModel> datas) {
        super(context, R.layout.item_direct_seeding, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final LiveModel item, int position) {
        holder.setText(R.id.tv_title,item.getTitle());
        if(item.getExpert() != null){
            holder.setText(R.id.tv_identity,item.getExpert().getRealname()+"/"+item.getExpert().getPos_duty());
        }
        holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getStart_at()));
        GlideUtils.load((ImageView) holder.getView(R.id.iv_direct_seeding),item.getCover());
        if(item.getIs_publish().equals("0")){ //预告
             holder.setImageDrawable(R.id.iv_status,mContext.getResources().getDrawable(R.drawable.homepage_label1));
        }else if(item.getIs_publish().equals("1")){ //直播
            holder.setImageDrawable(R.id.iv_status,mContext.getResources().getDrawable(R.drawable.homepage_label2));
        }else {
            holder.setImageDrawable(R.id.iv_status,mContext.getResources().getDrawable(R.drawable.homepage_label3));
        }

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,LiveDetailActivity.class);
                intent.putExtra("live_id",item.getLive_id());
                mContext.startActivity(intent);
            }
        });
    }
}
