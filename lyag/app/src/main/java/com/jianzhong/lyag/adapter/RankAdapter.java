package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.widget.ImageView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.RankModel;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
/**
 * 排行榜适配器
 * create by zhengwencheng on 2018/3/21 0021
 * package com.jianzhong.bs.adapter
 */
public class RankAdapter extends CommonAdapter<RankModel> {
    public RankAdapter(Context context, List<RankModel> datas) {
        super(context, R.layout.item_rank, datas);
    }

    @Override
    protected void convert(ViewHolder holder, RankModel item, int position) {
        holder.setText(R.id.tv_rank,position+"");
        if(position == 1 || position == 2 || position == 3){
            holder.getView(R.id.tv_rank).setBackground(mContext.getResources().getDrawable(R.drawable.shape_rank_theme));
            holder.setTextColor(R.id.tv_rank,mContext.getResources().getColor(R.color.color_white));
        }else {
            holder.getView(R.id.tv_rank).setBackground(mContext.getResources().getDrawable(R.drawable.shape_rank_white));
            holder.setTextColor(R.id.tv_rank,mContext.getResources().getColor(R.color.color_333333));
        }
        if(item.getUser() != null){
            GlideUtils.loadAvatarImage((ImageView) holder.getView(R.id.iv_avatar),item.getUser().getAvatar());
            holder.setText(R.id.tv_name,item.getUser().getRealname());
        }
        holder.setText(R.id.tv_credit,item.getRank_factor());
    }
}
