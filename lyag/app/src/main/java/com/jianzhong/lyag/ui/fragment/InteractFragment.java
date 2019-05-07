package com.jianzhong.lyag.ui.fragment;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.InteractAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewFragment;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.CommonMenuModel;
import com.jianzhong.lyag.model.InteractModel;
import com.jianzhong.lyag.ui.interact.AddHelpActivity;
import com.jianzhong.lyag.ui.interact.AddShareActivity;
import com.jianzhong.lyag.ui.interact.InteractActivity;
import com.jianzhong.lyag.ui.interact.RankActivity;
import com.jianzhong.lyag.ui.interact.ShareActivity;
import com.jianzhong.lyag.util.CommonMethodLogic;
import com.jianzhong.lyag.util.PopWindowUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 互动Fragment
 * Created by zhengwencheng on 2018/1/15 0015.
 * package com.jianzhong.bs.ui.fragment
 */

public class InteractFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.head_ll_back)
    LinearLayout mHeadLlBack;
    @BindView(R.id.head_title)
    TextView mHeadTitle;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.head_iv_right)
    ImageView mHeadIvRight;

    private List<InteractModel> mList = new ArrayList<>();
    private InteractAdapter mAdapter;
    private int pageIndex = 1;

    private List<CommonMenuModel> menuModels = new ArrayList<>();

    public static InteractFragment getInstance() {
        InteractFragment fragment = new InteractFragment();
        return fragment;
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new InteractAdapter(mContext, mList);
        return mAdapter;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_interact, null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();

        mHeadLlBack.setVisibility(View.GONE);
        mHeadTitle.setText("互动");
        mHeadTitle.setVisibility(View.VISIBLE);
        mHeadIvRight.setBackgroundResource(R.drawable.hd_fb);
        mHeadIvRight.setVisibility(View.VISIBLE);

        initRecylerView();
        showCommonLoading();
        getInteract();
    }

    /**
     * 初始化列表
     */
    private void initRecylerView() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_bg))
                .sizeResId(R.dimen.default_margin_8)
                .build());
        mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                pageIndex++;
                getInteract();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                mList.clear();
                getInteract();
            }
        });
    }

    /**
     * 获取互动首页
     */
    private void getInteract() {
        Map<String, Object> params = new HashMap<>();
        params.put("p", pageIndex + "");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_INTERACT_INDEX, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<InteractModel> resultList = GsonUtils.json2List(s, InteractModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    mPtrFrame.refreshComplete();
                    hideCommonLoading();
                    mList.addAll(resultList.getData());
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(mContext, resultList.getMessage());
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

    @OnClick({R.id.ll_help, R.id.ll_share, R.id.ll_start,R.id.head_iv_right})
    public void onViewClicked(View view) {
        final Intent intent;
        switch (view.getId()) {
            case R.id.ll_help:   //求助
                intent = new Intent(mContext, InteractActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_share: //分享
                intent = new Intent(mContext, ShareActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_start: //达人榜
                intent = new Intent(mContext, RankActivity.class);
                startActivity(intent);
                break;
            case R.id.head_iv_right:
                PopWindowUtil.getInstance().showMenu(mActivity, mHeadIvRight, CommonMethodLogic.getShareInteractMenu(), new PopWindowUtil.OnMenuItemClickListener() {
                    @Override
                    public void OnMenuItemClickListener(CommonMenuModel item, int position) {
                        if(position == 0){
                            Intent intent = new Intent(mContext, AddHelpActivity.class);
                            startActivity(intent);
                        }else if(position == 1){
                            Intent intent = new Intent(mContext, AddShareActivity.class);
                            startActivity(intent);
                        }
                    }
                });
                break;
        }
    }
}
