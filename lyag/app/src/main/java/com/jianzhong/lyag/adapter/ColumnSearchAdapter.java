package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.ColumnContentModel;
import com.jianzhong.lyag.ui.exam.ColumnDetailActivity;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 专栏搜索列表适配器
 * Created by zhengwencheng on 2018/1/29 0029.
 * package com.jianzhong.bs.adapter
 */

public class ColumnSearchAdapter extends CommonAdapter<ColumnContentModel> {

    public ColumnSearchAdapter(Context context, List<ColumnContentModel> datas) {
        super(context, R.layout.item_column_collect, datas);

    }

    @Override
    protected void convert(ViewHolder holder, final ColumnContentModel item, int position) {


        GlideUtils.load((ImageView) holder.getView(R.id.iv_special), item.getCover());
        holder.setText(R.id.tv_special_title, item.getTitle())
                .setText(R.id.tv_special_explain, "")
                .setText(R.id.tv_special_update, "" + item.getNow_ep() + "/" + item.getTotal_ep())
                .setText(R.id.tv_special_update_time, item.getUpdate_day())
                .setText(R.id.tv_special_subscribe, "已有" + item.getPlay_num() + "人观看");

        if(item.getExpert() != null){
            holder.setText(R.id.tv_special_identify, item.getExpert().getRealname() + "/" + item.getExpert().getPos_duty());
        }

        holder.setOnClickListener(R.id.ll_special, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, ColumnDetailActivity.class);
                intent.putExtra("column_id", item.getColumn_id());
                mContext.startActivity(intent);

            }
        });
    }

}
