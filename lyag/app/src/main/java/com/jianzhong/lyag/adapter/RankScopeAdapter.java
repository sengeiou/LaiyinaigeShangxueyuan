package com.jianzhong.lyag.adapter;

import android.content.Context;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.RankScopeModel;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;
/**
 * 新建客户类型选项选择
 * Created by zhengwencheng on 2017/2/14.
 * package com.jianzhong.ys.adapter
 */

public class RankScopeAdapter extends CommonAdapter<RankScopeModel> {
    public RankScopeAdapter(Context context, List<RankScopeModel> datas) {
        super(context, R.layout.item_rank_scope, datas);
    }

    @Override
    protected void convert(ViewHolder holder, RankScopeModel item, int position) {
        holder.setText(R.id.tv_customer_type, item.getName());
        /*if(item.isSelected()){
            holder.setTextColor(R.id.tv_customer_type,mContext.getResources().getColor(R.color.color_theme));
        }else {
            holder.setTextColor(R.id.tv_customer_type,mContext.getResources().getColor(R.color.color_333333));
        }*/
    }
}
