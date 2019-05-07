package com.jianzhong.lyag.ui.fragment;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;

import com.baselib.util.ListUtils;
import com.baselib.util.StringUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.DepartmentSelAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewFragment;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.ContactsModel;
import com.jianzhong.lyag.model.DepartmentModel;
import com.jianzhong.lyag.widget.sortlistview.CommonPinyinComparatorImpl;
import com.jianzhong.lyag.widget.sortlistview.SideBar;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;

/**
 * 按组织架构选择公司部分Fragment
 * Created by zhengwencheng on 2018/1/19 0019.
 * package com.jianzhong.bs.ui.fragment
 */
public class CompanySelectFragment extends BaseRecyclerViewFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.side_bar_list)
    SideBar mSideBarList;

    private DepartmentSelAdapter mCompanySelectAdapter = null;
    private List<DepartmentModel> mDepartmentModels = new ArrayList<>();
    private Map<String, DepartmentModel> map = new HashMap<>();
    private String key;

    public static CompanySelectFragment getInstance() {
        CompanySelectFragment fragment = new CompanySelectFragment();
        return fragment;
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mCompanySelectAdapter = new DepartmentSelAdapter(mContext, mDepartmentModels);
        return mCompanySelectAdapter;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_company_select, null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();

        hideCommonLoading();
        loadData();
    }

    /**
     * 从原数据中装载出部门格式数据
     */
    private void loadData() {
        for (int i = 0; i < GroupVarManager.getInstance().mContactList.size(); i++) {
            if (GroupVarManager.getInstance().mContactList.get(i).getIs_outermost().equals("1") && GroupVarManager.getInstance().mContactList.get(i).getIs_dealer().equals("0")) { //parent_branch_id为0 就是没有上级部门 这里只显示没有上级部门的
                key = GroupVarManager.getInstance().mContactList.get(i).getBranch_id();
                if (map.get(key) != null) {
                    map.get(key).setCount(map.get(key).getCount() + 1);
                    map.get(key).setIs_dealer(GroupVarManager.getInstance().mContactList.get(i).getIs_dealer());
                    map.get(key).getMember().add(GroupVarManager.getInstance().mContactList.get(i));
                } else {
                    List<ContactsModel> cache = new ArrayList<>();
                    DepartmentModel item = new DepartmentModel();
                    item.setCount(1);
                    if (!StringUtils.isEmpty(AppUserModel.getInstance().getmUserModel().getBranch_id()) && key.equals(AppUserModel.getInstance().getmUserModel().getBranch_id())) {
                        item.setBranch_name("我的部门");
                    } else {
                        item.setBranch_name(GroupVarManager.getInstance().mContactList.get(i).getBranch_name());
                    }
                    item.setIs_dealer(GroupVarManager.getInstance().mContactList.get(i).getIs_dealer());
                    item.setBranch_id(key);
                    cache.add(GroupVarManager.getInstance().mContactList.get(i));
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
//            entry.getValue().setCount(entry.getValue().getCount());
        }

        Collection<DepartmentModel> valueCollection = map.values();
        List<DepartmentModel> valueList = new ArrayList<>(valueCollection);
        for (int i = 0; i < valueList.size(); i++) {
            for (int j = 0; j < GroupVarManager.getInstance().mDepartmentModels.size(); j++) {
                if(valueList.get(i).getBranch_id().equals(GroupVarManager.getInstance().mDepartmentModels.get(j).getBranch_id())){
                    valueList.get(i).setIsSelected(1);
                    break;
                }
            }
        }

        setCompanyData(valueList);
    }


    /**
     * @param id     当前的branch_id
     * @param number 已有当前已有部分人数
     * @return
     */
    private int getAllBranchNum(String id, int number) {

        int result = number;
        int currentBranchNum = 0;
        String curBranchId = "";

        for (int i = 0; i < GroupVarManager.getInstance().mContactList.size(); i++) {
            if (GroupVarManager.getInstance().mContactList.get(i).getParent_branch_id().equals(id)) {
                if(map.get(id) != null){
                    map.get(id).getMember().add(GroupVarManager.getInstance().mContactList.get(i));
                    result += 1;
                    currentBranchNum += 1;
                    curBranchId = GroupVarManager.getInstance().mContactList.get(i).getBranch_id();
                }
            }
        }

        if (currentBranchNum == 0) {
            return result;
        } else {
            return getAllBranchNum(curBranchId, result);
        }
    }

    /**
     * 刷新Adapter
     *
     * @param list
     */
    private void setCompanyData(List<DepartmentModel> list) {
        if (!ListUtils.isEmpty(list)) {
            // 根据a-z进行排序源数据
            Collections.sort(list, new CommonPinyinComparatorImpl());

            mDepartmentModels.addAll(list);
            mCompanySelectAdapter.notifyDataSetChanged();
        }

        // 点击字母定位到字母
        mSideBarList.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {

            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = mCompanySelectAdapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    mRecyclerView.scrollToPosition(position);
                }
            }
        });
    }


}
