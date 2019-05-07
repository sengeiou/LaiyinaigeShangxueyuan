package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.logic.CommonLogic;
import com.jianzhong.lyag.model.HelpModel;
import com.jianzhong.lyag.ui.interact.ShareDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 求助列表适配器
 * create by zhengwencheng on 2018/3/19 0019
 * package com.jianzhong.bs.adapter
 */
public class ShareAdapter extends CommonAdapter<HelpModel> {

    public ShareAdapter(Context context, List<HelpModel> datas) {
        super(context, R.layout.item_help, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final HelpModel item, int position) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getCreate_at()));
        holder.setText(R.id.tv_identify, CommonLogic.getIdentify(item.getUser()));
        holder.setText(R.id.tv_content, item.getContent());
        holder.setText(R.id.tv_tag, CommonUtils.getTagStr(item.getTag()));
        holder.setText(R.id.tv_like, item.getLike_num());
        holder.setText(R.id.tv_comment, item.getComment_num());

        if(item.getIs_elite().equals("1")){
            Drawable drawable= mContext.getResources().getDrawable(R.drawable.hd_jh);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView)holder.getView(R.id.tv_title)).setCompoundDrawables(drawable,null,null,null);
        }else {
            ((TextView)holder.getView(R.id.tv_title)).setCompoundDrawables(null,null,null,null);
        }

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ShareDetailActivity.class);
                intent.putExtra("share_id", item.getShare_id());
                mContext.startActivity(intent);
            }
        });
    }
}
