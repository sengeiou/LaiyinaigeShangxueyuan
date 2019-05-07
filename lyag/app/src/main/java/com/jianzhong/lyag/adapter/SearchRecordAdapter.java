package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.listener.OnSearchRecListener;
import com.jianzhong.lyag.model.SearchRecordModel;
import com.jianzhong.lyag.ui.navi.SearchResultActivity;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;
/**
 * Created by zhengwencheng on 2018/1/27.
 * E-mail 15889964542@163.com
 * 搜索记录适配器
 */

public class SearchRecordAdapter extends CommonAdapter<SearchRecordModel> {
    private OnSearchRecListener listener;
    public SearchRecordAdapter(Context context, List<SearchRecordModel> datas,OnSearchRecListener listener) {
        super(context, R.layout.item_search_record, datas);

        this.listener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, final SearchRecordModel item, final int position) {
        holder.setText(R.id.tv_record,item.getSearch_record());
        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, SearchResultActivity.class);
                intent.putExtra("keyWord", item.getSearch_record());
                mContext.startActivity(intent);
            }
        });

        holder.setOnClickListener(R.id.ll_delete, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.callBack(position,item);
            }
        });
    }
}
