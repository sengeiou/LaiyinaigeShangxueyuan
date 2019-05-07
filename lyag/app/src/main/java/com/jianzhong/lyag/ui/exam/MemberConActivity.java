package com.jianzhong.lyag.ui.exam;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import com.baselib.util.GsonUtils;
import com.baselib.util.ResultList;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.MemberConAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewActivity;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.MemberConModel;
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
 * 成员列表 已学习或者
 * Created by zhengwencheng on 2018/3/6 0006.
 * package com.jianzhong.bs.ui.exam
 */

public class MemberConActivity extends BaseRecyclerViewActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;

    private String title;
    private String course_id;
    private String notice_id;
    private String help_id;
    private String share_id;
    private String dictate_id;
    private String live_id;
    private List<MemberConModel> mList = new ArrayList<>();
    private MemberConAdapter mAdapter;
    private int pageIndex = 1;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member_con);
        ButterKnife.bind(this);
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new MemberConAdapter(mContext, mList);
        return mAdapter;
    }

    @Override
    public void initData() {
        super.initData();

        title = getIntent().getStringExtra("title");
        course_id = getIntent().getStringExtra("course_id");
        notice_id = getIntent().getStringExtra("notice_id");
        help_id = getIntent().getStringExtra("help_id");
        share_id = getIntent().getStringExtra("share_id");
        dictate_id = getIntent().getStringExtra("dictate_id");
        live_id = getIntent().getStringExtra("live_id");

        setHeadTitle(title);
        showHeadTitle();
        if (!StringUtils.isEmpty(course_id)) {
            type = 1;
        } else if(!StringUtils.isEmpty(help_id)){
            type = 2;
        }else if(!StringUtils.isEmpty(share_id)){
            type = 3;
        }else if(!StringUtils.isEmpty(dictate_id)){
            type = 4;
        }else if(!StringUtils.isEmpty(live_id)){
            type = 5;
        }else {
            type = 0;
        }
        initRecylerView();
        getStudyUserAll();
    }

    /**
     * 初始化
     */
    private void initRecylerView() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_grey_divider))
                .sizeResId(R.dimen.default_divider_one)
                .build());
        if (type == 1 || type == 2 || type == 4 || type ==5) {
            mPtrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
        } else {
            mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);
        }

        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                pageIndex++;
                getStudyUserAll();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                mList.clear();
                getStudyUserAll();

            }
        });
    }

    private void getStudyUserAll() {
        Map<String, Object> params = new HashMap<>();
        if (type == 1) {
            params.put("course_id", course_id);
        } else if(type == 2){
            params.put("help_id", help_id);
        }else if(type == 3){
            params.put("share_id", share_id);
        }else if(type == 4){
            params.put("dictate_id", dictate_id);
        }else if(type == 5){
            params.put("live_id", live_id);
        }else {
            params.put("notice_id", notice_id);
            params.put("p", pageIndex + "");
        }
        String url;
        if(type == 1){
            url = HttpConfig.URL_BASE + HttpConfig.URL_STUDY_USER_ALL;
        }else if(type == 2){
            url = HttpConfig.URL_BASE + HttpConfig.URL_HELP_LIKE_USER_ALL;
        }else if(type == 3){
            url = HttpConfig.URL_BASE + HttpConfig.URL_SHARE_LIKE_USER_ALL;
        }else if(type == 4){
            url = HttpConfig.URL_BASE + HttpConfig.URL_DICTATE_UNFINISH_USER;
        }else if(type == 5){
            url = HttpConfig.URL_BASE + HttpConfig.URL_LIVE_WATCHER_ALL;
        }else {
            url = HttpConfig.URL_BASE + HttpConfig.URL_NOTICE_UNREAD_USER_ALL;
        }

        HttpRequest.getInstance().post(url, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<MemberConModel> resultList = GsonUtils.json2List(s, MemberConModel.class);
                if (resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    mPtrFrame.refreshComplete();
                    mList.addAll(resultList.getData());
                    mAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }
}
