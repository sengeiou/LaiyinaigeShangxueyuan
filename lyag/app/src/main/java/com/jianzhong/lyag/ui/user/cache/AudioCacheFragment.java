package com.jianzhong.lyag.ui.user.cache;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.FragmentViewPageAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.widget.customview.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 音频缓存Fragment
 * <p>
 * Created by zhengwencheng on 2018/4/9 0009.
 * package com.jianzhong.bs.ui.user.cache
 */

public class AudioCacheFragment extends BaseFragment {

    @BindView(R.id.rb_had_cache)
    RadioButton mRbHadCache;
    @BindView(R.id.rb_cache_now)
    RadioButton mRbCacheNow;
    @BindView(R.id.viewPager)
    NoScrollViewPager mViewPager;

    public static AudioCacheFragment newInstance() {

        Bundle args = new Bundle();
        AudioCacheFragment fragment = new AudioCacheFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_audio_cache, null);
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
    public void resetCahceEdit(String tag) {
        if (tag.equals(AppConstants.TAG_CAHCE_EDIT)) {
            if (GroupVarManager.getInstance().isEditCache == 1) {
                mRbCacheNow.setEnabled(false);
                mRbHadCache.setEnabled(false);
                mViewPager.setNoScroll(true);
            }else {
                mRbCacheNow.setEnabled(true);
                mRbHadCache.setEnabled(true);
                mViewPager.setNoScroll(false);
            }
        }
    }

    @Override
    protected void initData() {
        super.initData();

        initViewPager();

    }


    private void initViewPager() {
        //装载右侧
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(AudioCacheFinishFragment.newInstance());
        mFragments.add(AudioCacheNowFragment.newInstance());
        //ViewPager设置适配器
        FragmentViewPageAdapter mFragmentViewPageAdapter = new FragmentViewPageAdapter(getChildFragmentManager(), mFragments);
        mViewPager.setAdapter(mFragmentViewPageAdapter);
        mViewPager.setCurrentItem(0, false);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mViewpager.setCurrentItem(position);
                if (position == 0) {
                    mRbHadCache.setChecked(true);
                    mRbCacheNow.setChecked(false);
                    mRbHadCache.setBackground(mContext.getResources().getDrawable(R.drawable.selector_study));
                    mRbCacheNow.setBackground(null);
                } else {
                    mRbCacheNow.setChecked(true);
                    mRbHadCache.setChecked(false);
                    mRbCacheNow.setBackground(mContext.getResources().getDrawable(R.drawable.selector_study));
                    mRbHadCache.setBackground(null);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.rb_had_cache, R.id.rb_cache_now})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_had_cache:     //已完成
                mRbHadCache.setChecked(true);
                mRbCacheNow.setChecked(false);
                mRbHadCache.setBackground(mContext.getResources().getDrawable(R.drawable.selector_study));
                mRbCacheNow.setBackground(null);
                mViewPager.setCurrentItem(0);
                break;
            case R.id.rb_cache_now:    //正在缓存
                mRbCacheNow.setChecked(true);
                mRbHadCache.setChecked(false);
                mRbCacheNow.setBackground(mContext.getResources().getDrawable(R.drawable.selector_study));
                mRbHadCache.setBackground(null);
                mViewPager.setCurrentItem(1);
                break;
        }
    }

}
