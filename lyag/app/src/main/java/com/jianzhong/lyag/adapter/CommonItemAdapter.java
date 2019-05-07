package com.jianzhong.lyag.adapter;

import android.content.Context;
import com.jianzhong.lyag.R;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 *
 * Created by zhengwencheng on 2017/2/14.
 * package com.jianzhong.ys.adapter
 */

public class CommonItemAdapter extends CommonAdapter<String> {
    public CommonItemAdapter(Context context, List<String> datas) {
        super(context, R.layout.item_common_select, datas);
    }

    @Override
    protected void convert(ViewHolder holder, String item, int position) {
        holder.setText(R.id.tv_customer_type, item);
    }
}
