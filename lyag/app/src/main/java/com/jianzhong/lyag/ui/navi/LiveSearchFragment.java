package com.jianzhong.lyag.ui.navi;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.LiveSearchAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.LiveModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 搜索直播列表
 * Created by zhengwencheng on 2018/3/27 0027.
 * package com.jianzhong.bs.ui.navi
 */
public class LiveSearchFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    private String keyWord;
    private int pageIndex = 1;

    private LiveSearchAdapter mAdapter;
    private List<LiveModel> mList = new ArrayList<>();

    public static LiveSearchFragment newInstance(String keyWord) {

        Bundle args = new Bundle();

        LiveSearchFragment fragment = new LiveSearchFragment();
        args.putString("keyWord", keyWord);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new LiveSearchAdapter(mContext,mList);
        return mAdapter;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_course_search, null);
        return mRootView;
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
    public void updateSearch(String tag) {
        if (tag.equals(AppConstants.TAG_LIVE)) {
            keyWord = GroupVarManager.getInstance().keyWord;

            mPtrFrame.autoRefresh();
        }
    }

    @Override
    protected void initData() {
        super.initData();

        keyWord = getArguments().getString("keyWord");

        initRecylerView();
        showCommonLoading();
        getSearchFindLive();
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
                getSearchFindLive();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                mList.clear();
                getSearchFindLive();
            }
        });
    }

    private void getSearchFindLive(){
        Map<String,Object> params = new HashMap<>();
        params.put("key_word",keyWord);
        params.put("p",pageIndex+"");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_SERCH_FIND_LIVE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<LiveModel> resultList = GsonUtils.json2List(s,LiveModel.class);
                if(resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS){
                    mPtrFrame.refreshComplete();
                    hideCommonLoading();
                    if(!ListUtils.isEmpty(resultList.getData())){
                        mList.addAll(resultList.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                }else {
                    ToastUtils.show(mContext,resultList != null? resultList.getMessage() :"数据解析错误");
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
