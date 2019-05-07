package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.NavigationSecSonModel;
import com.jianzhong.lyag.ui.navi.NaviResultActivity;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;
/**
 * 内容导航二级列表的子项是适配器
 * Created by zhengwencheng on 2018/1/26 0026.
 * package com.jianzhong.bs.adapter
 */

public class NaviChildAdapter extends CommonAdapter<NavigationSecSonModel> {
    private String path;
    public NaviChildAdapter(Context context, List<NavigationSecSonModel> datas,String path) {
        super(context, R.layout.item_navigation_sec_child, datas);
        this.path = path;
    }

    @Override
    protected void convert(ViewHolder viewHolder, final NavigationSecSonModel item, final int position) {
        viewHolder.setText(R.id.tv_value,item.getTitle());
        viewHolder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupVarManager.getInstance().path_3 = item.getPath();
                GroupVarManager.getInstance().path_2 = path;
                Intent intent = new Intent(mContext, NaviResultActivity.class);
                intent.putExtra("navi_id",item.getNavi_id());
                intent.putExtra("search_key",item.getSearch_key());
                intent.putExtra("search",item.getSearch());
                intent.putExtra("title",item.getTitle());
                intent.putExtra("is_more","0");
                mContext.startActivity(intent);
            }
        });
    }
}
