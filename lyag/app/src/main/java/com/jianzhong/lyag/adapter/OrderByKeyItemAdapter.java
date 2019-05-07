package com.jianzhong.lyag.adapter;

import android.content.Context;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.OrderKeyModel;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 *
 * Created by zhengwencheng on 2017/2/14.
 * package com.jianzhong.ys.adapter
 */

public class OrderByKeyItemAdapter extends CommonAdapter<OrderKeyModel> {
    public OrderByKeyItemAdapter(Context context, List<OrderKeyModel> datas) {
        super(context, R.layout.item_common_select, datas);
    }

    @Override
    protected void convert(ViewHolder holder, OrderKeyModel item, int position) {
        if(item.getIsSelected() == 1){
            holder.setTextColor(R.id.tv_customer_type,mContext.getResources().getColor(R.color.color_theme));
        }else {
            holder.setTextColor(R.id.tv_customer_type,mContext.getResources().getColor(R.color.color_333333));
        }
        holder.setText(R.id.tv_customer_type, item.getStr());
    }
}
