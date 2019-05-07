package com.jianzhong.lyag.ui.exam;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.ToastUtils;
import com.baselib.widget.CustomListView;
import com.baselib.widget.xlistview.XScrollView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.ColumnAdvanceAdapter;
import com.jianzhong.lyag.adapter.ColumnClassAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.ColumnCourseModel;
import com.jianzhong.lyag.model.ColumnDetailModel;
import com.jianzhong.lyag.ui.organization.AssignStudyActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 专栏详情
 * Created by zhengwencheng on 2018/2/25 0025.
 * package com.jianzhong.bs.ui.exam
 */

public class ColumnDetailActivity extends ToolbarActivity {

    private static final int SHRINK_UP_STATE = 1; // 收起状态
    private static final int SPREAD_STATE = 2; // 展开状态
    private static int mState = SPREAD_STATE;//默认收起状态
    private static final int VIDEO_CONTENT_DESC_MAX_LINE = 2;// 默认展示最大行数2行
    @BindView(R.id.scroll_view)
    XScrollView mScrollView;
    @BindView(R.id.ll_collect)
    LinearLayout mLlCollect;
    @BindView(R.id.ll_asign)
    LinearLayout mLlAsign;
    private View mView; //要添加的头view
    private ViewHolder mViewHolder;
    //专栏预告
    private ColumnAdvanceAdapter mAdvanceAdapter;
    private List<ColumnCourseModel> mAdvanceList = new ArrayList<>();
    //专栏课程列表适配器
    private ColumnClassAdapter mClassAdapter;
    private List<ColumnCourseModel> mClassList = new ArrayList<>();
    //专栏id
    private String column_id;
    private ColumnDetailModel data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_column_detail);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        column_id = getIntent().getStringExtra("column_id");

        setHeadTitle("专栏详情");
        showHeadTitle();

        getColumnDetail();
    }

    /**
     * 获取专栏详情
     */
    private void getColumnDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("column_id", column_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COLUMN_DETAIL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<ColumnDetailModel> result = GsonUtils.json2Bean(s, ColumnDetailModel.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    data = result.getData();
                    initController();
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage() : "数据解析错误");
                }
                isShowNoDataView(data);
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
                showErrorView();
            }
        });
    }

    /**
     * 初始化View
     */
    private void initController() {
        if (data == null)
            return;
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {
            @Override
            public void onRefresh() {
                mScrollView.stopRefresh();
            }

            @Override
            public void onLoadMore() {
                mScrollView.stopRefresh();
            }
        });
        mScrollView.setRefreshTime(CommonUtils.getTime());
        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.content_column_detail, null);
            mViewHolder = new ViewHolder(mView);
            mScrollView.setView(mView);
        }

        //判断是是否有指派权限
        if (AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion() != null &&
                !ListUtils.isEmpty(AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion().getOperation())) {
            for (int i = 0; i < AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion().getOperation().size(); i++) {
                if (AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion().getOperation().get(i).equals(AppConstants.TAG_DICTATE_SEND)) {
                    mLlAsign.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }

        CommonUtils.getImageLayoutParams(mContext, mViewHolder.mIvColumn, 750, 300, 1, 0);
        GlideUtils.load(mViewHolder.mIvColumn, data.getCover_in());
        mViewHolder.mTvTitle.setText(data.getTitle());
        mViewHolder.mTvExplain.setText(data.getBrief());
        mViewHolder.mTvSummary.setText(data.getSummary());
        mViewHolder.mTvCount.setText(data.getTotal_ep() + "集");
        mViewHolder.mTvStudy.setText(data.getPlay_num() + "人已观看");
        //设置收藏
        if (data.getHas_favor().equals("0")) {
            mLlCollect.setSelected(false);
        } else {
            mLlCollect.setSelected(true);
        }
        //专栏预告
        mAdvanceList.clear();
        mClassList.clear();
        for (int i = 0; i < data.getCourse().size(); i++) {
            if (data.getCourse().get(i).getIs_publish().equals("0")) {
                mAdvanceList.add(data.getCourse().get(i));
            } else {
                mClassList.add(data.getCourse().get(i));
            }
        }
        if (!ListUtils.isEmpty(mAdvanceList)) {
            mViewHolder.mLlAdvance.setVisibility(View.VISIBLE);
            if (mClassAdapter == null) {
                mAdvanceAdapter = new ColumnAdvanceAdapter(mContext, mAdvanceList);

                mViewHolder.mLvAdvance.setAdapter(mAdvanceAdapter);
            } else {
                mAdvanceAdapter.notifyDataSetChanged();
            }
        } else {
            mViewHolder.mLlAdvance.setVisibility(View.GONE);
        }

        //课程
        if (!ListUtils.isEmpty(mClassList)) {
            mViewHolder.mLlClass.setVisibility(View.VISIBLE);
            if (mClassAdapter == null) {
                mClassAdapter = new ColumnClassAdapter(mContext, mClassList);
                mViewHolder.mLvClass.setAdapter(mClassAdapter);
            } else {
                mClassAdapter.notifyDataSetChanged();
            }
        } else {
            mViewHolder.mLlClass.setVisibility(View.GONE);
        }
    }

    @OnClick({R.id.head_iv_right, R.id.ll_collect, R.id.ll_asign})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.head_iv_right:

                break;
            case R.id.ll_collect:
                if (data.getHas_favor().equals("0")) {
                    addCollect();
                } else {
                    delCollect();
                }
                break;
            case R.id.ll_asign:
                intent = new Intent(mContext, AssignStudyActivity.class);
                intent.putExtra("foreign_id", data.getColumn_id());
                intent.putExtra("asset_type", data.getAsset_type());
                startActivity(intent);
                break;
        }
    }

    /**
     * 添加收藏
     */
    private void addCollect() {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", data.getAsset_type());
        params.put("foreign_id", data.getColumn_id());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_ADD_FAVOR, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<String> result = GsonUtils.json2Bean(s, String.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    data.setHas_favor("1");
                    mLlCollect.setSelected(true);
                }
                ToastUtils.show(mContext, result.getMessage());
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 删除收藏
     */
    private void delCollect() {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", data.getAsset_type());
        params.put("foreign_id", data.getColumn_id());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DEL_FAVOR, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<String> result = GsonUtils.json2Bean(s, String.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    data.setHas_favor("0");
                    mLlCollect.setSelected(false);
                }
                ToastUtils.show(mContext, result.getMessage());
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    class ViewHolder {
        @BindView(R.id.iv_column)
        ImageView mIvColumn;
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.tv_explain)
        TextView mTvExplain;
        @BindView(R.id.tv_count)
        TextView mTvCount;
        @BindView(R.id.tv_study)
        TextView mTvStudy;
        @BindView(R.id.text_summary)
        TextView mTvSummary;
        @BindView(R.id.tv_status)
        TextView mTvStatus;
        @BindView(R.id.iv_spread)
        ImageView mIvSpread;
        @BindView(R.id.iv_shrink_up)
        ImageView mIvShrinkUp;
        @BindView(R.id.lv_column_advance)
        CustomListView mLvAdvance;
        @BindView(R.id.lv_column_class)
        CustomListView mLvClass;
        @BindView(R.id.ll_advance)
        LinearLayout mLlAdvance;
        @BindView(R.id.ll_class)
        LinearLayout mLlClass;

        @OnClick({R.id.ll_status})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.ll_status:
                    if (mState == SPREAD_STATE) {
                        mViewHolder.mTvSummary.setMaxLines(Integer.MAX_VALUE);
                        mViewHolder.mTvSummary.requestLayout();
                        mViewHolder.mIvShrinkUp.setVisibility(View.VISIBLE);
                        mViewHolder.mIvSpread.setVisibility(View.GONE);
                        mViewHolder.mTvStatus.setText("收起");
                        mState = SHRINK_UP_STATE;
                    } else if (mState == SHRINK_UP_STATE) {
                        mViewHolder.mTvSummary.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                        mViewHolder.mTvSummary.requestLayout();
                        mViewHolder.mIvShrinkUp.setVisibility(View.GONE);
                        mViewHolder.mIvSpread.setVisibility(View.VISIBLE);
                        mViewHolder.mTvStatus.setText("展开");
                        mState = SPREAD_STATE;
                    }
                    break;
            }
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}

