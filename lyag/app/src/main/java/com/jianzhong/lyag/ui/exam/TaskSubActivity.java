package com.jianzhong.lyag.ui.exam;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.TaskSubAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewActivity;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.TaskSubModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 成长通关子任务
 * Created by zhengwencheng on 2018/2/1 0001.
 * package com.jianzhong.bs.ui.exam
 */

public class TaskSubActivity extends BaseRecyclerViewActivity {
    private String task_id;
    private String title;

    private List<TaskSubModel> mList = new ArrayList<>();
    private TaskSubAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_task_sub);
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new TaskSubAdapter(mContext,mList);
        return mAdapter;
    }

    @Override
    public void initData() {
        super.initData();

        task_id = getIntent().getStringExtra("task_id");
        title = getIntent().getStringExtra("title");

        setHeadTitle(title);
        showHeadTitle();

        initRecylerView();
        getTaskSubList();
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
     * 获取成长通关子列表
     */
    private void getTaskSubList(){
        Map<String,Object> params = new HashMap<>();
        params.put("top_task_id",task_id);

        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_TASK_SUB_LSIT, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {

                ResultList<TaskSubModel> resultList = GsonUtils.json2List(s,TaskSubModel.class);
                if(resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS){
                    hideCommonLoading();
                    mList.addAll(resultList.getData());
                    mAdapter.notifyDataSetChanged();
                }else {
                    ToastUtils.show(mContext,resultList != null ? resultList.getMessage():"数据错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext,msg);
            }
        });
    }
}
