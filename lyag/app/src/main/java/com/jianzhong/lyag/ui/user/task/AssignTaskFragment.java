package com.jianzhong.lyag.ui.user.task;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.AssignTaskAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewFragment;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.AssignTaskModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 发出的学习任务
 * create by zhengwencheng on 2018/3/20 0020
 * package com.jianzhong.bs.ui.exam
 */
public class AssignTaskFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;

    private AssignTaskAdapter mAdapter;
    private List<AssignTaskModel> mList = new ArrayList<>();
    private int pageIndex = 1;
    private int isFinished;
    public static AssignTaskFragment newInstance(int isFinished) {

        Bundle args = new Bundle();

        AssignTaskFragment fragment = new AssignTaskFragment();
        args.putInt("isFinished",isFinished);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new AssignTaskAdapter(mContext,mList,0);
        return mAdapter;
    }


    @Override
    protected void initData() {
        super.initData();

        isFinished = getArguments().getInt("isFinished",0);

        initRecylerView();

        getLearnDictate();
    }

    /**
     * 初始化列表
     */
    private void initRecylerView() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_grey_divider))
                .sizeResId(R.dimen.default_divider_one)
                .build());
        mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                pageIndex++;
                getLearnDictate();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                mList.clear();
                getLearnDictate();
            }
        });
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_post_major,null);
        return mRootView;
    }

    /**
     * 获取学习任务列表
     */
    private void getLearnDictate(){
        Map<String,Object> params = new HashMap<>();
        params.put("is_finish",isFinished+"");
        params.put("p",pageIndex+"");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DICTATE_MY_SEND, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<AssignTaskModel> resultList = GsonUtils.json2List(s,AssignTaskModel.class);
                if(resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS){
                    hideCommonLoading();
                    mPtrFrame.refreshComplete();
                    if(!ListUtils.isEmpty(resultList.getData())){
                        mList.addAll(resultList.getData());
                    }
                    mAdapter.setIsFinished(isFinished);
                    mAdapter.notifyDataSetChanged();
                }else {
                    ToastUtils.show(mContext,resultList != null ? resultList.getMessage():"数据解析错误");
                }
                isShowNoDataView();
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext,msg);
                showErrorView();
            }
        });
    }
}
