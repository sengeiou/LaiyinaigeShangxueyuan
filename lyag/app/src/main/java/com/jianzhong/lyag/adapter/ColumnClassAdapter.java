package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.ColumnCourseModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 专栏课程列表适配器
 * Created by zhengwencheng on 2018/2/26 0026.
 * package com.jianzhong.bs.adapter
 */
public class ColumnClassAdapter extends CommonAdapter<ColumnCourseModel> {
    public ColumnClassAdapter(Context context, List<ColumnCourseModel> datas) {
        super(context, R.layout.item_column_advance, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final ColumnCourseModel item, int position) {
        if (item.getExpert() != null) {
            holder.setText(R.id.tv_expert, item.getExpert().getRealname() + " | " + item.getExpert().getPos_duty());
        }
        GlideUtils.load((ImageView) holder.getView(R.id.iv_advance),item.getCover());
        holder.setText(R.id.tv_title,item.getTitle());
        holder.setText(R.id.tv_time,"总时长："+ CommonUtils.secToTime(Integer.valueOf(item.getDuration_sec())));
        holder.setText(R.id.tv_study,"已有"+item.getPlay_num()+"人学习");
        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassDetailActivity.class);
                intent.putExtra("course_id",item.getCourse_id());
                mContext.startActivity(intent);
            }
        });
    }
}
