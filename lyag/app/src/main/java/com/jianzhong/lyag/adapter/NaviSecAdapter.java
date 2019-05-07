package com.jianzhong.lyag.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baselib.widget.CustomGridView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.NavigationSecModel;
import com.jianzhong.lyag.ui.navi.NaviResultActivity;

import java.util.List;

/**
 * 导航栏的推荐对应的二级列表适配器
 *
 * Created by zhengwencheng on 2018/1/26 0026.
 * package com.jianzhong.bs.adapter
 */
public class NaviSecAdapter extends BaseExpandableListAdapter {
    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<NavigationSecModel> mList;

    public NaviSecAdapter(Activity mActivity, List<NavigationSecModel> mList) {
        this.mList = mList;
        this.mActivity = mActivity;
        this.mInflater = mActivity.getLayoutInflater().from(mActivity);
    }

    @Override
    public int getGroupCount() {
        return (mList == null) ? 0 : mList.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return (mList.get(groupPosition).getSub() == null) ? 0 : 1;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getSub().get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_navigationsec_group, null);
            groupHolder = new GroupHolder();
            groupHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            groupHolder.mIvMore = (ImageView) convertView.findViewById(R.id.iv_more);
            groupHolder.mLlItem = (LinearLayout) convertView.findViewById(R.id.ll_item);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.mTvTitle.setText(mList.get(groupPosition).getTitle());
        groupHolder.mIvMore.setVisibility(View.VISIBLE);
        groupHolder.mLlItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GroupVarManager.getInstance().path_2 = mList.get(groupPosition).getPath();
                Intent intent = new Intent(mActivity, NaviResultActivity.class);
                intent.putExtra("navi_id",mList.get(groupPosition).getNavi_id());
                intent.putExtra("search_key",mList.get(groupPosition).getSearch_key());
                intent.putExtra("search",mList.get(groupPosition).getSearch());
                intent.putExtra("title",mList.get(groupPosition).getTitle());
                intent.putExtra("is_more","0");
                mActivity.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_navigationsec_child, null);
            childHolder = new ChildHolder();
            childHolder.gridview = (CustomGridView) convertView.findViewById(R.id.gridview);
            convertView.setTag(childHolder);
        }else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        NaviChildAdapter mNaviChildAdapter = new NaviChildAdapter(mActivity,mList.get(groupPosition).getSub(),mList.get(groupPosition).getPath());
        childHolder.gridview.setAdapter(mNaviChildAdapter);
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        TextView mTvTitle;
        ImageView mIvMore;
        LinearLayout mLlItem;
    }

    class ChildHolder {
        CustomGridView gridview;
    }
}
