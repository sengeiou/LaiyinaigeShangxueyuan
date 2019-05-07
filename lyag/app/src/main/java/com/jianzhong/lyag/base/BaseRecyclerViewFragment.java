package com.jianzhong.lyag.base;

import android.support.annotation.Nullable;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.jianzhong.lyag.R;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by chenzhikai
 * Email 1310741158@qq.com
 */
public abstract class BaseRecyclerViewFragment extends BaseFragment{

    @Nullable
    @BindView(R.id.ptr_frame)
    protected PtrClassicFrameLayout mPtrFrame;
    @Nullable
    @BindView(R.id.recycler_view)
    protected RecyclerView mRecyclerView;
    protected RecyclerView.Adapter mAdapter;

    protected abstract RecyclerView.Adapter getRecyclerViewAdapter();

    // 设置自定义布局接口
    private IContentView mIContentView;

    public void setContentView(IContentView mIContentView){
        this.mIContentView = mIContentView;
    }

    public interface IContentView {
        int getContentView();
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        // 自定义布局
        if(mIContentView != null){
            mRootView = inflater.inflate(mIContentView.getContentView(), null);
        }else{
            mRootView = inflater.inflate(R.layout.fragment_base_recycler_view, null);
        }
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();
        if(isNull()) return;

        mAdapter = getRecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());
        mRecyclerView.setLayoutManager(new LinearLayoutManager(mActivity));
//        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
//                .color(getResources().getColor(R.color.color_dark_white))
//                .sizeResId(R.dimen.default_divider)
//                .build());

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
