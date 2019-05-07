package com.jianzhong.lyag.ui.navi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.FragmentViewPageAdapter;
import com.jianzhong.lyag.adapter.NaviFirstAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnItemsSelectInterface;
import com.jianzhong.lyag.model.NavigationFirstModel;
import com.jianzhong.lyag.widget.customview.NoScrollViewPager;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 内容导航首页
 * Created by zhengwencheng on 2018/1/25 0025.
 * package com.jianzhong.bs.ui.navigation
 */
public class HomeNaviActivity extends ToolbarActivity {
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.viewpager)
    NoScrollViewPager mViewpager;

    private List<NavigationFirstModel> mList = new ArrayList<>();
    private NaviFirstAdapter mNaviFirstAdapter;
    private FragmentViewPageAdapter mFragmentViewPageAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_navigation);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        String title = getIntent().getStringExtra("title");

        setHeadTitle(title);
        showHeadTitle();

        getNaviShow();
    }

    @OnClick(R.id.ll_search)
    public void onViewClicked() { //搜索
        Intent intent = new Intent(mContext,CommonSearchActivity.class);
        startActivity(intent);
    }

    private void getNaviShow() {
        Map<String, Object> params = new HashMap<>();
        params.put("inner", "0"); //有推荐
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_NAVI_NOW, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<NavigationFirstModel> resultList = GsonUtils.json2List(s, NavigationFirstModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    if (!ListUtils.isEmpty(resultList.getData())) {
                        mList.addAll(resultList.getData());
                        mList.get(0).setIsSelected(1);
                        GroupVarManager.getInstance().path_1 = mList.get(0).getPath();
                        initRecylerView();
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

    /**
     * 初始化列表
     */
    private void initRecylerView() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_grey_divider))
                .sizeResId(R.dimen.default_divider_one)
                .build());

        mRecyclerView.setHasFixedSize(true);

        mNaviFirstAdapter = new NaviFirstAdapter(mContext, mList, new OnItemsSelectInterface() {
            @Override
            public void OnClick(int which) {
                mViewpager.setCurrentItem(which,false);
                GroupVarManager.getInstance().path_1 = mList.get(which).getPath();//记录选中的唯一标识
            }
        });
        mRecyclerView.setAdapter(mNaviFirstAdapter);
        mRecyclerView.setLayoutManager(new GridLayoutManager(HomeNaviActivity.this, 1));

        //装载右侧
        List<Fragment> mFragments = new ArrayList<>();
        for (int i = 0; i < mList.size(); i++) {
            if(mList.get(i).getLabel().equals(AppConstants.NVAI_FOCUS_LABEL)){
                mFragments.add(NaviRecommendFragment.getInstance(mList.get(i).getSub()));
            }else {
                mFragments.add(NaviSecFragment.getInstance(mList.get(i).getSub()));
            }
        }

        //ViewPager设置适配器
        mFragmentViewPageAdapter = new FragmentViewPageAdapter(getSupportFragmentManager(), mFragments);
        mViewpager.setAdapter(mFragmentViewPageAdapter);
        mViewpager.setCurrentItem(0,false);
        mViewpager.setNoScroll(true);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mViewpager.setCurrentItem(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

}
