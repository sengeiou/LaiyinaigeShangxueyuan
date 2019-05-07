package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.InteractModel;
import com.jianzhong.lyag.ui.interact.HelpDetailActivity;
import com.jianzhong.lyag.ui.interact.ShareDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
/**
 * 互动首页列表适配器
 * create by zhengwencheng on 2018/3/17 0017
 * package com.jianzhong.bs.adapter
 */
public class InteractAdapter extends CommonAdapter<InteractModel> {
    public InteractAdapter(Context context, List<InteractModel> datas) {
        super(context, R.layout.item_interact, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final InteractModel item, int position) {
        holder.setText(R.id.tv_title,item.getTitle());
        if(item.getIs_elite().equals("1")){
            Drawable drawable= mContext.getResources().getDrawable(R.drawable.hd_jh);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView)holder.getView(R.id.tv_title)).setCompoundDrawables(drawable,null,null,null);
        }else {
            ((TextView)holder.getView(R.id.tv_title)).setCompoundDrawables(null,null,null,null);
        }

        if(item.getFrom().equals("share")){
            holder.setText(R.id.tv_from,"来自分享");
        }else if(item.getFrom().equals("help")){
            holder.setText(R.id.tv_from,"来自求助");
        }
        if(item.getUser() != null){
            holder.setText(R.id.tv_identify,item.getUser().getRealname()+" | "+ item.getUser().getPos_name()+" | "+item.getUser().getBranch_name());
        }
        holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getCreate_at()));
        holder.setText(R.id.tv_content,item.getContent());
        holder.setText(R.id.tv_tag,CommonUtils.getTagStr(item.getTag()));
        holder.setText(R.id.tv_comment,item.getComment_num());
        holder.setText(R.id.tv_like,item.getLike_num());

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(item.getFrom().equals("share")){
                    Intent intent = new Intent(mContext, ShareDetailActivity.class);
                    intent.putExtra("share_id",item.getForeign_id());
                    mContext.startActivity(intent);
                }else if(item.getFrom().equals("help")){
                    Intent intent = new Intent(mContext, HelpDetailActivity.class);
                    intent.putExtra("help_id",item.getForeign_id());
                    mContext.startActivity(intent);
                }
            }
        });
    }
}
