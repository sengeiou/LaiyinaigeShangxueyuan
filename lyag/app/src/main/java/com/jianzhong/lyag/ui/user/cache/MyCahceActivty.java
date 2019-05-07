package com.jianzhong.lyag.ui.user.cache;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.AbFragmentPagerAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.widget.customview.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 我的缓存
 * Created by zhengwencheng on 2018/4/8 0008.
 * package com.jianzhong.bs.ui.user.cache
 */

public class MyCahceActivty extends ToolbarActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    NoScrollViewPager mViewPager;

    private String[] mTabTitle = new String[]{"视频", "音频"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_my_cache);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void resetCahceEdit(String tag) {
        if(tag.equals(AppConstants.TAG_RESET_CACHE_EDIT)){
            GroupVarManager.getInstance().isEditCache = 0;
            setHeadRight("编辑");
//            EventBus.getDefault().post(AppConstants.TAG_CAHCE_EDIT);
            mViewPager.setNoScroll(false);
            //恢复点击
            LinearLayout tabStrip = (LinearLayout) mTabLayout.getChildAt(0);
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                View tabView = tabStrip.getChildAt(i);
                if (tabView != null) {
                    tabView.setClickable(true);
                }
            }
        }

    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("我的缓存");
        showHeadTitle();
        setHeadRight("编辑");
        showHeadRight();

        initViewPager();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        mFragments.add(ClassCacheFragment.newInstance());
        mFragments.add(AudioCacheFragment.newInstance());
        AbFragmentPagerAdapter mAbFragmentPagerAdapter = new AbFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTabTitle);
        mViewPager.setAdapter(mAbFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        //
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick(R.id.head_right)
    public void onViewClicked() {
        if (GroupVarManager.getInstance().isEditCache == 0) {
            GroupVarManager.getInstance().isEditCache = 1;
            GroupVarManager.getInstance().isAll = 1;
            GroupVarManager.getInstance().mCacheList.clear();
            setHeadRight("取消");
            EventBus.getDefault().post(AppConstants.TAG_CAHCE_EDIT);
            mViewPager.setNoScroll(true);
            //禁止Tablayout的点击
            LinearLayout tabStrip = (LinearLayout) mTabLayout.getChildAt(0);
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                View tabView = tabStrip.getChildAt(i);
                if (tabView != null) {
                    tabView.setClickable(false);
                }
            }
        } else {
            GroupVarManager.getInstance().isEditCache = 0;
            setHeadRight("编辑");
            GroupVarManager.getInstance().mCacheList.clear();
            EventBus.getDefault().post(AppConstants.TAG_CAHCE_EDIT);
            mViewPager.setNoScroll(false);
            //恢复点击
            LinearLayout tabStrip = (LinearLayout) mTabLayout.getChildAt(0);
            for (int i = 0; i < tabStrip.getChildCount(); i++) {
                View tabView = tabStrip.getChildAt(i);
                if (tabView != null) {
                    tabView.setClickable(true);
                }
            }
        }
    }
}
