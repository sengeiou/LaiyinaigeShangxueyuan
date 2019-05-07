package com.jianzhong.lyag.ui.user.history;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.CourseHistoryAdapter;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 观看历史
 * Created by zhengwencheng on 2018/3/8 0008.
 * package com.jianzhong.bs.ui.user.history
 */

public class HistoryCourseActivity extends BaseRecyclerViewActivity {

    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    private int pageIndex = 1;
    private List<TaskReferModel> mList = new ArrayList<>();
    private CourseHistoryAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history_course);
        ButterKnife.bind(this);
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new CourseHistoryAdapter(mContext, mList);
        return mAdapter;
    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("观看历史");
        showHeadTitle();

        initRecylerView();

        getCourseHistory();
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
                getCourseHistory();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                mList.clear();
                getCourseHistory();
            }
        });
    }

    private void getCourseHistory() {
        Map<String, Object> params = new HashMap<>();
        params.put("p", pageIndex + "");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COURSE_HISTORY, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                mPtrFrame.refreshComplete();
                ResultList<TaskReferModel> resultList = GsonUtils.json2List(s, TaskReferModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    mList.addAll(resultList.getData());
                    mAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(mContext, resultList != null ? resultList.getMessage():"数据解析错误");
                }
                isShowNoDataView();
            }

            @Override
            public void onFailure(String msg) {
                mPtrFrame.refreshComplete();
                ToastUtils.show(mContext,msg);
                showErrorView();
            }
        });
    }
}
