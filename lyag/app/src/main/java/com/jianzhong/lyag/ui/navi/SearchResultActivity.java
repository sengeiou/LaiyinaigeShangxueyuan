package com.jianzhong.lyag.ui.navi;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.AbFragmentPagerAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索结果列表
 * Created by zhengwencheng on 2018/3/27 0027.
 * package com.jianzhong.bs.ui.navi
 */
public class SearchResultActivity extends ToolbarActivity {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;

    private String[] mTabTitle = new String[]{"发现", "课程", "专栏", "直播", "音频"};
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private AbFragmentPagerAdapter mAbFragmentPagerAdapter;

    private String keyWord;
    private int curPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_search_result);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchSearch(String tag) {
        if (tag.equals(AppConstants.EVENT_SWITCH_SEARCH)) {
            switch (GroupVarManager.getInstance().curSearch) {
                case 1:
                    mViewPager.setCurrentItem(1);
                    break;
                case 2:
                    mViewPager.setCurrentItem(2);
                    break;
                case 3:
                    mViewPager.setCurrentItem(3);
                    break;
                case 4:
                    mViewPager.setCurrentItem(4);
                    break;
                default:
                    break;
            }
        }
    }

    @Override
    public void initData() {
        super.initData();

        keyWord = getIntent().getStringExtra("keyWord");
        initViewPager();

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(SearchResultActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEtClassSearch的非空判断
                    if ((mEtSearch.getText().toString()).isEmpty()) {
                        ToastUtils.show(mContext, "请输入搜索关键字");
                    } else {
                        keyWord = mEtSearch.getText().toString();
                        GroupVarManager.getInstance().keyWord = keyWord;
                        if (curPosition == 0) {
                            EventBus.getDefault().post(AppConstants.TAG_DISCOVER);
                        } else if (curPosition == 1) {
                            EventBus.getDefault().post(AppConstants.TAG_COURSE);
                        } else if (curPosition == 2) {
                            EventBus.getDefault().post(AppConstants.TAG_COLUMN);
                        } else if (curPosition == 3) {
                            EventBus.getDefault().post(AppConstants.TAG_LIVE);
                        } else if (curPosition == 4) {
                            EventBus.getDefault().post(AppConstants.TAG_AUDIO);
                        }
                    }

                }
                return false;
            }
        });
    }

    @OnClick(R.id.tv_cancel)
    public void onViewClicked() {
        SearchResultActivity.this.finish();
    }

    /**
     * 初始化viewpager
     */
    private void initViewPager() {

        mFragments.add(DiscoverFragment.newInstance(keyWord));
        mFragments.add(CourseSearchFragment.newInstance(keyWord));
        mFragments.add(ColumnSearchFragment.newInstance(keyWord));
        mFragments.add(LiveSearchFragment.newInstance(keyWord));
        mFragments.add(AudioSearchFragment.newInstance(keyWord));
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
                curPosition = position;
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }
}
