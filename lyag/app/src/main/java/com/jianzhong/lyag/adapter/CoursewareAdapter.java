package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.CoursewareModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 课件列表适配器
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.adapter
 */
public class CoursewareAdapter extends CommonAdapter<CoursewareModel> {
    public CoursewareAdapter(Context context,List<CoursewareModel> datas) {
        super(context, R.layout.item_course_ware, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CoursewareModel item, final int position) {
        GlideUtils.load((ImageView) holder.getView(R.id.iv_course),item.getWare_url());

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupVarManager.getInstance().isCheckWare = 1;
                CommonUtils.startImagePreviewActivity(mContext, mDatas, position);
            }
        });
    }
}
