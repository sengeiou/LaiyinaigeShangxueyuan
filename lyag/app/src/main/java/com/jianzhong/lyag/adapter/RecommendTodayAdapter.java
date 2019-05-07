package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.FocusModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;
/**
 * 每日推荐
 * Created by zhengwencheng on 2018/1/17 0017.
 * package com.jianzhong.bs.adapter
 */

public class RecommendTodayAdapter extends CommonAdapter<FocusModel> {

    public RecommendTodayAdapter(Context context,List<FocusModel> datas) {
        super(context, R.layout.item_recommend_today, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final FocusModel item, int position) {
        if(item != null){
            GlideUtils.load((ImageView) holder.getView(R.id.iv_recommend),item.getCover());
            holder.setText(R.id.tv_title,item.getTitle());
            holder.setText(R.id.tv_identity,item.getExpert().getRealname()+"/" +item.getExpert().getPos_duty());
            holder.setVisible(R.id.tv_duration_sec,true);
            holder.setText(R.id.tv_duration_sec,"总时长："+ CommonUtils.secToTime(Integer.valueOf(item.getDuration_sec())));
            holder.setText(R.id.tv_subscribe,"已有"+item.getPlay_num()+"人观看");

            holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, ClassDetailActivity.class);
                    intent.putExtra("course_id",item.getForeign_id());
                    mContext.startActivity(intent);
                }
            });
        }
    }
}
