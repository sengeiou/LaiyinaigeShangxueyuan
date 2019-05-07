package com.jianzhong.lyag.ui.organization;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baselib.util.ListUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.MemberListAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.ContactsModel;
import com.jianzhong.lyag.util.CommonUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 全部搜索的页面
 * Created by zhengwencheng on 2018/1/27 0027.
 * package com.jianzhong.bs.ui.navigation
 */
public class MemberSearchActivity extends BaseRecyclerViewActivity {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.tv_range)
    TextView mTvRange;

    private String keyWord;
    private MemberListAdapter mMemberListAdapter;
    private List<ContactsModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_member_search);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        GroupVarManager.getInstance().isSearchMember = 1;
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
        mMemberListAdapter = new MemberListAdapter(mContext, mList,0);
        return mMemberListAdapter;
    }

    @Override
    public void initData() {
        super.initData();
        hideCommonLoading();
        //
        initListener();
    }

    @OnClick({R.id.tv_cancel,R.id.btn_confirm})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:    //取消
                MemberSearchActivity.this.finish();
                break;
            case R.id.btn_confirm:
                MemberSearchActivity.this.finish();
                break;
        }
    }


    private void initListener() {
        mPtrFrame.setMode(PtrFrameLayout.Mode.NONE);
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });

        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(MemberSearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEtClassSearch的非空判断
                    if ((mEtSearch.getText().toString()).isEmpty()) {
                        ToastUtils.show(mContext, "请输入搜索关键字");
                    } else {
                        keyWord = mEtSearch.getText().toString();
                        mList.clear();
                        if (!ListUtils.isEmpty(search(keyWord))) {
                            mList.addAll(search(keyWord));
                            for (int i = 0; i < mList.size(); i++) {
                                for (int j = 0; j < GroupVarManager.getInstance().mMemberModels.size(); j++) {
                                    if (mList.get(i).getUser_id().equals(GroupVarManager.getInstance().mMemberModels.get(j).getUser_id())) {
                                        mList.get(i).setIsSelected(1);
                                    }
                                }
                            }
                        }
                        mMemberListAdapter.notifyDataSetChanged();
                    }

                }
                return false;
            }
        });
    }

    public List<ContactsModel> search(String keyWord) {
        List<ContactsModel> results = new ArrayList<>();
        Pattern pattern = Pattern.compile(keyWord);
        for (int i = 0; i < GroupVarManager.getInstance().mContactList.size(); i++) {
            Matcher matcher = pattern.matcher((GroupVarManager.getInstance().mContactList.get(i)).getRealname());
            if (matcher.find()) {
                results.add(GroupVarManager.getInstance().mContactList.get(i));
            }
        }
        return results;
    }

    @Override
    protected void onResume() {
        super.onResume();
        mTvRange.setText(CommonUtils.getAssignObject(GroupVarManager.getInstance().mDepartmentModels,
                GroupVarManager.getInstance().mAnecyModels, GroupVarManager.getInstance().mPostModels, GroupVarManager.getInstance().mMemberModels));
    }
}
