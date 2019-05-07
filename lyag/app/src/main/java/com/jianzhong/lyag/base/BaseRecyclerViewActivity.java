package com.jianzhong.lyag.base;

import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.jianzhong.lyag.R;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by zhengwencheng on 2017/2/7.
 * package com.jianzhong.ys.base
 */

public abstract class BaseRecyclerViewActivity extends ToolbarActivity {

    @Nullable
    @BindView(R.id.ptr_frame)
    protected PtrClassicFrameLayout mPtrFrame;
    @Nullable
    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;

    protected abstract RecyclerView.Adapter getRecyclerViewAdapter();

    @Override
    public void initData() {
        super.initData();
        if(isNull()) return;

        mAdapter = getRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));

        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setMode(PtrFrameLayout.Mode.NONE);
        mPtrFrame.autoLoadMore(false);
    }

    public boolean isNull(){
        if (mPtrFrame == null) return true;
        if (mRecyclerView == null) return true;
        return false;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public RecyclerView.Adapter getAdapter() {
        return mAdapter;
    }

    public PtrClassicFrameLayout getPtrClassicFrameLayout() {
        return mPtrFrame;
    }

    /**
     * 是否显示无数据页面
     */
    public void isShowNoDataView() {
        if (mAdapter == null || mAdapter.getItemCount() <= 0) {
            showNoData();
            hideError();
            hideCommonLoading();
        } else {
            hideNoData();
        }
        hideError();
    }

}
