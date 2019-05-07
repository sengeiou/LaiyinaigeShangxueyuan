package com.jianzhong.lyag.ui.navi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.ResultList;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.NaviResultAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewActivity;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.NaviResultListModel;
import com.jianzhong.lyag.model.NaviResultModel;
import com.jianzhong.lyag.model.NavigationFirstModel;
import com.jianzhong.lyag.model.NavigationSecModel;
import com.jianzhong.lyag.model.NavigationSecSonModel;
import com.jianzhong.lyag.model.OrderKeyModel;
import com.jianzhong.lyag.util.PopWindowUtil;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 内容导航结果列表
 * Created by Max on 2018/1/28.
 */
public class NaviResultActivity extends BaseRecyclerViewActivity {

    @BindView(R.id.ll_sort)
    LinearLayout mLlSort;
    @BindView(R.id.ll_classify)
    LinearLayout mLlClassify;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
//    @BindView(R.id.tv_sort)
//    TextView mTvSort;
    @BindView(R.id.tv_classify)
    TextView mTvClassify;
    @BindView(R.id.tv_type)
    TextView mTvType;
    @BindView(R.id.tv_count)
    TextView mTvCount;

    private TextView mTvSort;
    private List<NavigationFirstModel> mList = new ArrayList<>();
    private String navi_id;
    private String search_key;
    private String search;
    private String order_by;
    private int pageIndex = 1;
    private String title;
    private List<OrderKeyModel> mOrderKeyModels = new ArrayList<>();
    private List<NaviResultListModel> mNaviResultList = new ArrayList<>();
    private NaviResultAdapter mNaviResultAdapter;
    private String is_more;
    private int type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation_result);
        ButterKnife.bind(this);
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mNaviResultAdapter = new NaviResultAdapter(mContext, mNaviResultList, "");
        return mNaviResultAdapter;
    }

    @Override
    public void initData() {
        super.initData();

        navi_id = getIntent().getStringExtra("navi_id");
        search_key = getIntent().getStringExtra("search_key");
        search = getIntent().getStringExtra("search");
        title = getIntent().getStringExtra("title");
        is_more = getIntent().getStringExtra("is_more");
        type = getIntent().getIntExtra("type", 1);
        setHeadTitle(title);
        showHeadTitle();
        //
        initRecylerView();
        getNaviSort();
    }


    /**
     * 初始化列表
     */
    private void initRecylerView() {
        mTvSort = findViewById(R.id.tv_sort);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_grey_divider))
                .sizeResId(R.dimen.default_divider_one)
                .build());
        mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                pageIndex++;
                getNaviSearch();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                mNaviResultList.clear();
                getNaviSearch();
            }
        });
    }

    @OnClick({R.id.head_iv_right, R.id.ll_sort, R.id.ll_classify})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.head_iv_right: //搜索
                Intent intent = new Intent(mContext, CommonSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_sort:      //排序
                PopWindowUtil.getInstance().showOrderByKey(NaviResultActivity.this, mLlSort, mOrderKeyModels,mTvSort, new PopWindowUtil.OnOrderByKeyClickListener() {

                    @Override
                    public void orderByKeyClick(OrderKeyModel item, int position) {
                        order_by = item.getKey();
                        mTvSort.setText(item.getStr());
                        pageIndex = 1;
                        is_more = "0";
                        mNaviResultList.clear();
                        getNaviSearch();
                    }
                });
                break;
            case R.id.ll_classify:  //分类
                PopWindowUtil.getInstance().showNavigationSelect(NaviResultActivity.this, mLlClassify, mList, mTvClassify, new PopWindowUtil.OnItemSecClickListener() {
                    @Override
                    public void OnItemSecClick(NavigationSecModel item, int position) {
                        if (item != null) {
                            search = item.getSearch();
                            search_key = item.getSearch_key();
                            navi_id = item.getNavi_id();
                        }
                        pageIndex = 1;
                        is_more = "0";
                        mNaviResultList.clear();
                        getNaviSearch();
                    }
                }, new PopWindowUtil.OnItemSecSonClickListener() {
                    @Override
                    public void OnItemSecSonClick(NavigationSecSonModel item, int position) {
                        if (item != null) {
                            search = item.getSearch();
                            search_key = item.getSearch_key();
                            navi_id = item.getNavi_id();
                        }
                        pageIndex = 1;
                        is_more = "0";
                        mNaviResultList.clear();
                        getNaviSearch();
                    }
                }, new PopWindowUtil.OnItemFirstClickListener() {
                    @Override
                    public void OnItemFirstClick(List<OrderKeyModel> items, int position) {
                        mOrderKeyModels.clear();
                        if (!ListUtils.isEmpty(items)) {
                            mOrderKeyModels.addAll(items);
                            if(!ListUtils.isEmpty(mOrderKeyModels)){
                                mTvSort.setText(mOrderKeyModels.get(0).getStr());
                                mOrderKeyModels.get(0).setIsSelected(1);
                                order_by = mOrderKeyModels.get(0).getKey();
                            }
                        }
                    }
                });
                break;
        }
    }

    /**
     * 获取内容导航列表数据
     */
    private void getNaviSearch() {
        Map<String, Object> params = new HashMap<>();
        params.put("search", search);
        params.put("order_by", order_by);
        params.put("search_key", search_key);
        params.put("navi_id", navi_id);
        params.put("is_more", is_more);
        params.put("p", pageIndex + "");

        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_NAVI_SEARCH, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<NaviResultModel> result = GsonUtils.json2Bean(s, NaviResultModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    mPtrFrame.refreshComplete();
                    mNaviResultList.addAll(result.getData().getList());
                    mNaviResultAdapter.setTag(result.getData().getAsset());
                    mNaviResultAdapter.notifyDataSetChanged();
                    mTvType.setText(mTvClassify.getText().toString());
                    mTvCount.setText("”分类下，找到" + result.getData().getCount() + "门课");
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage() : "数据解析错误");
                }
                isShowNoDataView(mNaviResultList);
            }

            @Override
            public void onFailure(String msg) {
                hideCommonLoading();
                mPtrFrame.refreshComplete();
                ToastUtils.show(mContext, msg);
                showErrorView();
            }
        });
    }

    /**
     * 先拿排序
     */
    private void getNaviSort() {
        Map<String, Object> params = new HashMap<>();
        params.put("inner", "1"); //没有推荐
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_NAVI_NOW, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<NavigationFirstModel> resultList = GsonUtils.json2List(s, NavigationFirstModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    if (!ListUtils.isEmpty(resultList.getData())) {
                        mList.clear();
                        mList.addAll(resultList.getData());
                        ergodicData();
                    }
                    getNaviSearch();
                } else {
                    ToastUtils.show(mContext, resultList != null ? resultList.getMessage() : "数据解析错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
                showErrorView();
            }
        });
    }

    /**
     * 遍历排序和什么的数据
     */
    private void ergodicData() {
        for (int i = 0; i < mList.size(); i++) {
            if (!StringUtils.isEmpty(GroupVarManager.getInstance().path_1) && mList.get(i).getPath().equals(GroupVarManager.getInstance().path_1)) {
                mList.get(i).setIsSelected(1);
                mTvClassify.setText(mList.get(i).getTitle());
                if (!ListUtils.isEmpty(mList.get(i).getOrder_by())) {
                    mOrderKeyModels.addAll(mList.get(i).getOrder_by());
                    mOrderKeyModels.get(0).setIsSelected(1);
                    order_by = mOrderKeyModels.get(0).getKey();
                    mTvSort.setText(mOrderKeyModels.get(0).getStr());
                }
                if (!ListUtils.isEmpty(mList.get(i).getSub())) { //循环二级列表设置选中
                    for (int j = 0; j < mList.get(i).getSub().size(); j++) {
                        if (type == 1) { //首页更多进来的时候设置
                            if (j == 0) {
                                mList.get(i).getSub().get(j).setIsSelected(1);
                            } else {
                                mList.get(i).getSub().get(j).setIsSelected(0);
                            }
                        } else {
                            if (!StringUtils.isEmpty(GroupVarManager.getInstance().path_2) && (mList.get(i).getSub().get(j).getPath()).equals(GroupVarManager.getInstance().path_2)) {
                                mList.get(i).getSub().get(j).setIsSelected(1);
                            } else {
                                mList.get(i).getSub().get(j).setIsSelected(0);
                            }
                        }
                    }
                }
            } else {
                mList.get(i).setIsSelected(0);
            }
        }
    }

}
