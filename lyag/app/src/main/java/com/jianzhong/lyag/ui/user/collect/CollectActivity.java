package com.jianzhong.lyag.ui.user.collect;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.LinearLayout;
import com.baselib.util.ListUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.AbFragmentPagerAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.BlockModel;
import com.jianzhong.lyag.widget.customview.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 收藏首页
 * Created by zhengwencheng on 2018/3/9 0009.
 * package com.jianzhong.bs.ui.user.collect
 */

public class CollectActivity extends ToolbarActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    NoScrollViewPager mViewPager;
    private List<BlockModel> mBlockModels = new ArrayList<>();
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private AbFragmentPagerAdapter mAbFragmentPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_collect);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void resetCollect(String tag) {
        if(tag.equals(AppConstants.TAG_RESET_COLLECT)){
            GroupVarManager.getInstance().isEditCollect = 0;
            setHeadRight("编辑");
            EventBus.getDefault().post(AppConstants.TAG_EDIT_COLLECT);
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


        setHeadTitle("我的收藏");
        showHeadTitle();
        setHeadRight("编辑");
        showHeadRight();

        GroupVarManager.getInstance().isEditCollect = 0;
        initViewPager();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        if (ListUtils.isEmpty(AppUserModel.getInstance().getAll_block()))
            return;
        for (int i = 0; i < AppUserModel.getInstance().getAll_block().size(); i++) {
            if (AppUserModel.getInstance().getAll_block().get(i).getLabel().contains(AppConstants.TAG_FAVOR) && (AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("0"))) {
                mBlockModels.add(AppUserModel.getInstance().getAll_block().get(i));
            }
        }

        String[] tabTitle = new String[mBlockModels.size()];
        for (int i = 0; i < mBlockModels.size(); i++) {
            tabTitle[i] = mBlockModels.get(i).getBlock_name();
            isContainLabel(mBlockModels.get(i).getLabel());
        }

        mAbFragmentPagerAdapter = new AbFragmentPagerAdapter(getSupportFragmentManager(), mFragments, tabTitle);
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

    /**
     * @param label
     * @return
     */
    private void isContainLabel(String label) {
        switch (label) {
            case AppConstants.TAG_FAVOR_COURSE:
                mFragments.add(CourseFragment.newInstance());
                break;
            case AppConstants.TAG_FAVOR_COLUMN:
                mFragments.add(ColumnFragment.newInstance());
                break;
            case AppConstants.TAG_FAVOR_LIVE:
                mFragments.add(LiveFragment.newInstance());
                break;
            case AppConstants.TAG_FAVOR_AUDIO:
                mFragments.add(DryCargoFragment.newInstance());
                break;
            case AppConstants.TAG_FAVOR_NOTICE:
                mFragments.add(NoticeFragment.newInstance());
                break;
            case AppConstants.TAG_FAVOR_HELP:
                mFragments.add(SeekHelpFragment.newInstance());
                break;
            case AppConstants.TAG_FAVOR_SHARE:
                mFragments.add(ShareFragment.newInstance());
                break;
        }
    }

    @OnClick(R.id.head_right)
    public void onViewClicked() { //编辑
        GroupVarManager.getInstance().mCollectList.clear();
        if (GroupVarManager.getInstance().isEditCollect == 0) {
            GroupVarManager.getInstance().isEditCollect = 1;
            setHeadRight("取消");
            EventBus.getDefault().post(AppConstants.TAG_EDIT_COLLECT);
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
            GroupVarManager.getInstance().isEditCollect = 0;
            setHeadRight("编辑");
            EventBus.getDefault().post(AppConstants.TAG_EDIT_COLLECT);
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
