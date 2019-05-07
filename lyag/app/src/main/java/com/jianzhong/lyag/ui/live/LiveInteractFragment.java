package com.jianzhong.lyag.ui.live;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.BaseFragment;

/**
 * 互动Fragment
 * Created by zhengwencheng on 2018/3/14 0014.
 * package com.jianzhong.bs.ui.live
 */

public class LiveInteractFragment extends BaseFragment {

    public static LiveInteractFragment newInstance() {

        Bundle args = new Bundle();
        LiveInteractFragment fragment = new LiveInteractFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_live_interact,null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();
    }
}
