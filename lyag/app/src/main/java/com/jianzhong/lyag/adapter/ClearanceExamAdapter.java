package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.View;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.ExaminationItemModel;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 成长通关试题集
 * Created by zhengwencheng on 2018/2/2 0002.
 * package com.jianzhong.bs.adapter
 */
public class ClearanceExamAdapter extends CommonAdapter<ExaminationItemModel> {
    private int status;

    public ClearanceExamAdapter(Context context, List<ExaminationItemModel> datas, int status) {
        super(context, R.layout.item_clearance_exam, datas);

        this.status = status;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void convert(ViewHolder holder, ExaminationItemModel item, final int position) {
        holder.setText(R.id.tv_num, item.getLabel());
        holder.setText(R.id.tv_title, item.getDescription());
        if (status == 0) {
            holder.setVisible(R.id.tv_num, true);
            if (item.getIsSelected() == 1) {
                holder.getView(R.id.tv_num).setSelected(true);
                holder.getView(R.id.ll_exam).setBackground(mContext.getDrawable(R.drawable.shape_exam_selected));
            } else {
                holder.getView(R.id.tv_num).setSelected(false);
                holder.getView(R.id.ll_exam).setBackground(mContext.getDrawable(R.drawable.shape_exam_unselected));
            }
        } else if (status == 1) { //下一题
            if (item.getIsSelected() == 1 && item.getIs_right().equals("0")) { //错了
                GroupVarManager.getInstance().is_right = 0;
                holder.setVisible(R.id.tv_num, false);
                holder.getView(R.id.ll_exam).setBackground(mContext.getDrawable(R.drawable.tgdt_wrong));
            } else if (item.getIsSelected() == 1 && item.getIs_right().equals("1")) {
                holder.setVisible(R.id.tv_num, false);
                GroupVarManager.getInstance().is_right = 1;
                holder.getView(R.id.ll_exam).setBackground(mContext.getDrawable(R.drawable.tgdt_right));
            } else {
                holder.getView(R.id.tv_num).setSelected(false);
                if (item.getIs_right().equals("1")) {
                    holder.setVisible(R.id.tv_num, false);
                    holder.getView(R.id.ll_exam).setBackground(mContext.getDrawable(R.drawable.tgdt_right));
                } else {
                    holder.setVisible(R.id.tv_num, true);
                    holder.getView(R.id.ll_exam).setBackground(mContext.getDrawable(R.drawable.shape_exam_unselected));
                }
            }

            if(position == (mDatas.size()-1)){
                EventBus.getDefault().post(AppConstants.EVENT_IS_RIGHT);
            }
        } else if (status == 2) { //超时
            if (item.getIs_right().equals("0")) { //错了
                GroupVarManager.getInstance().is_right = 0;
                holder.setVisible(R.id.tv_num, false);
                holder.getView(R.id.ll_exam).setBackground(mContext.getDrawable(R.drawable.tgdt_wrong));
            } else if (item.getIs_right().equals("1")) {
                holder.setVisible(R.id.tv_num, false);
                holder.getView(R.id.ll_exam).setBackground(mContext.getDrawable(R.drawable.tgdt_right));
            }

            if(position == (mDatas.size()-1)){
                EventBus.getDefault().post(AppConstants.EVENT_TIME_OUT_BACK);
            }
        }

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mDatas.size(); i++) {
                    if (i == position) {
                        if (mDatas.get(i).getIsSelected() == 0) {
                            mDatas.get(i).setIsSelected(1);
                            GroupVarManager.getInstance().isSelectedAnswer = 1;
                        } else {
                            mDatas.get(i).setIsSelected(0);
                            GroupVarManager.getInstance().isSelectedAnswer = 0;
                        }
                    } else {
                        mDatas.get(i).setIsSelected(0);
                    }
                }
                notifyDataSetChanged();
            }
        });
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
