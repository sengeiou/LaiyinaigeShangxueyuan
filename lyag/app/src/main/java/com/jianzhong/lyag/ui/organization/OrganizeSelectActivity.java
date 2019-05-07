package com.jianzhong.lyag.ui.organization;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.baselib.util.ActivityManager;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.FragmentViewPageAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.ui.fragment.AgencySelectFragment;
import com.jianzhong.lyag.ui.fragment.CompanySelectFragment;
import com.jianzhong.lyag.util.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 按组织架构选择首页
 * Created by zhengwencheng on 2018/1/19 0019.
 * package com.jianzhong.bs.ui.organization
 */

public class OrganizeSelectActivity extends ToolbarActivity {

    @BindView(R.id.rb_company)
    RadioButton mRbCompany;
    @BindView(R.id.rb_agency)
    RadioButton mRbAgency;
    @BindView(R.id.radio_group)
    RadioGroup mRadioGroup;
    @BindView(R.id.viewpager)
    ViewPager mViewpager;
    @BindView(R.id.tv_range)
    TextView mTvRange;
    @BindView(R.id.tv_search)
    TextView mTvSearch;

    private FragmentViewPageAdapter mOrganizePageAdapter;
    private List<Fragment> mFragments = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_organize_select);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAssign(String tag) {
        if (tag.equals(AppConstants.TAG_UPDATE_ASSIGN)) {
            mTvRange.setText(CommonUtils.getAssignObject(GroupVarManager.getInstance().mDepartmentModels,
                    GroupVarManager.getInstance().mAnecyModels, GroupVarManager.getInstance().mPostModels, GroupVarManager.getInstance().mMemberModels));
        }
    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("按组织架构选择");
        showHeadTitle();

        mTvSearch.setText("搜索姓名");
        //
        initController();
    }

    private void initController() {
        mFragments.add(CompanySelectFragment.getInstance());
        mFragments.add(AgencySelectFragment.getInstance());
        //ViewPager设置适配器
        mOrganizePageAdapter = new FragmentViewPageAdapter(getSupportFragmentManager(), mFragments);
        mViewpager.setAdapter(mOrganizePageAdapter);
        mViewpager.setCurrentItem(0);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                mViewpager.setCurrentItem(position);
                if (position == 0) {
                    mRbCompany.setChecked(true);
                } else if (position == 1) {
                    mRbAgency.setChecked(true);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        mRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                if (mRbCompany.getId() == checkedId) {    //公司
                    mViewpager.setCurrentItem(0);
                } else if (mRbAgency.getId() == checkedId) { //经销商
                    mViewpager.setCurrentItem(1);
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvRange.setText(CommonUtils.getAssignObject(GroupVarManager.getInstance().mDepartmentModels,
                GroupVarManager.getInstance().mAnecyModels, GroupVarManager.getInstance().mPostModels, GroupVarManager.getInstance().mMemberModels));
    }

    @OnClick({R.id.head_right, R.id.ll_search,R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_right:
                OrganizeSelectActivity.this.finish();
                break;
            case R.id.ll_search:
                Intent intent = new Intent(mContext,MemberSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_confirm:
                ActivityManager.getActivityManager().finishActivity(OrganizeSelectActivity.class);
                ActivityManager.getActivityManager().finishActivity(MemberSelectActivity.class);
                break;
        }
    }
}
