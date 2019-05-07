package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.TaskReferModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 通关任务[子任务]参考资料
 * Created by zhengwencheng on 2018/2/1 0001.
 * package com.jianzhong.bs.adapter
 */
public class CourseHistoryAdapter extends CommonAdapter<TaskReferModel> {

    public CourseHistoryAdapter(Context context, List<TaskReferModel> datas) {
        super(context, R.layout.item_course_history, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final TaskReferModel item, int position) {
        GlideUtils.load((ImageView) holder.getView(R.id.iv_refer), item.getCover());
        holder.setText(R.id.tv_time, "观看时间：" + CommonUtils.getDryTime(item.getCreate_at() + ""));
        holder.setText(R.id.tv_title, item.getTitle());

        if (item.getDuration_sec().equals(item.getTotal_study_sec())) {
            holder.setText(R.id.tv_progress, "共" + CommonUtils.secToTime(Integer.valueOf(item.getDuration_sec())));
            ((TextRoundCornerProgressBar) holder.getView(R.id.progress_bar)).setProgress(100f);
        } else {
            holder.setText(R.id.tv_progress, "已看" + CommonUtils.secToTime(Integer.valueOf(item.getTotal_study_sec())) + "/共" + CommonUtils.secToTime(Integer.valueOf(item.getDuration_sec())));
            ((TextRoundCornerProgressBar) holder.getView(R.id.progress_bar)).setMax(Float.valueOf(item.getDuration_sec()));
            ((TextRoundCornerProgressBar) holder.getView(R.id.progress_bar)).setProgress(Float.valueOf(item.getTotal_study_sec()));
        }

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassDetailActivity.class);
                intent.putExtra("course_id", item.getCourse_id());
                mContext.startActivity(intent);
            }
        });

    }
}
