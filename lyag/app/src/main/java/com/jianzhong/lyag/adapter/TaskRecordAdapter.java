package com.jianzhong.lyag.adapter;

import android.content.Context;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.TaskRecordModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import java.util.List;

/**
 * 成长通关记录 列表的UI
 * Created by zhengwencheng on 2018/3/29 0029.
 * package com.jianzhong.bs.adapter
 */
public class TaskRecordAdapter extends CommonAdapter<TaskRecordModel> {
    public TaskRecordAdapter(Context context, List<TaskRecordModel> datas) {
        super(context, R.layout.item_task_record, datas);
    }

    @Override
    protected void convert(ViewHolder holder, TaskRecordModel item, int position) {
        if(item.getTask() != null){
            holder.setText(R.id.tv_title,item.getTask().getTitle());
        }

        holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getCreate_at()));
        if(item.getIs_pass().equals("1")){
            holder.setText(R.id.tv_status,"成功");
            holder.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.color_333333));
        }else {
            holder.setText(R.id.tv_status,"失败");
            holder.setTextColor(R.id.tv_status,mContext.getResources().getColor(R.color.color_theme));
        }
    }
}
