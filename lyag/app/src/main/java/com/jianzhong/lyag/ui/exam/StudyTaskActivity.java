package com.jianzhong.lyag.ui.exam;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.AbFragmentPagerAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 学习任务列表
 * create by zhengwencheng on 2018/3/20 0020
 * package com.jianzhong.bs.ui.exam
 */
public class StudyTaskActivity extends ToolbarActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    private String[] mTabTitle = new String[]{"岗位必修","领导指派","已完成"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private AbFragmentPagerAdapter mAbFragmentPagerAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_study_task);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("学习任务");
        showHeadTitle();

        initViewPager();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        mFragments.add(PostMajorFragment.newInstance(0));
        mFragments.add(LeaderAssignFragment.newInstance(0));
        mFragments.add(StudyFinishFragment.newInstance());
        mAbFragmentPagerAdapter = new AbFragmentPagerAdapter(getSupportFragmentManager(), mFragments, mTabTitle);
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
}
