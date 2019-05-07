package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.view.View;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.listener.OnItemsSelectInterface;
import com.jianzhong.lyag.model.NavigationSecSonModel;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;

/**
 * 内容导航结果分类的第三栏适配器列表
 * Created by zhengwencheng on 2018/1/26 0026.
 * package com.jianzhong.bs.adapter
 */

public class NaviClassifyThirdAdapter extends CommonAdapter<NavigationSecSonModel> {
    private OnItemsSelectInterface onItemsSelectInterface;
    public NaviClassifyThirdAdapter(Context context, List<NavigationSecSonModel> datas, OnItemsSelectInterface onItemsSelectInterface) {
        super(context, R.layout.item_navi_classify_third, datas);
        this.onItemsSelectInterface = onItemsSelectInterface;
    }

    @Override
    protected void convert(ViewHolder holder, NavigationSecSonModel item, final int position) {
        holder.setText(R.id.tv_value, item.getTitle());
        if (item.getIsSelected() == 1) {
            (holder.getView(R.id.ll_item)).setSelected(true);
        } else {
            (holder.getView(R.id.ll_item)).setSelected(false);
        }

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < mDatas.size(); i++) {
                    if (i == position) {
                        mDatas.get(i).setIsSelected(1);
                    } else {
                        mDatas.get(i).setIsSelected(0);
                    }
                }
                notifyDataSetChanged();
                onItemsSelectInterface.OnClick(position);
            }
        });
    }
}
