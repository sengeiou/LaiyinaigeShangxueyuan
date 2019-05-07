package com.jianzhong.lyag.ui.interact;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.RankAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.RankDetailModel;
import com.jianzhong.lyag.model.RankModel;
import com.jianzhong.lyag.model.RankScopeModel;
import com.jianzhong.lyag.util.GlideUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 达人榜fragment
 * create by zhengwencheng on 2018/3/21 0021
 * package com.jianzhong.bs.ui.interact
 */
public class RankFragment extends BaseRecyclerViewFragment {


    private List<RankModel> mList = new ArrayList<>();
    private RankAdapter mAdapter;
    //要添加的头
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private View headView;
    private ViewHolder mViewHolder;
    private int pageIndex = 1;
    private RankDetailModel data;
    private String chart;
    private String scope = "week";

    public static RankFragment newInstance(String chart) {

        Bundle args = new Bundle();

        RankFragment fragment = new RankFragment();
        args.putString("chart", chart);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        EventBus.getDefault().register(this);
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_rank, null);
        return mRootView;
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new RankAdapter(mContext, mList);
        return mAdapter;
    }

    @Override
    protected void initData() {
        super.initData();

        chart = getArguments().getString("chart");

        initRecyclerView();

        getInteractRank();
    }


    private void initRecyclerView() {
        /**设置RecyclerView的模式*/
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_grey_divider))
                .sizeResId(R.dimen.default_divider_one)
                .build());
        mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);

        /**上拉和下拉*/
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                pageIndex++;
                getInteractRank();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                pageIndex = 1;
                mList.clear();
                getInteractRank();
            }

        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateRankScope(RankScopeModel item) {
        if (item != null) {
            scope = item.getKey();
            pageIndex = 1;
            mList.clear();
            getInteractRank();
        }
    }

    private void getInteractRank() {
        Map<String, Object> params = new HashMap<>();
        params.put("chart", chart);
        params.put("scope", scope);
        params.put("p", pageIndex + "");

        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_INTERACT_RANK, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<RankDetailModel> result = GsonUtils.json2Bean(s, RankDetailModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    mPtrFrame.refreshComplete();
                    data = result.getData();
                    notifyData();
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage() : AppConstants.TRAN_ERROR_INFO);
                }
                isShowNoDataView();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
                showErrorView();
            }
        });
    }

    private void notifyData() {
        if (data == null)
            return;

        // 避免出现RecyclerView has no LayoutManager的错误
        mRecyclerView.setHasFixedSize(true);
        // 计算RecyclerView的大小，可以显示其内容
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        if (headView == null) {
            headView = mInflater.inflate(R.layout.content_rank, mRecyclerView, false);
            mViewHolder = new ViewHolder(headView);
            mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
            mHeaderAndFooterWrapper.addHeaderView(headView);
            mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        }

        if (data.getMy_rank() != null && data.getMy_rank().getInfo() != null) {
            mViewHolder.mLlItem.setVisibility(View.VISIBLE);
            mViewHolder.mTvMyRank.setVisibility(View.VISIBLE);
            mViewHolder.mTvRank.setText(data.getMy_rank().getRanking() + "");
            if (data.getMy_rank().getInfo().getUser() != null) {
                mViewHolder.mTvName.setText(data.getMy_rank().getInfo().getUser().getRealname());
                GlideUtils.loadAvatarImage(mViewHolder.mIvAvator, data.getMy_rank().getInfo().getUser().getAvatar());
            }
            mViewHolder.mTvCredit.setText(data.getMy_rank().getInfo().getRank_factor());
        } else {
            mViewHolder.mLlItem.setVisibility(View.GONE);
            mViewHolder.mTvMyRank.setVisibility(View.GONE);
        }

        if (!ListUtils.isEmpty(data.getList())) {
            mList.addAll(data.getList());
        }
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    class ViewHolder {
        @BindView(R.id.tv_rank)
        TextView mTvRank;
        @BindView(R.id.iv_avatar)
        ImageView mIvAvator;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_credit)
        TextView mTvCredit;
        @BindView(R.id.ll_item)
        LinearLayout mLlItem;
        @BindView(R.id.tv_my_rank)
        TextView mTvMyRank;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
