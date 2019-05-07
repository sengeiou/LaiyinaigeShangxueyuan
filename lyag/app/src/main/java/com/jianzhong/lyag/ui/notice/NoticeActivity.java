package com.jianzhong.lyag.ui.notice;

import android.annotation.SuppressLint;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.NoticeAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewActivity;
import com.jianzhong.lyag.flexboxlayout.TagAdapter;
import com.jianzhong.lyag.flexboxlayout.TagFlowLayout;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.NoticeListModel;
import com.jianzhong.lyag.model.TagNoticeModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 重要通知首页
 * Created by zhengwencheng on 2018/2/26 0026.
 * package com.jianzhong.bs.ui.notice
 */
public class NoticeActivity extends BaseRecyclerViewActivity {

    @BindView(R.id.tag_FlowLayout)
    TagFlowLayout mTagFlowLayout;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.tv_more)
    TextView mTvMore;
    @BindView(R.id.iv_spread)
    ImageView mIvSpread;
    @BindView(R.id.iv_shrink_up)
    ImageView mIvShrinkUp;


    private List<TagNoticeModel> mTagNoticeModels = new ArrayList<>();
    //重要通知适配器
    private NoticeAdapter mNoticeAdapter;
    private List<NoticeListModel> mList = new ArrayList<>();
    private int pageIndex = 1;
    private List<String> tagArr = new ArrayList<>();
    private int isExpand = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notice);
        ButterKnife.bind(this);
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mNoticeAdapter = new NoticeAdapter(mContext, mList);
        return mNoticeAdapter;
    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("重要通知");
        showHeadTitle();

        initRecylerView();
        //获取重要通知
        getNoticeTag();
    }

    /**
     * 初始化列表
     */
    @SuppressLint("ClickableViewAccessibility")
    private void initRecylerView() {
//        LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTagFlowLayout.getLayoutParams();
//        lp.setMargins(DeviceInfoUtil.dip2px(mContext, 13), DeviceInfoUtil.dip2px(mContext, 8), DeviceInfoUtil.dip2px(mContext, 13), DeviceInfoUtil.dip2px(mContext, 8));
//        lp.height = DeviceInfoUtil.dip2px(mContext, 8) * 6 + DeviceInfoUtil.sp2px(mContext, 13);
//        mTagFlowLayout.setLayoutParams(lp);

        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_bg))
                .sizeResId(R.dimen.default_margin_8)
                .build());
        mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                pageIndex++;
                getNoticeList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                pageIndex = 1;
                mList.clear();
                getNoticeList();
            }
        });


    }

    /**
     * 获取重要通知标签
     */
    private void getNoticeTag() {
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_NOTICE_TAG, null, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<TagNoticeModel> resultList = GsonUtils.json2List(s, TagNoticeModel.class);
                if (resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    mTagNoticeModels.addAll(resultList.getData());
                    initTag();
                    getNoticeList();
                } else {
                    ToastUtils.show(mContext, resultList.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    private List<TagNoticeModel> mTagFlowList = new ArrayList<>();
    private TagAdapter mTagFlowAdapter;

    /**
     * 初始化筛选的Tag
     */
    private void initTag() {
        mTagFlowList.clear();
        if (!ListUtils.isEmpty(mTagNoticeModels)) {
            for (int i = 0; i < mTagNoticeModels.size(); i++) {
                TagNoticeModel model = new TagNoticeModel();
                model.setTag_id(mTagNoticeModels.get(i).getTag_id());
                model.setTag_name(mTagNoticeModels.get(i).getTag_name());
                model.setIsSelected(mTagNoticeModels.get(i).getIsSelected());
                mTagFlowList.add(model);
            }
        }

        mTagFlowAdapter = new TagAdapter<TagNoticeModel>(mTagNoticeModels) {
            @Override
            protected View getView(ViewGroup parent, int position, final TagNoticeModel item) {
                final TextView tv = (TextView) mInflater.inflate(R.layout.item_notice_flow_layout, parent, false);
                tv.setText(item.getTag_name());
                tv.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                        if (item.getIsSelected() == 0) {
                            item.setIsSelected(1);
                            tv.setBackground(getDrawable(R.drawable.shape_item_theme_white_16));
                            tv.setTextColor(getResources().getColor(R.color.color_theme));
                        } else {
                            item.setIsSelected(0);
                            tv.setBackground(getDrawable(R.drawable.shape_item_gray_white_16));
                            tv.setTextColor(getResources().getColor(R.color.color_888888));
                        }

                        tagArr.clear();
                        for (int i = 0; i < mTagNoticeModels.size(); i++) {
                            if (mTagNoticeModels.get(i).getIsSelected() == 1) {
                                tagArr.add(mTagNoticeModels.get(i).getTag_id());
                            }
                        }
                        mList.clear();
                        mNoticeAdapter.notifyDataSetChanged();
                        getNoticeList();
                    }
                });
                return tv;
            }
        };
        mTagFlowLayout.setAdapter(mTagFlowAdapter);
        setTagData();
    }

    /**
     * 获取重要通知列表
     */
    private void getNoticeList() {
        Map<String, Object> params = new HashMap<>();
        params.put("p", pageIndex);
        params.put("tag_id_set", GsonUtils.toJson(tagArr));
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_NOTICE_LIST, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                mPtrFrame.refreshComplete();
                ResultList<NoticeListModel> resultList = GsonUtils.json2List(s, NoticeListModel.class);
                if (resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    mList.addAll(resultList.getData());
                    mNoticeAdapter.notifyDataSetChanged();
                } else {
                    ToastUtils.show(mContext, resultList != null ? resultList.getMessage() : "数据解析错误");
                }
                isShowNoDataView();
            }

            @Override
            public void onFailure(String msg) {
                mPtrFrame.refreshComplete();
                ToastUtils.show(mContext, msg);
                showErrorView();
            }
        });
    }

    @OnClick(R.id.ll_status)
    public void onViewClicked() {

        if (isExpand == 0) {
            mIvShrinkUp.setVisibility(View.VISIBLE);
            mIvSpread.setVisibility(View.GONE);
            mTvMore.setText("收起");
            isExpand = 1;
            setTagData();
//            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTagFlowLayout.getLayoutParams();
//            lp.setMargins(DeviceInfoUtil.dip2px(mContext, 13), DeviceInfoUtil.dip2px(mContext, 8), DeviceInfoUtil.dip2px(mContext, 13), DeviceInfoUtil.dip2px(mContext, 8));
//            lp.height = LinearLayout.LayoutParams.WRAP_CONTENT;
//            lp.width = LinearLayout.LayoutParams.WRAP_CONTENT;
//            mTagFlowLayout.setLayoutParams(lp);
        } else {
            mIvShrinkUp.setVisibility(View.GONE);
            mIvSpread.setVisibility(View.VISIBLE);
            mTvMore.setText("更多");
            isExpand = 0;
            setTagData();
//            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) mTagFlowLayout.getLayoutParams();
//            lp.setMargins(DeviceInfoUtil.dip2px(mContext, 13), DeviceInfoUtil.dip2px(mContext, 8), DeviceInfoUtil.dip2px(mContext, 13), DeviceInfoUtil.dip2px(mContext, 8));
//            lp.height = DeviceInfoUtil.dip2px(mContext, 8) * 6 + DeviceInfoUtil.sp2px(mContext, 13);
//            mTagFlowLayout.setLayoutParams(lp);
        }
    }

    private void setTagData() {
        if (isExpand == 0) {
            if (!ListUtils.isEmpty(mTagFlowList)) {
                mTagNoticeModels.clear();
                for (int i = 0; i < mTagFlowList.size(); i++) {
                    if (i <= 3) {
                        TagNoticeModel model = new TagNoticeModel();
                        model.setTag_id(mTagFlowList.get(i).getTag_id());
                        model.setTag_name(mTagFlowList.get(i).getTag_name());
                        model.setIsSelected(mTagFlowList.get(i).getIsSelected());
                        mTagNoticeModels.add(model);
                    }
                }
            }
        } else {
            if (!ListUtils.isEmpty(mTagFlowList)) {
                mTagNoticeModels.clear();
                for (int i = 0; i < mTagFlowList.size(); i++) {
                    TagNoticeModel model = new TagNoticeModel();
                    model.setTag_id(mTagFlowList.get(i).getTag_id());
                    model.setTag_name(mTagFlowList.get(i).getTag_name());
                    model.setIsSelected(mTagFlowList.get(i).getIsSelected());
                    mTagNoticeModels.add(model);
                }
            }
        }
        mTagFlowAdapter.notifyDataSetChanged();
    }


}
