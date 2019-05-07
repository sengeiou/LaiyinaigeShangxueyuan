package com.jianzhong.lyag.ui.user.studyrank;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.jianzhong.lyag.CommonWebActivity;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.AbFragmentPagerAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.listener.OnSelectListener;
import com.jianzhong.lyag.model.RankScopeModel;
import com.jianzhong.lyag.util.PopWindowUtil;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 学习排名首页
 * create by zhengwencheng on 2018/3/21 0021
 * package com.jianzhong.bs.ui.interact
 */
public class StudyRankActivity extends ToolbarActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.rl_toolbar)
    RelativeLayout mRlToolbar;
    @BindView(R.id.head_title)
    TextView mHeadTitle;
    private String[] mTabTitle = new String[]{"全部排名","同岗位排名","团队内排名"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();

    private List<RankScopeModel> mList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_rank);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("本周学习排名");
        showHeadTitle();
        setHeadRight("说明");
        showHeadRight();

        Drawable drawable= mContext.getResources().getDrawable(R.drawable.hd_drb_menu);
        // 这一步必须要做,否则不会显示.
        drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
        mHeadTitle.setCompoundDrawables(null,null,drawable,null);

        initRankScope();

        initViewPager();
    }

    private void initRankScope(){
        for (int i = 0; i < 4; i++) {
            RankScopeModel item = new RankScopeModel();
            if(i == 0){
                item.setKey("week");
                item.setName("本周学习排名");
                item.setSelected(true);
            }else if(i == 1){
                item.setKey("month");
                item.setName("本月学习排名");
            }else if(i == 2){
                item.setKey("year");
                item.setName("本年学习排名");
            }else {
                item.setKey("all");
                item.setName("所有学习排名");
            }
            mList.add(item);
        }
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        mFragments.add(StudyRankFragment.newInstance("all"));
        mFragments.add(StudyRankFragment.newInstance("pos"));
        mFragments.add(StudyRankFragment.newInstance("branch"));
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

    @OnClick({R.id.head_title, R.id.head_right})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.head_title:
                PopWindowUtil.getInstance().showRankScope(StudyRankActivity.this, mRlToolbar, mList, new OnSelectListener() {
                    @Override
                    public void selectItem(RankScopeModel item) {
                        mHeadTitle.setText(item.getName());
                        EventBus.getDefault().post(item);
                    }
                });
                break;
            case R.id.head_right:
                intent = new Intent(mContext, CommonWebActivity.class);
                intent.putExtra("title","学分说明");
                intent.putExtra("url", HttpConfig.WEB_URL_BASE + HttpConfig.URL_POINT_RULE);
                startActivity(intent);
                break;
        }
    }
}
