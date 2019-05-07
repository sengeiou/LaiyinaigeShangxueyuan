package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.CourseListModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;
/**
 * 精品课程列表适配器
 * Created by zhengwencheng on 2018/1/17 0017.
 * package com.jianzhong.bs.adapter
 */

public class GoodClassAdapter extends CommonAdapter<CourseListModel> {
    public GoodClassAdapter(Context context,List<CourseListModel> datas) {
        super(context, R.layout.item_good_class, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final CourseListModel item, int position) {
        GlideUtils.load((ImageView) holder.getView(R.id.iv_desc),item.getCover_home());
        if(item.getExpert() != null){
            holder.setText(R.id.tv_identity,item.getExpert().getRealname()+"/"+item.getExpert().getPos_duty());
        }
        holder.setText(R.id.tv_title,item.getTitle());
        holder.setText(R.id.tv_study,"已有"+item.getPlay_num()+"人观看");
        holder.setText(R.id.tv_price,"价钱");

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
