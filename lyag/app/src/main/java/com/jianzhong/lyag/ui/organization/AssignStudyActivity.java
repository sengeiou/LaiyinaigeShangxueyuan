package com.jianzhong.lyag.ui.organization;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import com.baselib.util.DateUtils;
import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.AssignCommitModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.DialogHelper;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 指派学习页面
 * Created by zhengwencheng on 2018/1/20 0020.
 * package com.jianzhong.bs.ui.organization
 */
public class AssignStudyActivity extends ToolbarActivity {

    @BindView(R.id.et_require)
    EditText mEtRequire;
    @BindView(R.id.tv_target)
    TextView mTvTarget;
    @BindView(R.id.tv_time)
    TextView mTvTime;

    private String foreign_id;
    private String date;
    private String asset_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_assign_study);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        foreign_id = getIntent().getStringExtra("foreign_id");
        asset_type = getIntent().getStringExtra("asset_type");

        //进来先清除选一下
        GroupVarManager.getInstance().mDepartmentModels.clear();
        GroupVarManager.getInstance().mAnecyModels.clear();
        GroupVarManager.getInstance().mMemberModels.clear();
        GroupVarManager.getInstance().mPostModels.clear();

        setHeadTitle("指派学习");
        showHeadTitle();
        setHeadRight("确定");
        showHeadRight();

    }


    @OnClick({R.id.head_right, R.id.ll_assign_target,R.id.ll_time})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.head_right:
                sendDictate();
                break;
            case R.id.ll_assign_target:
                intent = new Intent(mContext, MemberSelectActivity.class);
                startActivity(intent);
                break;
            case R.id.ll_time:
                CommonUtils.getDate(this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        date = DateUtils.timeStampToDate(millseconds, "yyyy-MM-dd");
                        mTvTime.setText(date);
                    }
                });
                break;
        }
    }

    /**
     * 判断是否提交
     */
    private void sendDictate() {
        if(mTvTarget.getText().toString().equals("选择")){
            ToastUtils.show(mContext,"请选择指派对象");
        }else if(mTvTime.getText().toString().equals("选择")){
            ToastUtils.show(mContext,"请选择结束时间");
        }else if(StringUtils.isEmpty(mEtRequire.getText().toString())){
            ToastUtils.show(mContext,"请选择学习要求");
        }else {
            AssignCommitModel item = new AssignCommitModel();
            HashSet<String> branch = new HashSet<>();
            HashSet<String> user = new HashSet<>();
            for (int i = 0; i < GroupVarManager.getInstance().mDepartmentModels.size(); i++) {
                branch.add(GroupVarManager.getInstance().mDepartmentModels.get(i).getBranch_id());
            }
            for (int i = 0; i < GroupVarManager.getInstance().mAnecyModels.size(); i++) {
                branch.add(GroupVarManager.getInstance().mDepartmentModels.get(i).getBranch_id());
            }
            for (int i = 0; i < GroupVarManager.getInstance().mMemberModels.size(); i++) {
                user.add(GroupVarManager.getInstance().mMemberModels.get(i).getUser_id());
            }
            for (int i = 0; i < GroupVarManager.getInstance().mPostModels.size(); i++) {
                if(!ListUtils.isEmpty(GroupVarManager.getInstance().mPostModels.get(i).getMember())){
                    for (int j = 0; j < GroupVarManager.getInstance().mPostModels.get(i).getMember().size(); j++) {
                        user.add(GroupVarManager.getInstance().mPostModels.get(i).getMember().get(j).getUser_id());
                    }
                }
            }
            item.setBranch(branch);
            item.setUser(user);
            Map<String,Object> params = new HashMap<>();
            params.put("asset_type",asset_type);
            params.put("title",mEtRequire.getText().toString());
            params.put("finish_at",mTvTime.getText().toString());
            params.put("user_range", GsonUtils.toJson(item));
            params.put("foreign_id",foreign_id);
            DialogHelper.showDialog(mContext);
            HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DICTATE_SEND, params, new HttpCallBack() {
                @Override
                public void onSuccess(String s) {
                    Result result = GsonUtils.json2Bean(s,Result.class);
                    DialogHelper.dismissDialog();
                    if(result.getCode() == HttpConfig.STATUS_SUCCESS){
                        ToastUtils.show(mContext,"指派学习成功");
                        AssignStudyActivity.this.finish();
                    }else {
                        ToastUtils.show(mContext,result.getMessage());
                    }
                }

                @Override
                public void onFailure(String msg) {
                    DialogHelper.dismissDialog();
                    ToastUtils.show(mContext,msg);
                }
            });
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (ListUtils.isEmpty(GroupVarManager.getInstance().mDepartmentModels) && ListUtils.isEmpty(GroupVarManager.getInstance().mAnecyModels) &&
                ListUtils.isEmpty(GroupVarManager.getInstance().mMemberModels) && ListUtils.isEmpty(GroupVarManager.getInstance().mPostModels)) {
            mTvTarget.setText("选择");
        } else {
            mTvTarget.setText(CommonUtils.getAssignObject(GroupVarManager.getInstance().mDepartmentModels,
                    GroupVarManager.getInstance().mAnecyModels, GroupVarManager.getInstance().mPostModels,GroupVarManager.getInstance().mMemberModels));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        //进来先清除选一下
        GroupVarManager.getInstance().mDepartmentModels.clear();
        GroupVarManager.getInstance().mAnecyModels.clear();
        GroupVarManager.getInstance().mMemberModels.clear();
        GroupVarManager.getInstance().mPostModels.clear();
    }
}
