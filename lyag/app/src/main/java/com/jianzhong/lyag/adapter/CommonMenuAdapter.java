package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.view.View;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.CommonMenuModel;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;
/**
 * 新建客户类型选项选择
 * Created by zhengwencheng on 2017/2/14.
 * package com.jianzhong.ys.adapter
 */

public class CommonMenuAdapter extends CommonAdapter<CommonMenuModel> {

    public CommonMenuAdapter(Context context, List<CommonMenuModel> datas) {
        super(context, R.layout.item_common_menu, datas);
    }

    @Override
    protected void convert(ViewHolder holder, CommonMenuModel item, int position) {
        holder.setText(R.id.tv_content, item.getTitle());
        holder.setImageResource(R.id.iv_icon, item.getIcon());

        if (item.getIcon() == 0) {
            holder.setVisible(R.id.iv_icon, false);
        } else {
            holder.setVisible(R.id.iv_icon, true);
        }

        if (mDatas.size() == position + 1) {
            holder.getView(R.id.view_divider).setVisibility(View.INVISIBLE);
        } else {
            holder.getView(R.id.view_divider).setVisibility(View.VISIBLE);
        }

    }

}
