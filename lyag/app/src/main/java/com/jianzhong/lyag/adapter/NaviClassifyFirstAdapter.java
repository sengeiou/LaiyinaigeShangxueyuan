package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.view.View;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.listener.OnItemsSelectInterface;
import com.jianzhong.lyag.model.NavigationFirstModel;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;
/**
 * 内容导航结果分类的第一栏适配器列表
 * Created by zhengwencheng on 2018/1/26 0026.
 * package com.jianzhong.bs.adapter
 */

public class NaviClassifyFirstAdapter extends CommonAdapter<NavigationFirstModel> {
    private OnItemsSelectInterface onItemsSelectInterface;
    public NaviClassifyFirstAdapter(Context context, List<NavigationFirstModel> datas, OnItemsSelectInterface onItemsSelectInterface) {
        super(context, R.layout.item_navi_classify_first, datas);
        this.onItemsSelectInterface = onItemsSelectInterface;
    }

    @Override
    protected void convert(ViewHolder holder, NavigationFirstModel item, final int position) {
        holder.setText(R.id.tv_value, item.getTitle());
        if (item.getIsSelected() == 1) {
            (holder.getView(R.id.ll_item)).setBackgroundColor(mContext.getResources().getColor(R.color.color_e8e8e9));
        } else {
            (holder.getView(R.id.ll_item)).setBackgroundColor(mContext.getResources().getColor(R.color.color_f2f2f3));
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
