package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.CourseListModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;
import java.util.List;
/**
 * 课程收藏列表适配器
 * Created by zhengwencheng on 2018/1/29 0029.
 * package com.jianzhong.bs.adapter
 */

public class CourseSearchAdapter extends CommonAdapter<CourseListModel> {
    public CourseSearchAdapter(Context context, List<CourseListModel> datas) {
        super(context, R.layout.item_course_collect, datas);

    }

    @Override
    protected void convert(ViewHolder holder, final CourseListModel item, int position) {


        holder.setText(R.id.tv_class_title, item.getTitle())
                .setText(R.id.tv_class_time, "时长：" + CommonUtils.secToTime(Integer.valueOf(item.getDuration_sec())))
                .setText(R.id.tv_class_count, "已有" + item.getPlay_num() + "人观看");
        if(item.getExpert() != null){
            holder.setText(R.id.tv_class_expert, item.getExpert().getRealname() + "/" + item.getExpert().getPos_duty());
        }
        GlideUtils.load((ImageView) holder.getView(R.id.iv_class), item.getCover());


        holder.setOnClickListener(R.id.ll_class, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ClassDetailActivity.class);
                intent.putExtra("course_id", item.getCourse_id());
                mContext.startActivity(intent);
            }
        });
    }

}
