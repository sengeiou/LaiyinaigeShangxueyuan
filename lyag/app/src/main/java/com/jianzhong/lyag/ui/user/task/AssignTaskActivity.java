package com.jianzhong.lyag.ui.user.task;

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
 * 我发出的学习任务
 * create by zhengwencheng on 2018/3/21 0021
 * package com.jianzhong.bs.ui.user.task
 */
public class AssignTaskActivity extends ToolbarActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private String[] mTabTitle = new String[]{"未完成","已完成"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_assign_task);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("发出的学习任务");
        showHeadTitle();

        initViewPager();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        mFragments.add(AssignTaskFragment.newInstance(0));
        mFragments.add(AssignTaskFragment.newInstance(1));
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


}
