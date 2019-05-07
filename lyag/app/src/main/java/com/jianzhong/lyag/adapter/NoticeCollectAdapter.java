package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.CollectModel;
import com.jianzhong.lyag.ui.notice.NoticeDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 重要通知列表适配器
 * Created by zhengwencheng on 2018/2/26 0026.
 * package com.jianzhong.bs.adapter
 */

public class NoticeCollectAdapter extends CommonAdapter<CollectModel> {
    private int isEdit;
    public NoticeCollectAdapter(Context context, List<CollectModel> datas,int isEdit) {
        super(context, R.layout.item_notice_list, datas);
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

        if(item.getNotice() != null){
            holder.setText(R.id.tv_content,item.getNotice().getContent());
            holder.setText(R.id.tv_sign, CommonUtils.getTagStr(item.getNotice().getTag()));
            holder.setText(R.id.tv_like,item.getNotice().getLike_num());
            holder.setText(R.id.tv_comment,item.getNotice().getComment_num());
            holder.setText(R.id.tv_title,item.getNotice().getTitle());
        }

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
                    EventBus.getDefault().post(AppConstants.TAG_NOTICE);
                    notifyDataSetChanged();
                }else {
                    Intent intent = new Intent(mContext,NoticeDetailActivity.class);
                    intent.putExtra("notice_id",item.getForeign_id());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public void setIsEdit(int isEdit){
        this.isEdit = isEdit;
    }
}
