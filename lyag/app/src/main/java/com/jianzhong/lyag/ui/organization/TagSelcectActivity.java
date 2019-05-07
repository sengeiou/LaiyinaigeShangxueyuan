package com.jianzhong.lyag.ui.organization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.baselib.util.ActivityManager;
import com.baselib.util.ListUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.TagSelectAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.ContactsModel;
import com.jianzhong.lyag.model.DepartmentModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.widget.sortlistview.CommonPinyinComparatorImpl;
import com.jianzhong.lyag.widget.sortlistview.SideBar;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
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
 * 按按岗位标签选择
 * Created by zhengwencheng on 2018/1/19 0019.
 * package com.jianzhong.bs.ui.organization
 */
public class TagSelcectActivity extends BaseRecyclerViewActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.side_bar_list)
    SideBar mSideBarList;
    @BindView(R.id.tv_range)
    TextView mTvRange;
    @BindView(R.id.tv_search)
    TextView mTvSearch;

    private TagSelectAdapter mTagSelectAdapter = null;
    private List<DepartmentModel> mTagSelectModels = new ArrayList<>();
    private Map<String, DepartmentModel> map = new HashMap<>();
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tag_select);
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
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mTagSelectAdapter = new TagSelectAdapter(mContext, mTagSelectModels);
        return mTagSelectAdapter;
    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("按岗位标签选择");
        showHeadTitle();

        mTvSearch.setText("搜索姓名");
        loadData();
        hideCommonLoading();
    }

    /**
     * 从原数据中装载出部门格式数据
     */
    private void loadData() {
        for (int i = 0; i < GroupVarManager.getInstance().mContactList.size(); i++) {
            key = GroupVarManager.getInstance().mContactList.get(i).getPos_id();
            if (map.get(key) != null) {
                map.get(key).setCount(map.get(key).getCount() + 1);
                map.get(key).setIs_dealer(GroupVarManager.getInstance().mContactList.get(i).getIs_dealer());
                map.get(key).getMember().add(GroupVarManager.getInstance().mContactList.get(i));
            } else {
                List<ContactsModel> cache = new ArrayList<>();
                DepartmentModel item = new DepartmentModel();
                item.setCount(1);
                item.setBranch_name(GroupVarManager.getInstance().mContactList.get(i).getPos_name());
                item.setIs_dealer(GroupVarManager.getInstance().mContactList.get(i).getIs_dealer());
                item.setPos_id(key);
                cache.add(GroupVarManager.getInstance().mContactList.get(i));
                item.setMember(cache);
                map.put(key, item);
            }
        }

        Collection<DepartmentModel> valueCollection = map.values();
        List<DepartmentModel> valueList = new ArrayList<>(valueCollection);
        for (int i = 0; i < valueList.size(); i++) {
            for (int j = 0; j < GroupVarManager.getInstance().mPostModels.size(); j++) {
                if (valueList.get(i).getPos_id().equals(GroupVarManager.getInstance().mPostModels.get(j).getPos_id())) {
                    valueList.get(i).setIsSelected(1);
                    break;
                }
            }
        }
        setTagData(valueList);
    }


    @OnClick(R.id.ll_search)
    public void onViewClicked() {

    }

    /**
     * 刷新Adapter
     *
     * @param list
     */
    private void setTagData(List<DepartmentModel> list) {
        if (!ListUtils.isEmpty(list)) {
            // 根据a-z进行排序源数据
            Collections.sort(list, new CommonPinyinComparatorImpl());

            mTagSelectModels.addAll(list);
            mTagSelectAdapter.notifyDataSetChanged();
        }

//        if (ListUtils.isEmpty(list)) {
//            mSideBarList.setVisibility(View.GONE);
//        } else {
//            mSideBarList.setVisibility(View.VISIBLE);
//        }
        mSideBarList.setVisibility(View.GONE);
        // 点击字母定位到字母
        mSideBarList.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mTagSelectAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
//                    mLvMember.smoothScrollToPosition(position);
                    mRecyclerView.scrollToPosition(position);
                }
            }
        });

        mPtrFrame.setMode(PtrFrameLayout.Mode.REFRESH);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                mPtrFrame.refreshComplete();
            }
        });
    }

    @OnClick({R.id.tv_range, R.id.btn_confirm,R.id.ll_search})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_range:

                break;
            case R.id.btn_confirm:
                ActivityManager.getActivityManager().finishActivity(TagSelcectActivity.class);
                ActivityManager.getActivityManager().finishActivity(MemberSelectActivity.class);
                break;
            case R.id.ll_search:
                Intent intent = new Intent(mContext,MemberSearchActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvRange.setText(CommonUtils.getAssignObject(GroupVarManager.getInstance().mDepartmentModels,
                GroupVarManager.getInstance().mAnecyModels, GroupVarManager.getInstance().mPostModels, GroupVarManager.getInstance().mMemberModels));
    }
}
