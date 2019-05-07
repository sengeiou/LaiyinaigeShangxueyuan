package com.jianzhong.lyag.ui.user.collect;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.Result;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.ShareCollectAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.CollectHomeModel;
import com.jianzhong.lyag.model.CollectModel;
import com.jianzhong.lyag.util.DialogHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

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
 * 分享收藏
 * Created by zhengwencheng on 2018/3/9 0009.
 * package com.jianzhong.bs.ui.user.collect
 */

public class ShareFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.tv_del)
    TextView mTvDel;
    @BindView(R.id.ll_item)
    LinearLayout mLlItem;

    private List<CollectModel> mList = new ArrayList<>();
    private ShareCollectAdapter mAdapter;
    private int pageIndex = 1;

    public static ShareFragment newInstance() {

        Bundle args = new Bundle();

        ShareFragment fragment = new ShareFragment();
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void isEdit(String tag) {
        if (tag.equals(AppConstants.TAG_EDIT_COLLECT)) {
            if (GroupVarManager.getInstance().isEditCollect == 1) {
                mLlItem.setVisibility(View.VISIBLE);
            }else {
                mLlItem.setVisibility(View.GONE);
                for (CollectModel item: mList) {
                    item.setIsSelected(0);
                }
            }
            mAdapter.setIsEdit(GroupVarManager.getInstance().isEditCollect);
            mAdapter.notifyDataSetChanged();
        } else if (tag.equals(AppConstants.TAG_SHARE)) {
            mTvDel.setText("删除（" + GroupVarManager.getInstance().mCollectList.size() + "）");
        }
        isShowNoDataView();
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new ShareCollectAdapter(mContext, mList,0);
        return mAdapter;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_column,null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();

        initRecylerView();

        getCollect();
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
                getCollect();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                mList.clear();
                getCollect();
            }
        });
    }

    /**
     * 获取收藏列表
     */
    private void getCollect() {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", AppConstants.TAG_SHARE);
        params.put("p", pageIndex + "");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_FAVOR_LIST, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<CollectHomeModel> resultList = GsonUtils.json2Bean(s, CollectHomeModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    mPtrFrame.refreshComplete();
                    mList.addAll(resultList.getData().getDetail());
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(mContext, resultList != null ? resultList.getMessage() : "数据解析错误");
                }
                isShowNoDataView();
            }

            @Override
            public void onFailure(String msg) {
                hideCommonLoading();
                ToastUtils.show(mContext, msg);
                showErrorView();
            }
        });
    }

    @OnClick({R.id.ll_all, R.id.ll_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_all:
                for (int i = 0; i < mList.size(); i++) {
                    mList.get(i).setIsSelected(1);
                    GroupVarManager.getInstance().mCollectList.add(mList.get(i).getItem_id());
                    mTvDel.setText("删除（" + GroupVarManager.getInstance().mCollectList.size() + "）");
                }
                mAdapter.notifyDataSetChanged();
                isShowNoDataView();
                break;
            case R.id.ll_del:
                new AlertDialog.Builder(getActivity()).setTitle("提示").
                        setMessage("确认删除所选收藏吗").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delFavor();
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();
                break;
        }
    }

    /**
     * 删除
     */
    private void delFavor(){
        Map<String,Object> params = new HashMap<>();
        params.put("item_id",GsonUtils.toJson(GroupVarManager.getInstance().mCollectList));
        DialogHelper.showDialog(mContext);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_FAVOR_BATCH_DEL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                DialogHelper.dismissDialog();
                Result<String> result = GsonUtils.json2Bean(s,String.class);
                if(result.getCode() == HttpConfig.STATUS_SUCCESS){
                    pageIndex = 1;
                    mList.clear();
                    getCollect();
                    EventBus.getDefault().post(AppConstants.TAG_RESET_COLLECT);
                }else {
                    ToastUtils.show(mContext,result.getMessage());
                }
                isShowNoDataView();
            }

            @Override
            public void onFailure(String msg) {
                DialogHelper.dismissDialog();
                ToastUtils.show(mContext,msg);
            }
        });
    }
}
