package com.jianzhong.lyag.ui.organization;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baselib.util.ActivityManager;
import com.baselib.widget.xlistview.XScrollView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.DepartmentSonAdapter;
import com.jianzhong.lyag.adapter.MemberByOrganizeAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.ContactsModel;
import com.jianzhong.lyag.model.DepartmentModel;
import com.jianzhong.lyag.util.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 按组织架构选择成员列表
 * Created by zhengwencheng on 2018/1/20 0020.
 * package com.jianzhong.bs.ui.fragment
 */

public class MemberByOrganizeActivity extends ToolbarActivity {

    @BindView(R.id.tv_classify)
    TextView mTvClassify;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.scroll_view)
    XScrollView mScrollView;
    @BindView(R.id.tv_range)
    TextView mTvRange;
    @BindView(R.id.tv_search)
    TextView mTvSearch;
    private View mView;
    private ViewHolder mViewHolder;
    private List<ContactsModel> mContactsModels = new ArrayList<>();
    private DepartmentModel mDepartmentModel; //公司部门的数据model
    private Map<String, DepartmentModel> map = new HashMap<>();
    private String parentBranchId;
    private String key;
    private int type;

    private MemberByOrganizeAdapter mMemberByOrganizeAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member_by_organize);
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
    public void initData() {
        super.initData();

        type = getIntent().getIntExtra("type", 0);
        mDepartmentModel = (DepartmentModel) getIntent().getSerializableExtra("department");
        parentBranchId = mDepartmentModel.getBranch_id();

        if(mDepartmentModel != null){
            setHeadTitle(mDepartmentModel.getBranch_name());
        }else {
            setHeadTitle("按组织架构选择");
        }
        showHeadTitle();
        setHeadRight("确定");
        showHeadRight();

        mTvSearch.setText("搜索姓名");
        setData();
        hideCommonLoading();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(GroupVarManager.getInstance().isSearchMember == 1){
            GroupVarManager.getInstance().isSearchMember = 0;

            for (int i = 0; i < mContactsModels.size(); i++) {
                for (int j = 0; j < GroupVarManager.getInstance().mMemberModels.size(); j++) {
                    if (mContactsModels.get(i).getUser_id().equals(GroupVarManager.getInstance().mMemberModels.get(j).getUser_id())) {
                        mContactsModels.get(i).setIsSelected(1);
                        break;
                    }
                }
            }

            mMemberByOrganizeAdapter.notifyDataSetChanged();
        }
        mTvRange.setText(CommonUtils.getAssignObject(GroupVarManager.getInstance().mDepartmentModels,
                GroupVarManager.getInstance().mAnecyModels, GroupVarManager.getInstance().mPostModels, GroupVarManager.getInstance().mMemberModels));

    }

    /**
     * 绑定数据
     */
    private void setData() {
        for (ContactsModel mContactsModel : mDepartmentModel.getMember()) {
            if (!mContactsModel.getParent_branch_id().equals("0") && mContactsModel.getParent_branch_id().equals(parentBranchId)) {
                key = mContactsModel.getBranch_id();
                if (map.get(key) != null) {
                    map.get(key).setCount(map.get(key).getCount() + 1);
                    map.get(key).setIs_dealer(mContactsModel.getIs_dealer());
                    map.get(key).getMember().add(mContactsModel);
                } else {
                    List<ContactsModel> cache = new ArrayList<>();
                    DepartmentModel item = new DepartmentModel();
                    item.setCount(1);
//                    if (key.equals(AppUserModel.getInstance().getmUserModel().getBranch_id())) {
//                        item.setBranch_name("我的部门");
//                    } else {
//                        item.setBranch_name(mContactsModel.getBranch_name());
//                    }
                    item.setBranch_name(mContactsModel.getBranch_name());
                    item.setBranch_id(key);
                    item.setIs_dealer(mContactsModel.getIs_dealer());
                    cache.add(mContactsModel);
                    item.setMember(cache);
                    map.put(key, item);
                }
            }
        }

        /**
         * 遍历map获取子部门的所有成员
         */
        for (Map.Entry<String, DepartmentModel> entry : map.entrySet()) {
            String key = entry.getKey();
            entry.getValue().setCount(getAllBranchNum(key, entry.getValue().getCount()));
        }

        Collection<DepartmentModel> valueCollection = map.values();
        List<DepartmentModel> valueList = new ArrayList<>(valueCollection);
        //设置上一次选中的部门
        for (int i = 0; i < valueList.size(); i++) {
            if (type == 1) {
                for (int j = 0; j < GroupVarManager.getInstance().mDepartmentModels.size(); j++) {
                    if (valueList.get(i).getBranch_id().equals(GroupVarManager.getInstance().mDepartmentModels.get(j).getBranch_id())) {
                        valueList.get(i).setIsSelected(1);
                        break;
                    }
                }
            } else {
                for (int j = 0; j < GroupVarManager.getInstance().mAnecyModels.size(); j++) {
                    if (valueList.get(i).getBranch_id().equals(GroupVarManager.getInstance().mAnecyModels.get(j).getBranch_id())) {
                        valueList.get(i).setIsSelected(1);
                        break;
                    }
                }
            }
        }

        mScrollView.setPullRefreshEnable(false);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {
            @Override
            public void onRefresh() {
            }

            @Override
            public void onLoadMore() {

            }
        });
        mScrollView.setRefreshTime(CommonUtils.getTime());
        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.content_member_by_organize, null);
            mViewHolder = new ViewHolder(mView);
            mScrollView.setView(mView);
        }


        DepartmentSonAdapter mDepartmentSelAdapter = new DepartmentSonAdapter(mContext, valueList);
        mViewHolder.mRvDepartment.setAdapter(mDepartmentSelAdapter);
        mViewHolder.mRvDepartment.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });

        mContactsModels.addAll(mDepartmentModel.getMember());
        for (int i = 0; i < mContactsModels.size(); i++) {
            for (int j = 0; j < GroupVarManager.getInstance().mMemberModels.size(); j++) {
                if (mContactsModels.get(i).getUser_id().equals(GroupVarManager.getInstance().mMemberModels.get(j).getUser_id())) {
                    mContactsModels.get(i).setIsSelected(1);
                    break;
                }
            }
        }
        mMemberByOrganizeAdapter = new MemberByOrganizeAdapter(mContext, mContactsModels);
        mViewHolder.mRvMember.setAdapter(mMemberByOrganizeAdapter);
        mViewHolder.mRvMember.setLayoutManager(new LinearLayoutManager(this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        });
    }

    /**
     * 获取每个部分的总分数
     *
     * @param id
     * @param number
     * @return
     */
    private int getAllBranchNum(String id, int number) {
        int result = number;
        int currentBranchNum = 0;
        String curBranchId = "";
        for (int i = 0; i < GroupVarManager.getInstance().mContactList.size(); i++) {
            if (GroupVarManager.getInstance().mContactList.get(i).getParent_branch_id().equals(id)) {
                map.get(id).getMember().add(GroupVarManager.getInstance().mContactList.get(i));
                result += 1;
                currentBranchNum += 1;
                curBranchId = GroupVarManager.getInstance().mContactList.get(i).getBranch_id();
            }
        }

        if (currentBranchNum == 0) {
            return result;
        } else {
            return getAllBranchNum(curBranchId, result);
        }
    }

    @OnClick({R.id.head_right, R.id.ll_search, R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_right:

                break;
            case R.id.ll_search:
                Intent intent = new Intent(mContext,MemberSearchActivity.class);
                startActivity(intent);
                break;

            case R.id.btn_confirm:
                ActivityManager.getActivityManager().finishActivity(MemberByOrganizeActivity.class);
                ActivityManager.getActivityManager().finishActivity(OrganizeSelectActivity.class);
                ActivityManager.getActivityManager().finishActivity(MemberSelectActivity.class);
                break;
        }
    }

    class ViewHolder {
        @BindView(R.id.rv_department)
        RecyclerView mRvDepartment;
        @BindView(R.id.rv_member)
        RecyclerView mRvMember;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
