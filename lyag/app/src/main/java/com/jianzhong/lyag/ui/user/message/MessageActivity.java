package com.jianzhong.lyag.ui.user.message;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.MessageAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewActivity;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.MessageModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * Created by chenzhikai on 2018/4/25.
 */

public class MessageActivity extends BaseRecyclerViewActivity {

    private int page = 1;
    private MessageAdapter mAdapter;
    private List<MessageModel> mList = new ArrayList<>();

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new MessageAdapter(this, mList);
        return mAdapter;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);
    }

    @Override
    public void initData() {
        super.initData();
        setHeadTitle("消息通知");
        showHeadTitle();
        initRecyclerView();
        getData();
    }

    /**
     * 初始化列表
     */
    private void initRecyclerView() {
        mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                ++page;
                getData();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                page = 0;
                mList.clear();
                getData();
            }
        });
    }

    @Override
    public void getData() {
        Map<String, Object> params = new HashMap<>();
        params.put("p", page);

        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_USER_GET_PUSH, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                hideCommonLoading();
                ResultList<MessageModel> result = GsonUtils.json2List(s, MessageModel.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    notifyDataSetChanged(result.getData());
                } else {
                    ToastUtils.show(mContext, result.getMessage());
                }
                mPtrFrame.refreshComplete();
            }

            @Override
            public void onFailure(String msg) {
                super.onFailure(msg);
                mPtrFrame.refreshComplete();
                hideCommonLoading();
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 刷新列表
     * @param list
     */
    private void notifyDataSetChanged(List<MessageModel> list){
        if (page == 1) {
            mList.clear();
        }
        if (!ListUtils.isEmpty(list)) {
            mList.addAll(list);
        }
        mAdapter.notifyDataSetChanged();
    }

}
