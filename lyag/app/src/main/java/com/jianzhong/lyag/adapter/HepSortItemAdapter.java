package com.jianzhong.lyag.adapter;

import android.content.Context;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.HelpSortFieldModel;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 *
 * Created by zhengwencheng on 2017/2/14.
 * package com.jianzhong.ys.adapter
 */

public class HepSortItemAdapter extends CommonAdapter<HelpSortFieldModel> {
    public HepSortItemAdapter(Context context, List<HelpSortFieldModel> datas) {
        super(context, R.layout.item_common_select, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final HelpSortFieldModel item, final int position) {
        holder.setText(R.id.tv_customer_type, item.getStr());
        if(item.getIsSelected() == 1){
            holder.setTextColor(R.id.tv_customer_type,mContext.getResources().getColor(R.color.color_theme));
        }else {
            holder.setTextColor(R.id.tv_customer_type,mContext.getResources().getColor(R.color.color_333333));
        }

    }
}
