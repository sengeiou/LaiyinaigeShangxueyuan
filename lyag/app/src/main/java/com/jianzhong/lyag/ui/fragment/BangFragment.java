package com.jianzhong.lyag.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 帮派Fragment
 * Created by zhengwencheng on 2018/1/15 0015.
 * package com.jianzhong.bs.ui.fragment
 */

public class BangFragment extends BaseFragment {

    @BindView(R.id.head_ll_back)
    LinearLayout mHeadLlBack;
    @BindView(R.id.head_title)
    TextView mHeadTitle;


    public static BangFragment getInstance() {
        BangFragment fragment = new BangFragment();
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_bang, null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();

        mHeadLlBack.setVisibility(View.GONE);
        mHeadTitle.setText("帮派");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        unbinder = ButterKnife.bind(this, rootView);
        return rootView;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
