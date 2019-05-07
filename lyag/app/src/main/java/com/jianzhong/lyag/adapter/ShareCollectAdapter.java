package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.CollectModel;
import com.jianzhong.lyag.ui.interact.ShareDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 互动收藏列表适配器
 * create by zhengwencheng on 2018/3/17 0017
 * package com.jianzhong.bs.adapter
 */
public class ShareCollectAdapter extends CommonAdapter<CollectModel> {
    private int isEdit;
    public ShareCollectAdapter(Context context, List<CollectModel> datas, int isEdit) {
        super(context, R.layout.item_interact, datas);

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

        if(item.getShare() != null){
            holder.setText(R.id.tv_title,item.getShare().getTitle());
            holder.setText(R.id.tv_content,item.getShare().getContent());
            holder.setText(R.id.tv_tag, CommonUtils.getTagStr(item.getShare().getTag()));
            holder.setText(R.id.tv_like,item.getShare().getLike_num());
            holder.setText(R.id.tv_comment,item.getShare().getComment_num());
        }
        holder.setVisible(R.id.tv_from,false);
        holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getCreate_at()));


        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
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
                    EventBus.getDefault().post(AppConstants.TAG_SHARE);
                    notifyDataSetChanged();
                }else {
                    Intent intent = new Intent(mContext, ShareDetailActivity.class);
                    intent.putExtra("share_id",item.getForeign_id());
                    mContext.startActivity(intent);
                }

            }
        });
    }

    public void setIsEdit(int isEdit){
        this.isEdit = isEdit;
    }
}
