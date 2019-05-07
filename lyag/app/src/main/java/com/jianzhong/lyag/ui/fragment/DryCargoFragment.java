package com.jianzhong.lyag.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.widget.CustomListView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.DryCargoAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.ContentDetailModel;

import org.greenrobot.eventbus.EventBus;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 每日音频列表
 * Created by zhengwencheng on 2018/1/17 0017.
 * package com.jianzhong.bs.ui.fragment
 */

public class DryCargoFragment extends BaseFragment {

    @BindView(R.id.lv_dry_cargo)
    CustomListView mLvDryCargo;
    private int position;
    List<ContentDetailModel> mList = new ArrayList<>();
    private DryCargoAdapter mDryCargoAdapter;
    private String cat_id;
    public static DryCargoFragment getInstance(List<ContentDetailModel> mDryCargoList, int position,String cat_id) {
        DryCargoFragment fragment = new DryCargoFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mDryCargoList", (Serializable) mDryCargoList);
        bundle.putInt("position", position);
        bundle.putString("cat_id", cat_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_dry_cargo, null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();

        cat_id = getArguments().getString("cat_id");
        position = getArguments().getInt("position", 0);
        List<ContentDetailModel> mDryCargoList = (List<ContentDetailModel>) getArguments().get("mDryCargoList");
        if (!ListUtils.isEmpty(mDryCargoList)) {
            mList.addAll(mDryCargoList);
            mDryCargoAdapter = new DryCargoAdapter(mContext, mList);
            mLvDryCargo.setAdapter(mDryCargoAdapter);

            GroupVarManager.getInstance().mViewPagerMap.put(position, getListViewParams());
            if (position == 0) { //第一次初始化的时候用EventBus先传 不用单例是怕线程不同步
                EventBus.getDefault().post(AppConstants.EVENT_DRY_CARGO);
            }
        }else {
            getDryCargo();
        }

    }


    /**
     * 每日音频主题切换
     */
    private void getDryCargo(){
        Map<String,Object> params = new HashMap<>();
        params.put("cat_id",cat_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_AUDIO_CAT_TAG, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<ContentDetailModel> result = GsonUtils.json2List(s,ContentDetailModel.class);
                if(result != null && result.getCode() == HttpConfig.STATUS_SUCCESS){
                    if(!ListUtils.isEmpty(result.getData())){
                        mList.addAll(result.getData());
                        mDryCargoAdapter = new DryCargoAdapter(mContext, mList);
                        mLvDryCargo.setAdapter(mDryCargoAdapter);
                        GroupVarManager.getInstance().mViewPagerMap.put(position, getListViewParams());
                    }
                }
            }
            @Override
            public void onFailure(String msg) {

            }
        });
    }

    /**
     * 获取Listview的高度 给首页的viewpager重新设置高度
     *
     * @return
     */
    private ViewGroup.LayoutParams getListViewParams() {
        //通过ListView获取其中的适配器adapter
        ListAdapter listAdapter = mLvDryCargo.getAdapter();
        //声明默认高度为0
        int totalHeight = 0;
        //遍历listView中所有Item，累加所有item的高度就是ListView的实际高度（后面会考虑分割线的高度）
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View item = listAdapter.getView(i, null, mLvDryCargo);
            item.measure(0, 0);
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams lp = mLvDryCargo.getLayoutParams();
        lp.height = totalHeight + (mLvDryCargo.getDividerHeight() * (listAdapter.getCount() - 1));

        return lp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

}
