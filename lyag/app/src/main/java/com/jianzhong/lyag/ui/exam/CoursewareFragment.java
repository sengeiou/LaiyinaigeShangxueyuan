package com.jianzhong.lyag.ui.exam;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.CoursewareAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.CoursewareModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 课件Fragment
 * Created by zhengwencheng on 2018/2/27 0027.
 * package com.jianzhong.bs.ui.exam
 */

public class CoursewareFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    private CoursewareAdapter mAdapter;
    private List<CoursewareModel> mList = new ArrayList<>();
    private String course_id;

    public static CoursewareFragment newInstance(String course_id) {
        Bundle args = new Bundle();
        CoursewareFragment fragment = new CoursewareFragment();
        args.putString("course_id", course_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_courseware, null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();
        course_id = getArguments().getString("course_id");
        //
        initRecylerView();

        getCourseware();
    }

    /**
     * 初始化列表
     */
    private void initRecylerView() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_bg))
                .sizeResId(R.dimen.default_margin_8)
                .build());

        mAdapter = new CoursewareAdapter(mContext,mList);
        mRecyclerView.setAdapter(mAdapter);
        // 避免出现RecyclerView has no LayoutManager的错误
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    /**
     * 获取课件列表
     */
    private void getCourseware() {
        Map<String, Object> params = new HashMap<>();
        params.put("course_id", course_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_WARE_LIST, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<CoursewareModel> resultList = GsonUtils.json2List(s, CoursewareModel.class);
                if (resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    if(!ListUtils.isEmpty(resultList.getData())){
                        mList.addAll(resultList.getData());
                    }
                    initRecylerView();

                } else {
                    ToastUtils.show(mContext, resultList.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

}
