package com.jianzhong.lyag.ui.interact;

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
 * 达人榜
 * create by zhengwencheng on 2018/3/21 0021
 * package com.jianzhong.bs.ui.interact
 */
public class RankActivity extends ToolbarActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.rl_toolbar)
    RelativeLayout mRlToolbar;
    @BindView(R.id.head_title)
    TextView mHeadTitle;
    private String[] mTabTitle = new String[]{"提问榜","采纳榜","分享榜"};
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

        setHeadTitle("本周互动排名");
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
                item.setName("本周互动排名");
                item.setSelected(true);
            }else if(i == 1){
                item.setKey("month");
                item.setName("本月互动排名");
            }else if(i == 2){
                item.setKey("year");
                item.setName("本年互动排名");
            }else {
                item.setKey("all");
                item.setName("全部互动排名");
            }

            mList.add(item);
        }
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        mFragments.add(RankFragment.newInstance("quiz"));
        mFragments.add(RankFragment.newInstance("adopt"));
        mFragments.add(RankFragment.newInstance("share"));
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
                PopWindowUtil.getInstance().showRankScope(RankActivity.this, mRlToolbar, mList, new OnSelectListener() {
                    @Override
                    public void selectItem(RankScopeModel item) {
                        mHeadTitle.setText(item.getName());
                        EventBus.getDefault().post(item);
                    }
                });
                break;
            case R.id.head_right:
                intent = new Intent(mContext, CommonWebActivity.class);
                intent.putExtra("title","达人榜说明");
                intent.putExtra("url", HttpConfig.WEB_URL_BASE + HttpConfig.URL_INTERACT_RULE);
                startActivity(intent);
                break;
        }
    }
}
