package com.jianzhong.lyag.ui.organization;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baselib.util.ActivityManager;
import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.MemberListAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.ContactsModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.widget.sortlistview.CommonPinyinComparatorImpl;
import com.jianzhong.lyag.widget.sortlistview.SideBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 成员列表选择
 * Created by zhengwencheng on 2018/1/18 0018.
 * package com.jianzhong.bs.ui.organization
 */

public class MemberSelectActivity extends BaseRecyclerViewActivity {

    @BindView(R.id.side_bar_list)
    SideBar mSideBarList;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.tv_range)
    TextView mTvRange; //已选择的
    @BindView(R.id.tv_search)
    TextView mTvSearch;

    private MemberListAdapter mMemberListAdapter = null;
    private List<ContactsModel> mList = new ArrayList<>();

    private int type; //1：拿求助分享邀请范围

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member_select);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mMemberListAdapter = new MemberListAdapter(mContext, mList,1);
        return mMemberListAdapter;
    }

    @Override
    public void initData() {
        super.initData();

        type = getIntent().getIntExtra("type", 0);
        setHeadTitle("选择成员");
        showHeadTitle();
//        setHeadRight("确定");
//        showHeadRight();

        getDictateRange();
        mPtrFrame.setMode(PtrFrameLayout.Mode.NONE);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mList.clear();
                getDictateRange();
            }
        });

    }

    /**
     * 获取数据
     */
    private void getDictateRange() {
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + (type == 0 ? HttpConfig.URL_DICTATE_RANGE : HttpConfig.URL_INVITE_RANGE), null, new HttpCallBack() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String s) {
                mPtrFrame.refreshComplete();
                ResultList<ContactsModel> resultList = GsonUtils.json2List(s, ContactsModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    GroupVarManager.getInstance().mContactList.clear();
                    GroupVarManager.getInstance().mContactList.addAll(resultList.getData());
                    mTvSearch.setText("搜索同事，共"+GroupVarManager.getInstance().mContactList.size()+"个同事");
                    setMemberData(GroupVarManager.getInstance().mContactList);
                    hideCommonLoading();
                } else {
                    ToastUtils.show(mContext, resultList != null ? resultList.getMessage() : "数据解析错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateAssign(String tag) {
        if (tag.equals(AppConstants.TAG_UPDATE_ASSIGN)) {
            mTvRange.setText(CommonUtils.getAssignObject(GroupVarManager.getInstance().mDepartmentModels,
                    GroupVarManager.getInstance().mAnecyModels, GroupVarManager.getInstance().mPostModels, GroupVarManager.getInstance().mMemberModels));
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        mTvRange.setText(CommonUtils.getAssignObject(GroupVarManager.getInstance().mDepartmentModels,
                GroupVarManager.getInstance().mAnecyModels, GroupVarManager.getInstance().mPostModels, GroupVarManager.getInstance().mMemberModels));

        mMemberListAdapter.notifyDataSetChanged();
    }

    @OnClick({R.id.ll_search, R.id.ll_by_organize, R.id.ll_by_tag, R.id.head_right, R.id.btn_confirm})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_search:
                intent = new Intent(mContext,MemberSearchActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_by_organize:
                intent = new Intent(mContext, OrganizeSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_by_tag:
                intent = new Intent(mContext, TagSelcectActivity.class);
                startActivity(intent);
                break;
            case R.id.head_right:

                break;
            case R.id.btn_confirm:
                ActivityManager.getActivityManager().finishActivity(MemberSelectActivity.class);
                break;
            default:
                break;
        }
    }

    /**
     * 刷新Adapter
     *
     * @param list
     */
    private void setMemberData(List<ContactsModel> list) {
        if (!ListUtils.isEmpty(list)) {
            // 根据a-z进行排序源数据
            Collections.sort(list, new CommonPinyinComparatorImpl());

            mList.addAll(list);
            if(!ListUtils.isEmpty(mList)){
                for (int i = 0; i < mList.size(); i++) {
                    for (int j = 0; j < GroupVarManager.getInstance().mMemberModels.size(); j++) {
                        if(mList.get(i).getUser_id().equals(GroupVarManager.getInstance().mMemberModels.get(j).getUser_id())){
                            mList.get(i).setIsSelected(1);
                            break;
                        }
                    }
                }
            }
            mMemberListAdapter.notifyDataSetChanged();
        }

        if (ListUtils.isEmpty(list)) {
            mSideBarList.setVisibility(View.GONE);
            mTvSearch.setText("搜索同事");
        } else {
            mSideBarList.setVisibility(View.VISIBLE);
            mTvSearch.setText("搜索同事，共"+ list.size() + "个同事");
        }
        // 点击字母定位到字母
        mSideBarList.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mMemberListAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mRecyclerView.scrollToPosition(position);
                }
            }
        });
    }

}
