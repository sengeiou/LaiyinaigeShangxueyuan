package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.baselib.util.StringUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.model.StudyTaskModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;
import com.jianzhong.lyag.ui.exam.ColumnDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 学习任务列表适配器
 * Created by zhengwencheng on 2018/2/1 0001.
 * package com.jianzhong.bs.adapter
 */
public class StudyTaskAdapter extends CommonAdapter<StudyTaskModel> {


    public StudyTaskAdapter(Context context, List<StudyTaskModel> datas) {
        super(context, R.layout.item_study_task, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final StudyTaskModel item, int position) {
        GlideUtils.load((ImageView) holder.getView(R.id.iv_refer), item.getCover());
        holder.setText(R.id.tv_title, item.getTitle());
        if (item.getAsset_type().equals(AppConstants.TAG_COLUMN)) { //专栏
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.xxrw_zl);
            // 这一步必须要做,否则不会显示.
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView) holder.getView(R.id.tv_title)).setCompoundDrawables(drawable, null, null, null);

            if (item.getFinish_ep().equals(item.getTotal_ep())) {
                holder.setText(R.id.tv_progress, "共" + item.getTotal_ep() + "课时");
            } else {
                holder.setText(R.id.tv_progress, "已看" + item.getFinish_ep() + "课时/共" + item.getTotal_ep() + "课时");
                ((TextRoundCornerProgressBar) holder.getView(R.id.progress_bar)).setMax(Float.valueOf(item.getTotal_ep()));
                ((TextRoundCornerProgressBar) holder.getView(R.id.progress_bar)).setProgress(Float.valueOf(item.getFinish_ep()));
            }
        } else if (item.getAsset_type().equals(AppConstants.TAG_COURSE)) { //课程
            ((TextView) holder.getView(R.id.tv_title)).setCompoundDrawables(null, null, null, null);
            if (item.getDuration_sec().equals(item.getTotal_study_sec())) {
                ((TextRoundCornerProgressBar) holder.getView(R.id.progress_bar)).setProgress(100f);
                holder.setText(R.id.tv_progress, "共" + CommonUtils.secToTime(Integer.valueOf(item.getDuration_sec())));
            } else {
                holder.setText(R.id.tv_progress, "已看" + CommonUtils.secToTime((Integer.valueOf(item.getTotal_study_sec()))) + "/共" + CommonUtils.secToTime(Integer.valueOf(item.getDuration_sec())));
                ((TextRoundCornerProgressBar) holder.getView(R.id.progress_bar)).setMax(Float.valueOf(item.getDuration_sec()));
                ((TextRoundCornerProgressBar) holder.getView(R.id.progress_bar)).setProgress(Float.valueOf(item.getTotal_study_sec()));
            }
        }

        if (item.getExpert() != null) {
            if (!StringUtils.isEmpty(item.getExpert().getRealname()) && !StringUtils.isEmpty(item.getExpert().getPos_duty())) {
                holder.setText(R.id.tv_expert, item.getExpert().getRealname() + " | " + item.getExpert().getPos_duty());
            } else if (!StringUtils.isEmpty(item.getExpert().getRealname())) {
                holder.setText(R.id.tv_expert, item.getExpert().getRealname());
            } else if (!StringUtils.isEmpty(item.getExpert().getPos_duty())) {
                holder.setText(R.id.tv_expert, item.getExpert().getPos_duty());
            }
        } else {
            holder.setText(R.id.tv_expert, "");
        }

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (item.getAsset_type().equals(AppConstants.TAG_COLUMN)) { //专栏
                    intent = new Intent(mContext, ColumnDetailActivity.class);
                    intent.putExtra("column_id", item.getForeign_id());
                    mContext.startActivity(intent);
                } else if (item.getAsset_type().equals(AppConstants.TAG_COURSE)) { //课程
                    intent = new Intent(mContext, ClassDetailActivity.class);
                    intent.putExtra("course_id", item.getForeign_id());
                    mContext.startActivity(intent);
                }
            }
        });

    }

}
