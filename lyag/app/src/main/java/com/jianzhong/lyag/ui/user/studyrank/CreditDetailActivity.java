package com.jianzhong.lyag.ui.user.studyrank;

import android.os.Bundle;
import android.widget.ExpandableListView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.PointDetailAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.PointDetailModel;
import com.jianzhong.lyag.model.PointExpandModel;
import com.jianzhong.lyag.util.CommonUtils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 学分详情页面
 * create by zhengwencheng on 2018/3/26 0026
 * package com.jianzhong.bs.ui.user.studyrank
 */
public class CreditDetailActivity extends ToolbarActivity {

    @BindView(R.id.expandable_listview)
    ExpandableListView mExpandableListview;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;

    private int pageIndex = 1;
    private List<PointExpandModel> mPointExpandModels = new ArrayList<>();
    private PointDetailAdapter mAdapter;

    private TreeMap<String, PointExpandModel> map = new TreeMap<>();
    private String key;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_credit_detail);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("学分明细");
        showHeadTitle();

        mAdapter = new PointDetailAdapter(this,mPointExpandModels);
        mExpandableListview.setAdapter(mAdapter);

        getPointDetail();

        // 刷新控件
        mPtrFrame.setLastUpdateTimeRelateObject(this);
        mPtrFrame.setResistance(1.7f);
        mPtrFrame.setRatioOfHeaderHeightToRefresh(1.2f);
        mPtrFrame.setDurationToClose(200);
        mPtrFrame.setDurationToCloseHeader(1000);
        mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);
        mPtrFrame.autoLoadMore(false);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                pageIndex++;

                getPointDetail();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                mPointExpandModels.clear();
                map.clear();
                getPointDetail();
            }

        });
    }

    /**
     * 获取学分明细
     */
    private void getPointDetail(){
        Map<String,Object> params = new HashMap<>();
        params.put("p",pageIndex+"");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_POINT_DETAIL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                mPtrFrame.refreshComplete();
                ResultList<PointDetailModel> resultList = GsonUtils.json2List(s,PointDetailModel.class);
                if(resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS){
                    hideCommonLoading();
                    setData(resultList.getData());
                }else {
                    ToastUtils.show(mContext,resultList != null ? resultList.getMessage():"数据解析错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext,msg);
            }
        });
    }

    private void setData(List<PointDetailModel> mList){
        for (PointDetailModel mPointDetailModel : mList) {
            if (!mPointDetailModel.getCreate_at().equals("0")) {
                key = CommonUtils.timeStampToDate(mPointDetailModel.getCreate_at(),"yyyy-MM");
                if (map.get(key) != null) {
                    map.get(key).getPoint_detail().add(mPointDetailModel);
                } else {
                    List<PointDetailModel> cache = new ArrayList<>();
                    PointExpandModel item = new PointExpandModel();
                    item.setTag(key);
                    cache.add(mPointDetailModel);
                    item.setPoint_detail(cache);
                    map.put(key, item);
                }
            }
        }


        Collection<PointExpandModel> valueCollection = map.values();
        List<PointExpandModel> valueList = new ArrayList<>(valueCollection);

        mPointExpandModels.addAll(valueList);

        notifyDataSetChanged();
    }

    private void notifyDataSetChanged() {
        mPtrFrame.refreshComplete();
        if (!ListUtils.isEmpty(mPointExpandModels)) {
            // 设置ExpandableListView的子项默认展开
            for (int i = 0; i < mPointExpandModels.size(); i++) {
                mExpandableListview.expandGroup(i);
            }
            mAdapter.notifyDataSetChanged();
        }
    }
}
