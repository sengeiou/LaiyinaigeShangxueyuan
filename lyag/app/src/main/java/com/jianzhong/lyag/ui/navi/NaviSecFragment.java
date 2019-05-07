package com.jianzhong.lyag.ui.navi;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ImageView;

import com.baselib.util.ListUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.NaviSecAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.model.NavigationSecModel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 内容导航
 * Created by zhengwencheng on 2018/1/26 0026.
 * package com.jianzhong.bs.ui.navigation
 */

public class NaviSecFragment extends BaseFragment {

    @BindView(R.id.expandable_listview)
    ExpandableListView mExpandableListview;

    private View headView;
    private ViewHolder mViewHolder;
    private NaviSecAdapter mNaviSecAdapter;
    private List<NavigationSecModel> mList = new ArrayList<>();

    public static NaviSecFragment getInstance(List<NavigationSecModel> mNavigationSecModels) {
        NaviSecFragment fragment = new NaviSecFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mNavigationSecModels", (Serializable) mNavigationSecModels);

        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_navigation_sec, null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();

//        headView = LayoutInflater.from(mContext).inflate(R.layout.content_navigation_headview, null);
//        mViewHolder = new ViewHolder(headView);
//        mExpandableListview.addHeaderView(headView);

        List<NavigationSecModel> mNavigationSecModels = (List<NavigationSecModel>) getArguments().getSerializable("mNavigationSecModels");
        if (!ListUtils.isEmpty(mNavigationSecModels)) {
            mList.addAll(mNavigationSecModels);

            mNaviSecAdapter = new NaviSecAdapter(getActivity(), mList);
            mExpandableListview.setAdapter(mNaviSecAdapter);
            mExpandableListview.setGroupIndicator(null);
            mExpandableListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return true;
                }
            });

            notifyDataSetChanged();
        }
    }

    /**
     * 刷新Adapter
     *
     * @param
     */
    private void notifyDataSetChanged() {
        if (!ListUtils.isEmpty(mList)) {
            // 设置ExpandableListView的子项默认展开
            for (int i = 0; i < mList.size(); i++) {
                mExpandableListview.expandGroup(i);
            }
            mNaviSecAdapter.notifyDataSetChanged();
        }
    }

    class ViewHolder {
        @BindView(R.id.iv_banner)
        ImageView mIvBanner;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
