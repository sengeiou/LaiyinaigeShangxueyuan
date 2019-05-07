package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;

import com.baselib.util.StringUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.CollectModel;
import com.jianzhong.lyag.ui.live.LiveDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 直播收藏列表的适配器
 * Created by zhengwencheng on 2018/1/17 0017.
 * package com.jianzhong.bs.adapter
 */

public class LiveCollectAdapter extends CommonAdapter<CollectModel> {
    private int isEdit;

    public LiveCollectAdapter(Context context, List<CollectModel> datas, int isEdit) {
        super(context, R.layout.item_direct_seeding, datas);

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


        if (item.getLive() != null) {
            holder.setText(R.id.tv_title, item.getLive().getTitle());
            if (item.getLive().getExpert() != null) {
                if (!TextUtils.isEmpty(item.getLive().getExpert().getRealname()) && !TextUtils.isEmpty(item.getLive().getExpert().getPos_duty())) {
                    holder.setText(R.id.tv_identity, item.getLive().getExpert().getRealname() + " | " + item.getLive().getExpert().getPos_duty());
                } else if (!TextUtils.isEmpty(item.getLive().getExpert().getRealname()) && TextUtils.isEmpty(item.getLive().getExpert().getPos_duty())) {
                    holder.setText(R.id.tv_identity, item.getLive().getExpert().getRealname());
                } else if (TextUtils.isEmpty(item.getLive().getExpert().getRealname()) && !TextUtils.isEmpty(item.getLive().getExpert().getPos_duty())) {
                    holder.setText(R.id.tv_identity, item.getLive().getExpert().getPos_duty());
                }
            }
            holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getLive().getStart_at()));
            GlideUtils.load((ImageView) holder.getView(R.id.iv_direct_seeding), item.getLive().getCover());
            if (!StringUtils.isEmpty(item.getLive().getIs_publish())) {
                if (item.getLive().getIs_publish().equals("0")) { //预告
                    holder.setImageDrawable(R.id.iv_status, mContext.getResources().getDrawable(R.drawable.homepage_label1));
                } else if (item.getLive().getIs_publish().equals("1")) { //直播
                    holder.setImageDrawable(R.id.iv_status, mContext.getResources().getDrawable(R.drawable.homepage_label2));
                } else {
                    holder.setImageDrawable(R.id.iv_status, mContext.getResources().getDrawable(R.drawable.homepage_label3));
                }
            }
        }

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
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
                    EventBus.getDefault().post(AppConstants.TAG_LIVE);
                    notifyDataSetChanged();
                } else {
                    Intent intent = new Intent(mContext, LiveDetailActivity.class);
                    intent.putExtra("live_id", item.getLive().getLive_id());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public void setIsEdit(int isEdit) {
        this.isEdit = isEdit;
    }
}
