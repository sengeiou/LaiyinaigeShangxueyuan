package com.jianzhong.lyag.ui.exam;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.TaskReferAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewActivity;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.TaskReferModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 通关任务 子任务 列表
 * Created by zhengwencheng on 2018/2/1 0001.
 * package com.jianzhong.bs.ui.exam
 */
public class TaskReferActiVity extends BaseRecyclerViewActivity {
    private String title;
    private String task_id;
    private String top_task_id;
    private TaskReferAdapter mAdapter;
    private List<TaskReferModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_refer);
        ButterKnife.bind(this);
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new TaskReferAdapter(mContext, mList,task_id);
        return mAdapter;
    }

    @Override
    public void initData() {
        super.initData();

        title = getIntent().getStringExtra("title");
        top_task_id = getIntent().getStringExtra("top_task_id");
        task_id = getIntent().getStringExtra("task_id");

        setHeadTitle(title);
        showHeadTitle();
        //
        initRecylerView();

        getTaskRefer();
    }

    /**
     * 初始化列表
     */
    private void initRecylerView() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_grey_divider))
                .sizeResId(R.dimen.default_divider_one)
                .build());
        mPtrFrame.setMode(PtrFrameLayout.Mode.NONE);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
    }

    /**
     *
     */
    private void getTaskRefer() {
        Map<String, Object> params = new HashMap<>();
        params.put("task_id", task_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_TASK_REFER, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                hideCommonLoading();
                ResultList<TaskReferModel> resultList = GsonUtils.json2List(s, TaskReferModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    mList.addAll(resultList.getData());
                    mAdapter.setTask_id(task_id);
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(mContext, resultList != null ? resultList.getMessage() : "数据错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                hideCommonLoading();
                ToastUtils.show(mContext, msg);
            }
        });
    }

    @OnClick(R.id.ll_start)
    public void onViewClicked() { //开始通关
        Intent intent = new Intent(mContext, ClearanceExamActivity.class);
        intent.putExtra("task_id",task_id);
        intent.putExtra("top_task_id",top_task_id);
        intent.putExtra("title",title);
        mContext.startActivity(intent);
    }
}
