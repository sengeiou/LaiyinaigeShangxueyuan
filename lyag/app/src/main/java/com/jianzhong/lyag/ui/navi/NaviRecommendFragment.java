package com.jianzhong.lyag.ui.navi;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ExpandableListView;
import android.widget.FrameLayout;

import com.baselib.util.DeviceInfoUtil;
import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.BannerPageAdapter;
import com.jianzhong.lyag.adapter.NaviRecommendAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnItemsSelectInterface;
import com.jianzhong.lyag.model.BannerDisplayModel;
import com.jianzhong.lyag.model.NavigationSecModel;
import com.jianzhong.lyag.widget.customview.MyViewPagerIndicator;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 内容导航
 * Created by zhengwencheng on 2018/1/26 0026.
 * package com.jianzhong.bs.ui.navigation
 */
public class NaviRecommendFragment extends BaseFragment {

    @BindView(R.id.expandable_listview)
    ExpandableListView mExpandableListview;
    //
    private View headView;
    private ViewHolder mViewHolder;
    private NaviRecommendAdapter mNaviRecommendAdapter;
    private List<NavigationSecModel> mList = new ArrayList<>();

    private BannerPageAdapter mBannerPageAdapter; //广告栏的适配器
    private List<BannerDisplayModel> mBannerDisplayModels = new ArrayList<>();

    public static NaviRecommendFragment getInstance(List<NavigationSecModel> mNavigationSecModels) {
        NaviRecommendFragment fragment = new NaviRecommendFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mNavigationSecModels", (Serializable) mNavigationSecModels);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_navigation_sec, null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();

        headView = LayoutInflater.from(mContext).inflate(R.layout.content_navigation_headview, null);
        mViewHolder = new ViewHolder(headView);
        mExpandableListview.addHeaderView(headView);

        getNaviBanner();

        List<NavigationSecModel> mNavigationSecModels = (List<NavigationSecModel>) getArguments().getSerializable("mNavigationSecModels");
        if (!ListUtils.isEmpty(mNavigationSecModels)) {
            mList.addAll(mNavigationSecModels);
            mNaviRecommendAdapter = new NaviRecommendAdapter(getActivity(), mList);
            mExpandableListview.setAdapter(mNaviRecommendAdapter);
            mExpandableListview.setGroupIndicator(null);
            mExpandableListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
                @Override
                public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                    return true;
                }
            });

            notifyDataSetChanged();
        }
    }

    /**
     * 获取推荐栏的图片
     */
    private void getNaviBanner(){
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_NAVI_BANNER, null, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<BannerDisplayModel> resultList = GsonUtils.json2List(s,BannerDisplayModel.class);
                if(resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS){
                    if(!ListUtils.isEmpty(resultList.getData())){
                        mBannerDisplayModels.addAll(resultList.getData());
                        initBanner();
                    }else {
                        mViewHolder.mLayoutAdv.setVisibility(View.GONE);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    /**
     * 推荐的广告栏
     */
    private void initBanner() {
        /**获取屏幕宽度 设计图片的高度*/
        WindowManager wm = getActivity().getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        //获取控件现在的布局参数对象
        int mScreenWidth = dm.widthPixels/10*7 - DeviceInfoUtil.dip2px(mContext,16);// 获取屏幕分辨率宽度
        ViewGroup.LayoutParams layoutParams = mViewHolder.mLayoutAdv.getLayoutParams();
        //图片的分辨率是700 x 280
        layoutParams.height = (mScreenWidth * 280) / 700;
        mViewHolder.mLayoutAdv.setLayoutParams(layoutParams);
        if (mBannerPageAdapter == null) {
            mBannerPageAdapter = new BannerPageAdapter(mBannerDisplayModels, getActivity());
            mViewHolder.mVpBanners.setAdapter(mBannerPageAdapter);
            mViewHolder.mVpViewpagerindicator.setViewPager(mViewHolder.mVpBanners, new OnItemsSelectInterface() {
                @Override
                public void OnClick(int which) {
//                    mViewHolder.mTvAdv.setText(mBannerDisplayModels.get(which).getTitle());
                }
            });
            mViewHolder.mVpViewpagerindicator.autoScrollEnable(true);
        } else {
            mBannerPageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 刷新Adapter
     *
     * @param
     */
    private void notifyDataSetChanged() {
        if (!ListUtils.isEmpty(mList)) {
            // 设置ExpandableListView的子项默认展开
            for (int i = 0; i < mList.size(); i++) {
                mExpandableListview.expandGroup(i);
            }
            mNaviRecommendAdapter.notifyDataSetChanged();
        }
    }


    static class ViewHolder {
        @BindView(R.id.vp_banners)
        ViewPager mVpBanners;
        @BindView(R.id.vp_viewpagerindicator)
        MyViewPagerIndicator mVpViewpagerindicator;
        @BindView(R.id.layout_adv)
        FrameLayout mLayoutAdv;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
