package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.widget.ImageView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.ColumnCourseModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;
/**
 * 专栏预告列表适配器
 * Created by zhengwencheng on 2018/2/26 0026.
 * package com.jianzhong.bs.adapter
 */
public class ColumnAdvanceAdapter extends CommonAdapter<ColumnCourseModel> {
    public ColumnAdvanceAdapter(Context context, List<ColumnCourseModel> datas) {
        super(context, R.layout.item_column_advance, datas);
    }

    @Override
    protected void convert(ViewHolder holder, ColumnCourseModel item, int position) {
        GlideUtils.load((ImageView) holder.getView(R.id.iv_advance),item.getCover());
        holder.setText(R.id.tv_title,item.getTitle());
        holder.setText(R.id.tv_time,"上线时间："+ CommonUtils.getDryTime(item.getPublish_at()));
        holder.setText(R.id.tv_study,"已有"+item.getPlay_num()+"人学习");
    }
}
