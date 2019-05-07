package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.TaskSubModel;
import com.jianzhong.lyag.ui.exam.TaskReferActiVity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 学习任务子列表的适配器
 * Created by zhengwencheng on 2018/2/1 0001.
 * package com.jianzhong.bs.adapter
 */

public class TaskSubAdapter extends CommonAdapter<TaskSubModel> {
    public TaskSubAdapter(Context context,List<TaskSubModel> datas) {
        super(context, R.layout.item_task_sub, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final TaskSubModel item, int position) {
        holder.setText(R.id.tv_title,item.getTitle());
//        holder.setText(R.id.tv_pass_str,item.getPass_str());

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, TaskReferActiVity.class);
                intent.putExtra("title",item.getTitle());
                intent.putExtra("task_id",item.getTask_id());
                intent.putExtra("top_task_id",item.getTop_task_id());
                mContext.startActivity(intent);
            }
        });
    }
}
