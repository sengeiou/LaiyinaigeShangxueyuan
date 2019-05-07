package com.jianzhong.lyag.ui.exam;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RadioButton;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.FragmentViewPageAdapter;
import com.jianzhong.lyag.base.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 已完成
 * create by zhengwencheng on 2018/3/20 0020
 * package com.jianzhong.bs.ui.exam
 */
public class StudyFinishFragment extends BaseFragment {

    @BindView(R.id.rb_post_major)
    RadioButton mRbMajor;
    @BindView(R.id.rb_leader_assign)
    RadioButton mRbAssign;
    @BindView(R.id.viewPager)
    ViewPager mViewpager;

    private FragmentViewPageAdapter mFragmentViewPageAdapter;
    public static StudyFinishFragment newInstance() {

        Bundle args = new Bundle();

        StudyFinishFragment fragment = new StudyFinishFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_study_finish,null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();

        initViewPager();
    }


    private void initViewPager(){
        //装载右侧
        List<Fragment> mFragments = new ArrayList<>();
        mFragments.add(PostMajorFragment.newInstance(1));
        mFragments.add(LeaderAssignFragment.newInstance(1));
        //ViewPager设置适配器
        mFragmentViewPageAdapter = new FragmentViewPageAdapter(getChildFragmentManager(), mFragments);
        mViewpager.setAdapter(mFragmentViewPageAdapter);
        mViewpager.setCurrentItem(0,false);
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
//                mViewpager.setCurrentItem(position);
                if(position == 0){
                    mRbMajor.setChecked(true);
                    mRbAssign.setChecked(false);
                    mRbMajor.setBackground(mContext.getResources().getDrawable(R.drawable.selector_study));
                    mRbAssign.setBackground(null);
                }else {
                    mRbAssign.setChecked(true);
                    mRbMajor.setChecked(false);
                    mRbAssign.setBackground(mContext.getResources().getDrawable(R.drawable.selector_study));
                    mRbMajor.setBackground(null);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.rb_post_major, R.id.rb_leader_assign})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.rb_post_major:
                mRbMajor.setChecked(true);
                mRbAssign.setChecked(false);
                mRbMajor.setBackground(mContext.getResources().getDrawable(R.drawable.selector_study));
                mRbAssign.setBackground(null);
                mViewpager.setCurrentItem(0);
                break;
            case R.id.rb_leader_assign:
                mRbAssign.setChecked(true);
                mRbMajor.setChecked(false);
                mRbAssign.setBackground(mContext.getResources().getDrawable(R.drawable.selector_study));
                mRbMajor.setBackground(null);
                mViewpager.setCurrentItem(1);
                break;
        }
    }


}
