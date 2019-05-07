package com.jianzhong.lyag.adapter;

import android.app.Activity;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.NavigationSecModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;

import java.util.List;
/**
 * 导航栏的推荐对应的二级列表适配器 因为推荐度又唔同其他 服未
 * Created by zhengwencheng on 2018/1/26 0026.
 * package com.jianzhong.bs.adapter
 */
public class NaviRecommendAdapter extends BaseExpandableListAdapter {
    private Activity mActivity;
    private LayoutInflater mInflater;
    private List<NavigationSecModel> mList;

    public NaviRecommendAdapter(Activity mActivity, List<NavigationSecModel> mList) {
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
        return (mList.get(groupPosition).getSub() == null) ? 0 : mList.get(groupPosition).getSub().size();
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
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_navigationsec_group, null);
            groupHolder = new GroupHolder();
            groupHolder.mTvTitle = (TextView) convertView.findViewById(R.id.tv_title);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.mTvTitle.setText(mList.get(groupPosition).getTitle());
        return convertView;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ChildHolder childHolder;
        if(convertView == null){
            convertView = mInflater.inflate(R.layout.item_navigationsec_recommend, null);
            childHolder = new ChildHolder();
            childHolder.mTvSubTitle = (TextView) convertView.findViewById(R.id.tv_sub_title);
            convertView.setTag(childHolder);
        }else {
            childHolder = (ChildHolder) convertView.getTag();
        }
        childHolder.mTvSubTitle.setText(mList.get(groupPosition).getSub().get(childPosition).getTitle());
        childHolder.mTvSubTitle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mActivity, ClassDetailActivity.class);
                intent.putExtra("course_id",mList.get(groupPosition).getSub().get(childPosition).getNavi_id());
                mActivity.startActivity(intent);
            }
        });
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    class GroupHolder {
        TextView mTvTitle;
    }

    class ChildHolder {
        TextView mTvSubTitle;
    }
}
