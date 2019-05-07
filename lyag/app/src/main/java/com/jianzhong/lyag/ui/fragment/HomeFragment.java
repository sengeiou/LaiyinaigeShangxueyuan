package com.jianzhong.lyag.ui.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.baselib.widget.CustomListView;
import com.baselib.widget.xlistview.XScrollView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.AudioDryCargoAdapter;
import com.jianzhong.lyag.adapter.BannerPageAdapter;
import com.jianzhong.lyag.adapter.ColumnTodayAdapter;
import com.jianzhong.lyag.adapter.DirectSeedingAdapter;
import com.jianzhong.lyag.adapter.ImportantInfoAdapter;
import com.jianzhong.lyag.adapter.RecommendTodayAdapter;
import com.jianzhong.lyag.adapter.SpecialAdapter;
import com.jianzhong.lyag.base.AbFragmentPagerAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnImporetInfoListener;
import com.jianzhong.lyag.listener.OnItemsSelectInterface;
import com.jianzhong.lyag.model.AudioDetailModel;
import com.jianzhong.lyag.model.BannerDisplayModel;
import com.jianzhong.lyag.model.ColumnContentModel;
import com.jianzhong.lyag.model.CourseListModel;
import com.jianzhong.lyag.model.FocusModel;
import com.jianzhong.lyag.model.HomeIndexModel;
import com.jianzhong.lyag.model.LiveModel;
import com.jianzhong.lyag.model.NoticeMsgModel;
import com.jianzhong.lyag.ui.exam.StudyTaskActivity;
import com.jianzhong.lyag.ui.navi.CommonSearchActivity;
import com.jianzhong.lyag.ui.navi.HomeNaviActivity;
import com.jianzhong.lyag.ui.navi.NaviResultActivity;
import com.jianzhong.lyag.ui.notice.NoticeActivity;
import com.jianzhong.lyag.ui.notice.NoticeDetailActivity;
import com.jianzhong.lyag.ui.user.history.HistoryCourseActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.widget.customview.MyViewPagerIndicator;
import com.jianzhong.lyag.widget.customview.TextSwitcherView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 首页Fragment
 * Created by zhengwencheng on 2018/1/15 0015.
 * package com.jianzhong.bs.ui.fragment
 */
public class HomeFragment extends BaseFragment {
    @BindView(R.id.scroll_view)
    XScrollView mScrollView;
    @BindView(R.id.ll_search)
    LinearLayout mLlSearch;
    @BindView(R.id.iv_history)
    ImageView mIvHistory;

    private View mView; //要添加的头view
    private ViewHolder mViewHolder;
    private ArrayList<NoticeMsgModel> mImportantInfoList = new ArrayList<>(); //重要通知数据列表
    private ImportantInfoAdapter mImportantInfoAdapter;
    private List<NoticeMsgModel> cache1 = new ArrayList<>();
    private List<NoticeMsgModel> cache2 = new ArrayList<>();
    private static final int UPDATE_IMPORTANT_INFO = 0;
    private static final int UPDATE_ViewPagerHeight = 1001;
    private AbFragmentPagerAdapter mAbFragmentPagerAdapter;
    private RecommendTodayAdapter mRecommendTodayAdapter; //每日推荐列表的适配器
    private ColumnTodayAdapter mColumnTodayAdapter; //每日推荐列表的适配器
    private List<FocusModel> mRecommendTodayModels = new ArrayList<>();
    private List<FocusModel> mColumnContentModels = new ArrayList<>();
    private DirectSeedingAdapter mDirectSeedingAdapter; //直播列表适配器
    private List<LiveModel> mDirectSeedingModels = new ArrayList<>();
    private AbFragmentPagerAdapter mGoodClassPagerAdapter;     //
    private SpecialAdapter mSpecialAdapter; //每日专栏适配器
    private List<ColumnContentModel> mSpecialModels = new ArrayList<>();
    private HomeIndexModel data;
    private BannerPageAdapter mBannerPageAdapter; //广告栏的适配器
    private List<BannerDisplayModel> mBannerDisplayModels = new ArrayList<>();
    //记录当前精品课程的顶层id
    private String top_cat_id;
    //音频列表改版的适配器
    private AudioDryCargoAdapter mAudioDryCargoAdapter;
    private List<AudioDetailModel> mAudioDetailModels = new ArrayList<>();

    public static HomeFragment getInstance() {
        HomeFragment fragment = new HomeFragment();
        return fragment;
    }

    @Override
    protected void initData() {
        super.initData();

        showCommonLoading();
        //获取首页数据
        getHomeIndex();
    }

    /**
     * 初始化View
     */
    private void initView() {
        if (data == null) {
            return;
        }
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {
            @Override
            public void onRefresh() {
                getHomeIndex();
            }

            @Override
            public void onLoadMore() {
            }
        });
        mScrollView.setRefreshTime(CommonUtils.getTime());
        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.content_home, null);
            mViewHolder = new ViewHolder(mView);
            mViewHolder.mLayoutAdv.setFocusable(true);
            mViewHolder.mLvRecommend.setFocusable(false);
            mViewHolder.mLvSpecial.setFocusable(false);
            mViewHolder.mLvDirectSeeding.setFocusable(false);
            mViewHolder.mLvFocusColum.setFocusable(false);
            mViewHolder.mVpGood.setFocusable(false);
            mViewHolder.mTlGood.setFocusable(false);
            mViewHolder.mTextSwitcherView.setFocusable(false);
            mViewHolder.mLvAudio.setFocusable(false);
            mScrollView.setView(mView);
        }

        mLlSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, CommonSearchActivity.class);
                startActivity(intent);
            }
        });

        mIvHistory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, HistoryCourseActivity.class);
                startActivity(intent);
            }
        });

        //显示隐藏模块
        showItem();
        initBanner();
        //
        initImportantInfo();
        //每日音频
        initDryCargo();
        //每日推荐
        initRecommend();
        //直播推荐
        initDirectSeeding();
        //精品课程
        initGoodClass();
        //专栏
        initSpecial();
    }

    /**
     * 获取
     */
    private void getHomeIndex() {
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_HOME_INDEX, null, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {

                Result<HomeIndexModel> result = GsonUtils.json2Bean(s, HomeIndexModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    mScrollView.stopRefresh();
                    data = result.getData();
                    initView();
                } else {
                    if (result != null) {
                        ToastUtils.show(mContext, result.getMessage());
                    } else {
                        ToastUtils.show(mContext, "数据解析出错");
                    }
                }
                isShowNoDataView(data);
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
                showErrorView();
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
        int mScreenWidth = dm.widthPixels;// 获取屏幕分辨率宽度
        ViewGroup.LayoutParams layoutParams = mViewHolder.mLayoutAdv.getLayoutParams();
        //图片的分辨率是700 x 280
        layoutParams.height = (mScreenWidth * 280) / 700;
        mViewHolder.mLayoutAdv.setLayoutParams(layoutParams);
        if (!mBannerDisplayModels.isEmpty()) {
            mBannerDisplayModels.clear();
        }
        mBannerDisplayModels.addAll(data.getBanner());
        if (mBannerPageAdapter == null) {
            mBannerPageAdapter = new BannerPageAdapter(mBannerDisplayModels, getActivity());
            mViewHolder.mVpBanners.setAdapter(mBannerPageAdapter);
            if (mBannerDisplayModels.size() > 0) {
                mViewHolder.mTvAdv.setText(mBannerDisplayModels.get(0).getTitle());
            }
            mViewHolder.mVpViewpagerindicator.setViewPager(mViewHolder.mVpBanners, new OnItemsSelectInterface() {
                @Override
                public void OnClick(int which) {
                    mViewHolder.mTvAdv.setText(mBannerDisplayModels.get(which).getTitle());
                }
            });
            mViewHolder.mVpViewpagerindicator.autoScrollEnable(true);
        } else {
            mBannerPageAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 设置重要通知展示
     */
    private void initImportantInfo() {
        if(!ListUtils.isEmpty(data.getNotice())){
            mViewHolder.mTextSwitcherView.getResource(data.getNotice());
            mViewHolder.mTextSwitcherView.updateTextSwitcher();
            mViewHolder.mTextSwitcherView.setOnImportInfoClick(new OnImporetInfoListener() {
                @Override
                public void OnImportInfo(NoticeMsgModel item) {
                    Intent intent = new Intent(mContext, NoticeDetailActivity.class);
                    intent.putExtra("notice_id", item.getNotice_id());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    /**
     * 每日音频
     */
    private void initDryCargo() {
        mAudioDetailModels.clear();
        mAudioDetailModels.addAll(data.getAudio());
        if (ListUtils.isEmpty(mAudioDetailModels)) {
            mViewHolder.mLlDryCargo.setVisibility(View.GONE);
        } else {
            mViewHolder.mLlDryCargo.setVisibility(View.VISIBLE);
        }
        if (mAudioDryCargoAdapter == null) {
            mAudioDryCargoAdapter = new AudioDryCargoAdapter(mContext, mAudioDetailModels);
            mViewHolder.mLvAudio.setAdapter(mAudioDryCargoAdapter);
        } else {
            mAudioDryCargoAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 每日推荐
     */
    private void initRecommend() {
        if(data.getFocus() != null){
            if (data.getFocus().getFocus_type().equals(AppConstants.TAG_COURSE)) {
                mViewHolder.mLvRecommend.setVisibility(View.VISIBLE);
                mViewHolder.mLvFocusColum.setVisibility(View.GONE);
                mRecommendTodayModels.clear();
                mRecommendTodayModels.add(data.getFocus());
                if (mRecommendTodayAdapter == null) {
                    mRecommendTodayAdapter = new RecommendTodayAdapter(mContext, mRecommendTodayModels);
                    mViewHolder.mLvRecommend.setAdapter(mRecommendTodayAdapter);
                } else {
                    mRecommendTodayAdapter.notifyDataSetChanged();
                }
            } else {
                mViewHolder.mLvRecommend.setVisibility(View.GONE);
                mViewHolder.mLvFocusColum.setVisibility(View.VISIBLE);
                mColumnContentModels.clear();
                mColumnContentModels.add(data.getFocus());
                if (mColumnTodayAdapter == null) {
                    mColumnTodayAdapter = new ColumnTodayAdapter(mContext, mColumnContentModels);
                    mViewHolder.mLvFocusColum.setAdapter(mColumnTodayAdapter);
                } else {
                    mColumnTodayAdapter.notifyDataSetChanged();
                }
            }
        }
    }

    /**
     * 直播间
     */
    private void initDirectSeeding() {
        mDirectSeedingModels.clear();
        mDirectSeedingModels.addAll(data.getLive());
        if (ListUtils.isEmpty(mDirectSeedingModels)) {
            mViewHolder.mLlLiveRoom.setVisibility(View.GONE);
        } else {
            mViewHolder.mLlLiveRoom.setVisibility(View.VISIBLE);
        }
        if (mDirectSeedingAdapter == null) {
            mDirectSeedingAdapter = new DirectSeedingAdapter(mContext, mDirectSeedingModels);
            mViewHolder.mLvDirectSeeding.setAdapter(mDirectSeedingAdapter);
        } else {
            mDirectSeedingAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 精品课程
     */
    private void initGoodClass() {
        if (ListUtils.isEmpty(data.getCourse())) {
            mViewHolder.mLlCourse.setVisibility(View.GONE);
        } else {
            mViewHolder.mLlCourse.setVisibility(View.VISIBLE);
            String[] mGoodClassTitle = new String[data.getCourse().size()]; //精品课程
            for (int i = 0; i < data.getCourse().size(); i++) {
                mGoodClassTitle[i] = data.getCourse().get(i).getCat_name();
            }
            ArrayList<Fragment> fragmentList = new ArrayList<>();
            for (int i = 0; i < data.getCourse().size(); i++) {
                if (i == 0) {
                    top_cat_id = data.getCourse().get(i).getTop_cat_id();
                }
                fragmentList.add(GoodClassFragment.getInstance(data.getCourse().get(i).getDetail(), i, data.getCourse().get(i).getTop_cat_id()));
            }
            mGoodClassPagerAdapter = new AbFragmentPagerAdapter(getChildFragmentManager(), fragmentList, mGoodClassTitle);
            mViewHolder.mVpGood.setAdapter(mGoodClassPagerAdapter);
            mViewHolder.mTlGood.setupWithViewPager(mViewHolder.mVpGood);
            mViewHolder.mVpGood.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
                @Override
                public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

                }

                @Override
                public void onPageSelected(int position) {
                    top_cat_id = data.getCourse().get(position).getTop_cat_id();
                    GroupVarManager.getInstance().coursePosition = position;
                    //重新设置高度
                    if (GroupVarManager.getInstance().mGoodClassMap.get(position) != null) {
                        mViewHolder.mVpGood.setLayoutParams(GroupVarManager.getInstance().mGoodClassMap.get(position));
                    } else {
//                    mViewHolder.mVpGood.setLayoutParams(new LinearLayout.LayoutParams(0,0));
                    }
                }

                @Override
                public void onPageScrollStateChanged(int state) {

                }
            });
        }
    }

    //重新设置ViewPager高度 主要针对初始化的时候第一个Fragment调用
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getViewPagerHeight(String tag) {
        if (tag.equals(AppConstants.EVENT_GOOD_CLASS)) {
            mViewHolder.mVpGood.setLayoutParams(GroupVarManager.getInstance().mGoodClassMap.get(GroupVarManager.getInstance().coursePosition));
        } else if (tag.equals(AppConstants.TAG_CLOSE_AUDIO)) {
            if (!ListUtils.isEmpty(mAudioDetailModels)) {
                for (int i = 0; i < mAudioDetailModels.size(); i++) {
                    mAudioDetailModels.get(i).setIsPlay(0);
                }
                mAudioDryCargoAdapter.notifyDataSetChanged();
            }
        } else if (tag.equals(AppConstants.TAG_UPDATE_AUDIO)) {
            if (!ListUtils.isEmpty(mAudioDetailModels) && GroupVarManager.getInstance().mAudioDetailModel != null) {
                for (int i = 0; i < mAudioDetailModels.size(); i++) {
                    if (GroupVarManager.getInstance().mAudioDetailModel.getAudio_id().equals(mAudioDetailModels.get(i).getAudio_id())) {
                        mAudioDetailModels.get(i).setIsPlay(1);
                    } else {
                        mAudioDetailModels.get(i).setIsPlay(0);
                    }
                }
                mAudioDryCargoAdapter.notifyDataSetChanged();
            }
        }
    }

    private void initSpecial() {
        mSpecialModels.clear();
        mSpecialModels.addAll(data.getColumn());
        if (ListUtils.isEmpty(mSpecialModels)) {
            mViewHolder.mLlSpecial.setVisibility(View.GONE);
        } else {
            mViewHolder.mLlSpecial.setVisibility(View.VISIBLE);
        }
        if (mSpecialAdapter == null) {
            mSpecialAdapter = new SpecialAdapter(mContext, mSpecialModels);
            mViewHolder.mLvSpecial.setAdapter(mSpecialAdapter);
        } else {
            mSpecialAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    /**
     * 重要通知的轮播线程
     */
    private class ImportantInfoThread extends Thread {
        int count = 0;

        @Override
        public void run() {
            try {
                while (true) {
                    sleep(5000);
                    count++;
                    if (count % 2 == 0) {
                        mImportantInfoList.clear();
                        mImportantInfoList.addAll(cache1);
                    } else {
                        if (!ListUtils.isEmpty(cache2)) {
                            mImportantInfoList.clear();
                            mImportantInfoList.addAll(cache2);
                        }
                    }
                }

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() { //更新UI 必须通知主线程更新
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_IMPORTANT_INFO) {
                mImportantInfoAdapter.notifyDataSetChanged();
            } else if (msg.what == UPDATE_ViewPagerHeight) {
                if (GroupVarManager.getInstance().mViewPagerMap.get(GroupVarManager.getInstance().dryCargoPosition) != null) {
//                    mViewHolder.mViewPager.setLayoutParams(GroupVarManager.getInstance().mViewPagerMap.get(GroupVarManager.getInstance().dryCargoPosition));
                }
            }
        }
    };

    class ViewHolder {
        @BindView(R.id.vp_banners)
        ViewPager mVpBanners;
        @BindView(R.id.vp_viewpagerindicator)
        MyViewPagerIndicator mVpViewpagerindicator;
        @BindView(R.id.tv_adv)
        TextView mTvAdv;
        @BindView(R.id.layout_adv)
        FrameLayout mLayoutAdv;
        //        @BindView(R.id.tabLayout)
//        TabLayout mTabLayout;
//        @BindView(R.id.viewPager)
//        ViewPager mViewPager;
        @BindView(R.id.lv_recommend)
        CustomListView mLvRecommend;
        @BindView(R.id.lv_direct_seeding)
        CustomListView mLvDirectSeeding;
        @BindView(R.id.tl_good)
        TabLayout mTlGood;
        @BindView(R.id.vp_good)
        ViewPager mVpGood;
        @BindView(R.id.lv_special)
        CustomListView mLvSpecial;
        @BindView(R.id.ll_good_class)
        LinearLayout mLlGoodClass;  //精品课程
        @BindView(R.id.ll_special_column)
        LinearLayout mLlSpecialColum;  //专栏
        @BindView(R.id.ll_direct_seeding)
        LinearLayout mLlLive;  //直播
        @BindView(R.id.ll_classify)
        LinearLayout mLlClssify;  //分类
        @BindView(R.id.ll_study_task)
        LinearLayout mLlStudyTask;
        @BindView(R.id.tv_good_class)
        TextView mTvGoodClass;
        @BindView(R.id.tv_special_column)
        TextView mTvSpecialColum;
        @BindView(R.id.tv_live)
        TextView mTvLive;
        @BindView(R.id.tv_classify)
        TextView mTvClassify;
        @BindView(R.id.ll_dry_cargo)
        LinearLayout mLlDryCargo;
        @BindView(R.id.tv_dry_cargo)
        TextView mTvDryCargo;
        @BindView(R.id.ll_focus)
        LinearLayout mLlFocus;
        @BindView(R.id.tv_focus)
        TextView mTvFocus;
        @BindView(R.id.ll_live_room)
        LinearLayout mLlLiveRoom;
        @BindView(R.id.tv_live_room)
        TextView mTvLiveRoom;
        @BindView(R.id.ll_course)
        LinearLayout mLlCourse;
        @BindView(R.id.tv_course)
        TextView mTvCourse;
        @BindView(R.id.tv_study_task)
        TextView mTvStudyTask;
        @BindView(R.id.lv_foucs_colum)
        CustomListView mLvFocusColum;
        @BindView(R.id.watertext)
        TextSwitcherView mTextSwitcherView;
        @BindView(R.id.lv_audio)
        CustomListView mLvAudio;
        @BindView(R.id.ll_special)
        LinearLayout mLlSpecial;
        @BindView(R.id.tv_special)
        TextView mTvSpecial;

        @OnClick({R.id.tv_important_info_more, R.id.ll_good_class, R.id.ll_special_column, R.id.ll_direct_seeding, R.id.ll_classify, R.id.tv_dry_cargo_more,
                R.id.tv_focus_change, R.id.tv_live_more, R.id.tv_course_change, R.id.tv_course_more, R.id.tv_special_change, R.id.tv_special_more, R.id.ll_study_task})
        public void onViewClicked(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.tv_important_info_more: //更多通知
                    intent = new Intent(mContext, NoticeActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_good_class: //精品课程导航
                    intent = new Intent(mContext, NaviResultActivity.class);
                    intent.putExtra("search", AppConstants.TAG_COURSE);
                    intent.putExtra("is_more", "1");
                    intent.putExtra("type", 1);
                    intent.putExtra("title", mTvGoodClass.getText().toString());
                    GroupVarManager.getInstance().path_1 = AppConstants.TAG_COURSE_NAVI;
                    startActivity(intent);
                    break;
                case R.id.ll_special_column: //导航专栏
                    intent = new Intent(mContext, NaviResultActivity.class);
                    intent.putExtra("search", AppConstants.TAG_COLUMN);
                    intent.putExtra("is_more", "1");
                    intent.putExtra("type", 1);
                    intent.putExtra("title", mTvSpecial.getText().toString());
                    GroupVarManager.getInstance().path_1 = AppConstants.TAG_COLUMN_NAVI;
                    startActivity(intent);
                    break;
                case R.id.ll_direct_seeding: //直播
                    intent = new Intent(mContext, NaviResultActivity.class);
                    intent.putExtra("search", AppConstants.TAG_LIVE);
                    intent.putExtra("is_more", "1");
                    intent.putExtra("type", 1);
                    intent.putExtra("title", mTvLive.getText().toString());
                    GroupVarManager.getInstance().path_1 = AppConstants.TAG_LIVE_NAVI;
                    startActivity(intent);
                    break;
                case R.id.ll_classify:      //内容导航
                    intent = new Intent(mContext, HomeNaviActivity.class);
                    intent.putExtra("title", mTvClassify.getText().toString());
                    startActivity(intent);
                    break;
                case R.id.tv_dry_cargo_more:  //更多音频
                    intent = new Intent(mContext, NaviResultActivity.class);
                    intent.putExtra("search", AppConstants.TAG_AUDIO);
                    intent.putExtra("is_more", "1");
                    intent.putExtra("type", 1);
                    intent.putExtra("title", mViewHolder.mTvDryCargo.getText().toString());
                    GroupVarManager.getInstance().path_1 = AppConstants.TAG_AUDIO_NAVI;
                    startActivity(intent);
                    break;
                case R.id.tv_focus_change:   //每日推荐换一换
                    getFocusLoop();
                    break;
                case R.id.tv_live_more:      //更多直播
                    intent = new Intent(mContext, NaviResultActivity.class);
                    intent.putExtra("search", AppConstants.TAG_LIVE);
                    intent.putExtra("is_more", "1");
                    intent.putExtra("type", 1);
                    intent.putExtra("title", mViewHolder.mTvLiveRoom.getText().toString());
                    GroupVarManager.getInstance().path_1 = AppConstants.TAG_LIVE_NAVI;
                    startActivity(intent);
                    break;
                case R.id.tv_course_change:  //精品课程换一换
                    getCourseCatLoop();
                    break;
                case R.id.tv_course_more:   //更多精品课程
                    intent = new Intent(mContext, NaviResultActivity.class);
                    intent.putExtra("search", AppConstants.TAG_COURSE);
                    intent.putExtra("is_more", "1");
                    intent.putExtra("type", 1);
                    intent.putExtra("title", mViewHolder.mTvCourse.getText().toString());
                    GroupVarManager.getInstance().path_1 = AppConstants.TAG_COURSE_NAVI;
                    startActivity(intent);
                    break;
                case R.id.tv_special_change: //专栏换一换
                    getColumnLoop();
                    break;
                case R.id.tv_special_more:  //更多专栏
                    intent = new Intent(mContext, NaviResultActivity.class);
                    intent.putExtra("search", AppConstants.TAG_COLUMN);
                    intent.putExtra("is_more", "1");
                    intent.putExtra("type", 1);
                    intent.putExtra("title", mViewHolder.mTvSpecial.getText().toString());
                    GroupVarManager.getInstance().path_1 = AppConstants.TAG_COLUMN_NAVI;
                    startActivity(intent);
                    break;
                case R.id.ll_study_task:  //学习任务
                    intent = new Intent(mContext, StudyTaskActivity.class);
                    startActivity(intent);
                    break;
                default:
                    break;
            }
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_home, null);
        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    /**
     * 每日推荐换衣换
     */
    private void getFocusLoop() {
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_HOME_FOCUS_LOOP, null, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<FocusModel> result = GsonUtils.json2Bean(s, FocusModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    if (result.getData().getFocus_type().equals(AppConstants.TAG_COURSE)) {
                        mViewHolder.mLvRecommend.setVisibility(View.VISIBLE);
                        mViewHolder.mLvFocusColum.setVisibility(View.GONE);
                        mRecommendTodayModels.clear();
                        mRecommendTodayModels.add(result.getData());
                        if (mRecommendTodayAdapter == null) {
                            mRecommendTodayAdapter = new RecommendTodayAdapter(mContext, mRecommendTodayModels);
                            mViewHolder.mLvRecommend.setAdapter(mRecommendTodayAdapter);
                        } else {
                            mRecommendTodayAdapter.notifyDataSetChanged();
                        }
                    } else {
                        mViewHolder.mLvRecommend.setVisibility(View.GONE);
                        mViewHolder.mLvFocusColum.setVisibility(View.VISIBLE);
                        mColumnContentModels.clear();
                        mColumnContentModels.add(result.getData());
                        if (mColumnTodayAdapter == null) {
                            mColumnTodayAdapter = new ColumnTodayAdapter(mContext, mColumnContentModels);
                            mViewHolder.mLvFocusColum.setAdapter(mColumnTodayAdapter);
                        } else {
                            mColumnTodayAdapter.notifyDataSetChanged();
                        }
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
     * 精品课程主题下换一换
     */
    private void getCourseCatLoop() {
        Map<String, Object> params = new HashMap<>();
        params.put("top_cat_id", top_cat_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_HOME_COURSE_CAT_LOOP, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<CourseListModel> result = GsonUtils.json2List(s, CourseListModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    EventBus.getDefault().post(result.getData());
                } else {
                    if (result != null) {
                        ToastUtils.show(mContext, result.getMessage());
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
     * 专栏换一换
     */
    private void getColumnLoop() {
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_HOME_COLUMN_LOOP, null, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<ColumnContentModel> result = GsonUtils.json2List(s, ColumnContentModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    mSpecialModels.clear();
                    mSpecialModels.addAll(result.getData());
                    if (mSpecialAdapter == null) {
                        mSpecialAdapter = new SpecialAdapter(mContext, mSpecialModels);
                        mViewHolder.mLvSpecial.setAdapter(mSpecialAdapter);
                    } else {
                        mSpecialAdapter.notifyDataSetChanged();
                    }
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage() : "数据解析错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 首页要显示那些Ui
     */
    private void showItem() {
        //显示隐藏分类按钮
        for (int i = 0; i < AppUserModel.getInstance().getAll_block().size(); i++) {
            if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals("home_navi_course")) {
                if (AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("1")) {
                    mViewHolder.mLlGoodClass.setVisibility(View.GONE);
                } else {
                    mViewHolder.mLlGoodClass.setVisibility(View.VISIBLE);
                    mViewHolder.mTvGoodClass.setText(AppUserModel.getInstance().getAll_block().get(i).getBlock_name());
                }
            } else if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals("home_navi_column")) {
                if (AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("1")) {
                    mViewHolder.mLlSpecial.setVisibility(View.GONE);
                } else {
                    mViewHolder.mLlSpecial.setVisibility(View.VISIBLE);
                    mViewHolder.mTvSpecial.setText(AppUserModel.getInstance().getAll_block().get(i).getBlock_name());
                }
            } else if ((AppUserModel.getInstance().getAll_block().get(i).getLabel()).equals("home_navi_live")) {
                if (AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("1")) {
                    mViewHolder.mLlLive.setVisibility(View.GONE);
                } else {
                    mViewHolder.mLlLive.setVisibility(View.VISIBLE);
                    mViewHolder.mTvLive.setText(AppUserModel.getInstance().getAll_block().get(i).getBlock_name());
                }
            } else if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals("home_navi_content")) {
                if (AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("1")) {
                    mViewHolder.mLlClssify.setVisibility(View.GONE);
                } else {
                    mViewHolder.mLlClssify.setVisibility(View.VISIBLE);
                    mViewHolder.mTvClassify.setText(AppUserModel.getInstance().getAll_block().get(i).getBlock_name());
                }
            } else if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals("audio")) {
                if (AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("1")) {
                    mViewHolder.mLlDryCargo.setVisibility(View.GONE);
                } else {
                    mViewHolder.mLlDryCargo.setVisibility(View.VISIBLE);
                    mViewHolder.mTvDryCargo.setText(AppUserModel.getInstance().getAll_block().get(i).getBlock_name());
                }
            } else if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals("focus")) {
                if (AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("1")) {
                    mViewHolder.mLlFocus.setVisibility(View.GONE);
                } else {
                    mViewHolder.mLlFocus.setVisibility(View.VISIBLE);
                    mViewHolder.mTvFocus.setText(AppUserModel.getInstance().getAll_block().get(i).getBlock_name());
                }
            } else if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals("live")) {
                if (AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("1")) {
                    mViewHolder.mLlLiveRoom.setVisibility(View.GONE);
                } else {
                    mViewHolder.mLlLiveRoom.setVisibility(View.VISIBLE);
                    mViewHolder.mTvLiveRoom.setText(AppUserModel.getInstance().getAll_block().get(i).getBlock_name());
                }
            } else if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals("course")) {
                if (AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("1")) {
                    mViewHolder.mLlCourse.setVisibility(View.GONE);
                } else {
                    mViewHolder.mLlCourse.setVisibility(View.VISIBLE);
                    mViewHolder.mTvCourse.setText(AppUserModel.getInstance().getAll_block().get(i).getBlock_name());
                }
            } else if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals("home_navi_learn")) {
                if (AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("1")) {
                    mViewHolder.mLlStudyTask.setVisibility(View.GONE);
                } else {
                    mViewHolder.mLlStudyTask.setVisibility(View.VISIBLE);
                    mViewHolder.mTvStudyTask.setText(AppUserModel.getInstance().getAll_block().get(i).getBlock_name());
                }
            }
        }
    }
}
