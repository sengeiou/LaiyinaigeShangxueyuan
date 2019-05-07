package com.jianzhong.lyag.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.PointDetailModel;
import com.jianzhong.lyag.model.PointExpandModel;
import com.jianzhong.lyag.util.CommonUtils;

import java.util.List;

/**
 * Created by Administrator on 2017/3/21.
 */

public class PointDetailAdapter extends BaseExpandableListAdapter {

    private Activity mActivity;
    private List<PointExpandModel> mList;
    private LayoutInflater mInflater;

    public PointDetailAdapter(Activity mActivity, List<PointExpandModel> mList) {
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
        return (mList.get(groupPosition).getPoint_detail() == null) ? 0 : mList.get(groupPosition).getPoint_detail().size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return mList.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return mList.get(groupPosition).getPoint_detail().get(childPosition);
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
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        GroupHolder groupHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_point_group, null);
            groupHolder = new GroupHolder();
            groupHolder.mTvDate = convertView.findViewById(R.id.tv_date);
            convertView.setTag(groupHolder);
        } else {
            groupHolder = (GroupHolder) convertView.getTag();
        }
        groupHolder.mTvDate.setText(mList.get(groupPosition).getTag());
        return convertView;
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildHolder childHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.item_point_child, null);
            childHolder = new ChildHolder();
            childHolder.mTvTitle = convertView.findViewById(R.id.tv_title);
            childHolder.mTvTime = convertView.findViewById(R.id.tv_time);
            childHolder.mTvPoint = convertView.findViewById(R.id.tv_point);
            convertView.setTag(childHolder);
        } else {
            childHolder = (ChildHolder) convertView.getTag();
        }

        final PointDetailModel item = mList.get(groupPosition).getPoint_detail().get(childPosition);

        childHolder.mTvTitle.setText(item.getRule() != null ? item.getRule().getRemark() : "");
        childHolder.mTvTime.setText(CommonUtils.getDryTime(item.getCreate_at()));
        childHolder.mTvPoint.setText("+" + item.getPoint());
        return convertView;
    }

    class GroupHolder {
        TextView mTvDate;
    }

    class ChildHolder {
        TextView mTvTitle, mTvTime, mTvPoint;
    }

}



