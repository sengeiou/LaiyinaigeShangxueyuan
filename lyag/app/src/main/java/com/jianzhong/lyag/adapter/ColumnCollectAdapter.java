package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.CollectModel;
import com.jianzhong.lyag.ui.exam.ColumnDetailActivity;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 专栏收藏列表适配器
 * Created by zhengwencheng on 2018/1/29 0029.
 * package com.jianzhong.bs.adapter
 */

public class ColumnCollectAdapter extends CommonAdapter<CollectModel> {

    private int isEdit;

    public ColumnCollectAdapter(Context context, List<CollectModel> datas, int isEdit) {
        super(context, R.layout.item_column_collect, datas);

        this.isEdit = isEdit;
    }

    @Override
    protected void convert(ViewHolder holder, final CollectModel item, int position) {

        if (item.getIsSelected() == 1) {
            holder.setChecked(R.id.cb_check, true);
        } else {
            holder.setChecked(R.id.cb_check, false);
        }

        if (isEdit == 1) {
            holder.setVisible(R.id.cb_check, true);
        } else {
            holder.setVisible(R.id.cb_check, false);
        }

        if (item.getColumn() != null) {
            GlideUtils.load((ImageView) holder.getView(R.id.iv_special), item.getColumn().getCover());
            holder.setText(R.id.tv_special_title, item.getColumn().getTitle())
                    .setVisible(R.id.tv_special_explain, false)
                    .setText(R.id.tv_special_update_time, item.getColumn().getUpdate_day())
                    .setText(R.id.tv_special_subscribe, "已有" + item.getColumn().getPlay_num() + "人观看");

            if (item.getColumn().getExpert() != null) {
                if (!TextUtils.isEmpty(item.getColumn().getExpert().getRealname()) && !TextUtils.isEmpty(item.getColumn().getExpert().getPos_duty())) {
                    holder.setText(R.id.tv_special_identify, item.getColumn().getExpert().getRealname() + " | " + item.getColumn().getExpert().getPos_duty());
                } else if (!TextUtils.isEmpty(item.getColumn().getExpert().getRealname()) && TextUtils.isEmpty(item.getColumn().getExpert().getPos_duty())) {
                    holder.setText(R.id.tv_special_identify, item.getColumn().getExpert().getRealname());
                } else if (TextUtils.isEmpty(item.getColumn().getExpert().getRealname()) && !TextUtils.isEmpty(item.getColumn().getExpert().getPos_duty())) {
                    holder.setText(R.id.tv_special_identify, item.getColumn().getExpert().getPos_duty());
                }
            }
            if (!TextUtils.isEmpty(item.getColumn().getNow_ep()) && !TextUtils.isEmpty(item.getColumn().getTotal_ep())){
                holder.setText(R.id.tv_special_update, item.getColumn().getNow_ep() + "|" + item.getColumn().getTotal_ep());
            }else if (!TextUtils.isEmpty(item.getColumn().getNow_ep()) && TextUtils.isEmpty(item.getColumn().getTotal_ep())){
                holder.setText(R.id.tv_special_update, item.getColumn().getNow_ep());
            }else if (TextUtils.isEmpty(item.getColumn().getNow_ep()) && !TextUtils.isEmpty(item.getColumn().getTotal_ep())){
                holder.setText(R.id.tv_special_update, item.getColumn().getTotal_ep());
            }
        }

        holder.setOnClickListener(R.id.ll_special, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isEdit == 1) {
                    if (item.getIsSelected() == 1) {
                        item.setIsSelected(0);
                        for (String s : GroupVarManager.getInstance().mCollectList) {
                            if (s.equals(item.getItem_id())) {
                                GroupVarManager.getInstance().mCollectList.remove(s);
                                break;
                            }
                        }
                    } else {
                        item.setIsSelected(1);
                        GroupVarManager.getInstance().mCollectList.add(item.getItem_id());
                    }
                    EventBus.getDefault().post(AppConstants.TAG_COLUMN);
                    notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(mContext, ColumnDetailActivity.class);
                    intent.putExtra("column_id", item.getForeign_id());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public void setIsEdit(int isEdit) {
        this.isEdit = isEdit;
    }
}
