package com.jianzhong.lyag.ui.navi;

import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.baselib.util.ListUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.NaviFirstAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.listener.OnItemsSelectInterface;
import com.jianzhong.lyag.model.NavigationFirstModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 内容导航
 * Created by zhengwencheng on 2018/1/26 0026.
 * package com.jianzhong.bs.ui.navigation
 */

public class NaviFirstFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private NaviFirstAdapter mNaviFirstAdapter;
    private List<NavigationFirstModel> mList = new ArrayList<>();

    public static NaviFirstFragment getInstance(List<NavigationFirstModel> mNavigationFirstModels) {
        NaviFirstFragment fragment = new NaviFirstFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mNavigationFirstModels", (Serializable) mNavigationFirstModels);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_navigation_first, null);
        return mRootView;
    }


    @Override
    protected void initData() {
        super.initData();

        initRecylerView();

        List<NavigationFirstModel> mNavigationFirstModels = (List<NavigationFirstModel>) getArguments().getSerializable("mNavigationFirstModels");
        if (!ListUtils.isEmpty(mNavigationFirstModels)) {
            mList.addAll(mNavigationFirstModels);
        }
        mNaviFirstAdapter = new NaviFirstAdapter(mContext, mList, new OnItemsSelectInterface() {
            @Override
            public void OnClick(int which) {

            }
        });
        mRecyclerView.setAdapter(mNaviFirstAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));
    }

    /**
     * 初始化列表
     */
    private void initRecylerView() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_grey_divider))
                .sizeResId(R.dimen.default_divider_one)
                .build());

        mRecyclerView.setHasFixedSize(true);
    }

}
