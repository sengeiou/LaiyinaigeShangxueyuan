package com.jianzhong.lyag.ui.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.widget.CustomGridView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.GoodClassAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.CourseListModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;

/**
 * 精品课程Fragment
 * Created by zhengwencheng on 2018/1/17 0017.
 * package com.jianzhong.bs.ui.fragment
 */

public class GoodClassFragment extends BaseFragment {

    @BindView(R.id.gv_good_class)
    CustomGridView mGvGoodClass;
    private int position;
    private List<CourseListModel> mList = new ArrayList<>();
    private GoodClassAdapter mGoodClassAdapter;
    private String top_cat_id;
    public static GoodClassFragment getInstance(List<CourseListModel> mGoodClassModels, int position, String top_cat_id) {
        GoodClassFragment fragment = new GoodClassFragment();
        Bundle bundle = new Bundle();
        bundle.putSerializable("mGoodClassModels", (Serializable) mGoodClassModels);
        bundle.putInt("position",position);
        bundle.putString("top_cat_id",top_cat_id);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_good_class, null);
        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void initData() {
        super.initData();
        mGvGoodClass.setFocusable(false);
        top_cat_id = getArguments().getString("top_cat_id");
        position = getArguments().getInt("position",0);
        List<CourseListModel> mGoodClassModels = (List<CourseListModel>) getArguments().get("mGoodClassModels");
        if(!ListUtils.isEmpty(mGoodClassModels)){
            mList.addAll(mGoodClassModels);
            mGoodClassAdapter = new GoodClassAdapter(mContext, mList);
            mGvGoodClass.setAdapter(mGoodClassAdapter);
            GroupVarManager.getInstance().mGoodClassMap.put(position,getListViewParams());
            if(position == 0){ //第一次初始化的时候用EventBus先传 不用单例是怕线程不同步
                EventBus.getDefault().post(AppConstants.EVENT_GOOD_CLASS);
            }
        }else {
            getCourseCatTag();
        }

    }

    private void getCourseCatTag(){
        Map<String,Object> params = new HashMap<>();
        params.put("top_cat_id",top_cat_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COURSE_CAT_TAG, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<CourseListModel> result = GsonUtils.json2List(s,CourseListModel.class);
                if(result != null && result.getCode() == HttpConfig.STATUS_SUCCESS){
                    if(!ListUtils.isEmpty(result.getData())){
                        mList.addAll(result.getData());
                        mGoodClassAdapter = new GoodClassAdapter(mContext, mList);
                        mGvGoodClass.setAdapter(mGoodClassAdapter);
                        GroupVarManager.getInstance().mGoodClassMap.put(position,getListViewParams());
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateData(List<CourseListModel> models) {
        if(!ListUtils.isEmpty(models)){
            if(position == GroupVarManager.getInstance().coursePosition){
                mList.clear();
                mList.addAll(models);
                mGoodClassAdapter.notifyDataSetChanged();
                GroupVarManager.getInstance().mGoodClassMap.put(position,getListViewParams());
                EventBus.getDefault().post(AppConstants.EVENT_GOOD_CLASS);
            }
        }
    }

    /**
     * 获取Listview的高度 给首页的viewpager重新设置高度
     * @return
     */
    private ViewGroup.LayoutParams getListViewParams() {
        //通过ListView获取其中的适配器adapter
        ListAdapter listAdapter = mGvGoodClass.getAdapter();
        //声明默认高度为0
        int totalHeight = 0;
        //遍历listView中所有Item，累加所有item的高度就是ListView的实际高度（后面会考虑分割线的高度）
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View item = listAdapter.getView(i, null, mGvGoodClass);
            item.measure(0, 0);
            if(i % 2 == 0){
                totalHeight += item.getMeasuredHeight();
            }
        }
        ViewGroup.LayoutParams lp = mGvGoodClass.getLayoutParams();
        lp.height = totalHeight;
        if(listAdapter.getCount() == 0){
            lp.height = totalHeight;
        }else if(listAdapter.getCount() > 0 && listAdapter.getCount() <= 2){
            lp.height = totalHeight + (mGvGoodClass.getVerticalSpacing());
        }else {
            lp.height = totalHeight + (mGvGoodClass.getVerticalSpacing() * (listAdapter.getCount()/2 +1));
        }

        return lp;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = super.onCreateView(inflater, container, savedInstanceState);
        return rootView;
    }

}
