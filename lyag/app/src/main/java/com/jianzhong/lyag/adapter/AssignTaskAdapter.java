package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.AssignTaskModel;
import com.jianzhong.lyag.ui.exam.MemberConActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 学习任务列表适配器指派
 * create by zhengwencheng on 2018/3/21 0021
 * package com.jianzhong.bs.adapter
 */
public class AssignTaskAdapter extends CommonAdapter<AssignTaskModel> {
    private int isFinished;

    public AssignTaskAdapter(Context context, List<AssignTaskModel> datas, int isFinished) {
        super(context, R.layout.item_assign_task, datas);

        this.isFinished = isFinished;
    }

    @Override
    protected void convert(ViewHolder holder, final AssignTaskModel item, int position) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_require, "任务要求：" + item.getDictate_title());
        holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getCreate_at()));
        holder.setText(R.id.tv_count, item.getUn_finish_num());
        if (isFinished == 0) {
            holder.setVisible(R.id.ll_count, true);
            holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, MemberConActivity.class);
                    intent.putExtra("title","学习任务未完成用户");
                    intent.putExtra("dictate_id",item.getDictate_id());
                    mContext.startActivity(intent);
                }
            });
        } else {
            holder.setVisible(R.id.tv_all_done_study, true);
            holder.setVisible(R.id.ll_count, false);
        }
    }

    public void setIsFinished(int isFinished) {
        this.isFinished = isFinished;
    }
}
