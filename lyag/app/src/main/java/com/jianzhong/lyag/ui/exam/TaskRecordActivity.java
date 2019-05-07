package com.jianzhong.lyag.ui.exam;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.TaskRecordAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewActivity;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.TaskRecordModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 成长通关记录
 * Created by zhengwencheng on 2018/3/29 0029.
 * package com.jianzhong.bs.ui.exam
 */

public class TaskRecordActivity extends BaseRecyclerViewActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    private List<TaskRecordModel> mList = new ArrayList<>();
    private TaskRecordAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_record);
        ButterKnife.bind(this);
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new TaskRecordAdapter(mContext, mList);
        return mAdapter;
    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("通关成长记录");
        showHeadTitle();

        initRecylerView();
        showCommonLoading();
        getTaskPickList();
    }

    /**
     * 初始化列表
     */
    private void initRecylerView() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_grey_divider))
                .sizeResId(R.dimen.default_divider_one)
                .build());
        mPtrFrame.setMode(PtrFrameLayout.Mode.REFRESH);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mList.clear();
                getTaskPickList();
            }
        });
    }

    private void getTaskPickList() {
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_TASK_PICK_LIST, null, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<TaskRecordModel> resultList = GsonUtils.json2List(s, TaskRecordModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    if (!ListUtils.isEmpty(resultList.getData())) {
                        hideCommonLoading();
                        mPtrFrame.refreshComplete();
                        mList.addAll(resultList.getData());
                        mAdapter.notifyDataSetChanged();
                    } else {
                        ToastUtils.show(mContext, resultList != null ? resultList.getMessage() : "数据解析错误");
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }
}
