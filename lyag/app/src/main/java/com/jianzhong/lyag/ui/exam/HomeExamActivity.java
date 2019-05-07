package com.jianzhong.lyag.ui.exam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.baselib.util.DeviceInfoUtil;
import com.baselib.util.GsonUtils;
import com.baselib.util.Result;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.CommonWebActivity;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.TaskModel;
import com.jianzhong.lyag.model.TaskViewModel;
import com.jianzhong.lyag.util.CommonUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 成长路线首页
 * Created by zhengwencheng on 2018/1/30 0030.
 * package com.jianzhong.bs.ui.exam
 */

public class HomeExamActivity extends ToolbarActivity {
    //图片的宽高 1334X750
    private static final int IMG_WIDTH = 750;
    private static final int IMG_HEIGHT = 1334;
    //必修课向左margin的宽度
    private static final int BX_MARGIN = 336;

    @BindView(R.id.ll_bx_1)
    LinearLayout mLlBx1;
    @BindView(R.id.ll_bx_5)
    LinearLayout mLlBx5;
    @BindView(R.id.ll_bx_3)
    LinearLayout mLlBx3;
    @BindView(R.id.ll_bx_4)
    LinearLayout mLlBx4;
    @BindView(R.id.ll_bx_2)
    LinearLayout mLlBx2;
    @BindView(R.id.cb_bx_1)
    CheckBox mCbBx1;
    @BindView(R.id.cb_bx_5)
    CheckBox mCbBx5;
    @BindView(R.id.cb_bx_4)
    CheckBox mCbBx4;
    @BindView(R.id.cb_bx_3)
    CheckBox mCbBx3;
    @BindView(R.id.cb_bx_2)
    CheckBox mCbBx2;
    @BindView(R.id.ll_xx_1)
    LinearLayout mLlXx1;
    @BindView(R.id.ll_xx_2)
    LinearLayout mLlXx2;
    @BindView(R.id.ll_xx_3)
    LinearLayout mLlXx3;
    @BindView(R.id.ll_xx_4)
    LinearLayout mLlXx4;
    @BindView(R.id.ll_xx_5)
    LinearLayout mLlXx5;
    @BindView(R.id.cb_xx_1)
    CheckBox mCbXx1;
    @BindView(R.id.cb_xx_2)
    CheckBox mCbXx2;
    @BindView(R.id.cb_xx_3)
    CheckBox mCbXx3;
    @BindView(R.id.cb_xx_4)
    CheckBox mCbXx4;
    @BindView(R.id.cb_xx_5)
    CheckBox mCbXx5;
    @BindView(R.id.tv_xx_1)
    TextView mTvXx1;
    @BindView(R.id.tv_xx_2)
    TextView mTvXx2;
    @BindView(R.id.tv_xx_3)
    TextView mTvXx3;
    @BindView(R.id.tv_xx_4)
    TextView mTvXx4;
    @BindView(R.id.tv_xx_5)
    TextView mTvXx5;
    @BindView(R.id.tv_bx_5)
    TextView mTvBx5;
    @BindView(R.id.tv_bx_4)
    TextView mTvBx4;
    @BindView(R.id.tv_bx_3)
    TextView mTvBx3;
    @BindView(R.id.tv_bx_2)
    TextView mTvBx2;
    @BindView(R.id.tv_bx_1)
    TextView mTvBx1;
    @BindView(R.id.tv_job)
    TextView mTvJob;
    //选修和必修的id
    private String b_task_id_1;
    private String b_task_id_2;
    private String b_task_id_3;
    private String b_task_id_4;
    private String b_task_id_5;

    private String x_task_id_1;
    private String x_task_id_2;
    private String x_task_id_3;
    private String x_task_id_4;
    private String x_task_id_5;

    private List<TaskViewModel> mBxViewList = new ArrayList<>();
    private List<TaskViewModel> mXxViewList = new ArrayList<>();
    private TaskModel data;
    /**
     * 获取屏幕宽度
     */
    private WindowManager wm;
    private DisplayMetrics dm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_home_exam);
        ButterKnife.bind(this);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void initData() {
        super.initData();

        wm = getWindowManager();
        dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);
        setHeadTitle("成长路线");
        showHeadTitle();
        mTvJob.setText("岗位："+ AppUserModel.getInstance().getmUserModel().getPos_name());

        //根据比例设置margin位置
        mLlBx1.setLayoutParams(getLayoutParamsLeft(mLlBx1, 32));
        mLlBx2.setLayoutParams(getLayoutParamsRight(mLlBx2, 6));
        mLlBx3.setLayoutParams(getLayoutParamsLeft(mLlBx3, 6));
        mLlBx4.setLayoutParams(getLayoutParamsRight(mLlBx4, 6));
        mLlBx5.setLayoutParams(getLayoutParamsLeft(mLlBx5, 6));

        mLlXx1.setLayoutParams(getLayoutParamsTop(mLlXx1, 1));
        mLlXx2.setLayoutParams(getLayoutParamsTop(mLlXx2, 2));
        mLlXx3.setLayoutParams(getLayoutParamsTop(mLlXx3, 3));
        mLlXx4.setLayoutParams(getLayoutParamsTop(mLlXx4, 4));
        mLlXx5.setLayoutParams(getLayoutParamsTop(mLlXx5, 5));

        //
        initController();

        getTaskList();
    }

    private void initController() {
        //装载选修的view
        for (int i = 0; i < 5; i++) {
            TaskViewModel item = new TaskViewModel();
            if (i == 0) {
                item.mLl = mLlXx1;
                item.mTv = mTvXx1;
                item.mcb = mCbXx1;
            } else if (i == 1) {
                item.mLl = mLlXx2;
                item.mTv = mTvXx2;
                item.mcb = mCbXx2;
            } else if (i == 2) {
                item.mLl = mLlXx3;
                item.mTv = mTvXx3;
                item.mcb = mCbXx3;
            } else if (i == 3) {
                item.mLl = mLlXx4;
                item.mTv = mTvXx4;
                item.mcb = mCbXx4;
            } else if (i == 4) {
                item.mLl = mLlXx5;
                item.mTv = mTvXx5;
                item.mcb = mCbXx5;
            }
            mXxViewList.add(item);
        }

        //装载必修的view
        for (int i = 0; i < 5; i++) {
            TaskViewModel item = new TaskViewModel();
            if (i == 0) {
                item.mLl = mLlBx1;
                item.mTv = mTvBx1;
                item.mcb = mCbBx1;
            } else if (i == 1) {
                item.mLl = mLlBx2;
                item.mTv = mTvBx2;
                item.mcb = mCbBx2;
            } else if (i == 2) {
                item.mLl = mLlBx3;
                item.mTv = mTvBx3;
                item.mcb = mCbBx3;
            } else if (i == 3) {
                item.mLl = mLlBx4;
                item.mTv = mTvBx4;
                item.mcb = mCbBx4;
            } else if (i == 4) {
                item.mLl = mLlBx5;
                item.mTv = mTvBx5;
                item.mcb = mCbBx5;
            }
            mBxViewList.add(item);
        }
    }

    /**
     * 获取通关考试
     */
    private void getTaskList() {
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_TASK_LSIT, null, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                hideCommonLoading();
                Result<TaskModel> result = GsonUtils.json2Bean(s, TaskModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    data = result.getData();
                    initTask();
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage() : "数据解析错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                hideCommonLoading();
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 初始化任务列表的数据
     *
     * @param
     */
    private void initTask() {

        //必修
        for (int i = 0; i < mBxViewList.size(); i++) {
            if ((i + 1) > data.getBase().size()) {
                mBxViewList.get(i).mLl.setVisibility(View.GONE);
            } else {
                mBxViewList.get(i).mTv.setText(data.getBase().get(i).getTitle());
                if (data.getBase().get(i).getIs_pass().equals("0")) {
                    mBxViewList.get(i).mcb.setChecked(false);
                } else {
                    mBxViewList.get(i).mcb.setChecked(true);
                }
            }
        }

        //选修
        for (int i = 0; i < mXxViewList.size(); i++) {
            if ((i + 1) > data.getOptional().size()) {
                mXxViewList.get(i).mLl.setVisibility(View.GONE);
            } else {
                mXxViewList.get(i).mTv.setText(data.getOptional().get(i).getTitle());
                if (data.getOptional().get(i).getIs_pass().equals("0")) {
                    mXxViewList.get(i).mcb.setChecked(false);
                } else {
                    mXxViewList.get(i).mcb.setChecked(true);
                }
            }
        }
    }

    /**
     * 设置选修课的动态margin设置 position是第几个
     *
     * @param ll
     * @param position
     * @return
     */
    private RelativeLayout.LayoutParams getLayoutParamsTop(LinearLayout ll, int position) {
        int marginHeiht;
        int marginWidth;
        //获取控件现在的布局参数对象
        int mScreenWidth = dm.widthPixels;
        int mScreenHeigt = dm.heightPixels;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        if (position == 1) {
            marginHeiht = (mScreenHeigt * 176) / IMG_HEIGHT;
            marginWidth = (mScreenWidth * 184) / IMG_WIDTH - CommonUtils.measureLlWidth(mLlXx1) / 2 + CommonUtils.measureCheckWidth(mCbXx1) / 2;
            lp.setMargins(marginWidth, marginHeiht, 0, 0);
        } else if (position == 2) {
            marginHeiht = (mScreenHeigt * 100) / IMG_HEIGHT;
            marginWidth = (mScreenWidth * 248) / IMG_WIDTH - CommonUtils.measureLlWidth(mLlXx2) / 2 + CommonUtils.measureCheckWidth(mCbXx2) / 2;
            lp.setMargins(0, marginHeiht, marginWidth, 0);
        } else if (position == 3) {
            marginHeiht = (mScreenHeigt * 252) / IMG_HEIGHT;
            marginWidth = (mScreenWidth * 328) / IMG_WIDTH - CommonUtils.measureLlWidth(mLlXx3) / 2 + CommonUtils.measureCheckWidth(mCbXx3) / 2;
            lp.setMargins(0, marginHeiht, marginWidth, 0);
        } else if (position == 4) {
            marginHeiht = (mScreenHeigt * 368) / IMG_HEIGHT;
            marginWidth = (mScreenWidth * 218) / IMG_WIDTH - CommonUtils.measureLlWidth(mLlXx4) / 2 + CommonUtils.measureCheckWidth(mCbXx4) / 2;
            lp.setMargins(marginWidth, marginHeiht, 0, 0);
        } else if (position == 5) {
            marginHeiht = (mScreenHeigt * 312) / IMG_HEIGHT;
            marginWidth = (mScreenWidth * 158) / IMG_WIDTH - CommonUtils.measureLlWidth(mLlXx5) / 2 + CommonUtils.measureCheckWidth(mCbXx5) / 2;
            lp.setMargins(0, marginHeiht, marginWidth, 0);
        }
        return lp;
    }

    /**
     * 设置必修课margin的动态设置
     *
     * @param ll
     * @param margin
     * @return
     */
    private RelativeLayout.LayoutParams getLayoutParamsLeft(LinearLayout ll, int margin) {
        int marginWith;
        //获取控件现在的布局参数对象
        int mScreenWidth = dm.widthPixels;
        marginWith = (mScreenWidth * 336) / IMG_WIDTH;
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        lp.setMargins(marginWith, 0, 0, DeviceInfoUtil.dip2px(mContext, margin));
        return lp;
    }

    /**
     * 设置必修课margin的动态设置 向右margin
     *
     * @param ll
     * @param margin
     * @return
     */
    private RelativeLayout.LayoutParams getLayoutParamsRight(LinearLayout ll, int margin) {
        int marginWith;
        //获取控件现在的布局参数对象
        int mScreenWidth = dm.widthPixels;
        marginWith = (mScreenWidth * (IMG_WIDTH - 336)) / IMG_WIDTH - CommonUtils.measureCheckWidth(mCbBx5);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) ll.getLayoutParams();
        lp.setMargins(0, 0, marginWith, DeviceInfoUtil.dip2px(mContext, margin));
        return lp;
    }

    @OnClick({R.id.ll_xx_1, R.id.ll_xx_2, R.id.ll_xx_3, R.id.ll_xx_4, R.id.ll_xx_5, R.id.ll_bx_1, R.id.ll_bx_2, R.id.ll_bx_3, R.id.ll_bx_4, R.id.ll_bx_5, R.id.tv_record, R.id.tv_introduce})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_xx_1:
                intent = new Intent(mContext, TaskSubActivity.class);
                intent.putExtra("task_id", data.getOptional().get(0).getTask_id());
                intent.putExtra("title", data.getOptional().get(0).getTitle());
                startActivity(intent);
                break;
            case R.id.ll_xx_2:
                intent = new Intent(mContext, TaskSubActivity.class);
                intent.putExtra("task_id", data.getOptional().get(1).getTask_id());
                intent.putExtra("title", data.getOptional().get(1).getTitle());
                startActivity(intent);
                break;
            case R.id.ll_xx_3:
                intent = new Intent(mContext, TaskSubActivity.class);
                intent.putExtra("task_id", data.getOptional().get(2).getTask_id());
                intent.putExtra("title", data.getOptional().get(2).getTitle());
                startActivity(intent);
                break;
            case R.id.ll_xx_4:
                intent = new Intent(mContext, TaskSubActivity.class);
                intent.putExtra("task_id", data.getOptional().get(3).getTask_id());
                intent.putExtra("title", data.getOptional().get(3).getTitle());
                startActivity(intent);
                break;
            case R.id.ll_xx_5:
                intent = new Intent(mContext, TaskSubActivity.class);
                intent.putExtra("task_id", data.getOptional().get(4).getTask_id());
                intent.putExtra("title", data.getOptional().get(4).getTitle());
                startActivity(intent);
                break;
            case R.id.ll_bx_1:
                intent = new Intent(mContext, TaskSubActivity.class);
                intent.putExtra("task_id", data.getBase().get(0).getTask_id());
                intent.putExtra("title", data.getBase().get(0).getTitle());
                startActivity(intent);
                break;
            case R.id.ll_bx_2:
                intent = new Intent(mContext, TaskSubActivity.class);
                intent.putExtra("task_id", data.getBase().get(1).getTask_id());
                intent.putExtra("title", data.getBase().get(1).getTitle());
                startActivity(intent);
                break;
            case R.id.ll_bx_3:
                intent = new Intent(mContext, TaskSubActivity.class);
                intent.putExtra("task_id", data.getBase().get(2).getTask_id());
                intent.putExtra("title", data.getBase().get(2).getTitle());
                startActivity(intent);
                break;
            case R.id.ll_bx_4:
                intent = new Intent(mContext, TaskSubActivity.class);
                intent.putExtra("task_id", data.getBase().get(3).getTask_id());
                intent.putExtra("title", data.getBase().get(3).getTitle());
                startActivity(intent);
                break;
            case R.id.ll_bx_5:
                intent = new Intent(mContext, TaskSubActivity.class);
                intent.putExtra("task_id", data.getBase().get(4).getTask_id());
                intent.putExtra("title", data.getBase().get(4).getTitle());
                startActivity(intent);
                break;
            case R.id.tv_record://成长通关记录
                intent = new Intent(mContext, TaskRecordActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_introduce: //成长通关说明
                intent = new Intent(mContext, CommonWebActivity.class);
                intent.putExtra("title", "成长通关说明");
                intent.putExtra("url", HttpConfig.WEB_URL_BASE + HttpConfig.URL_TASK_RULE);
                startActivity(intent);
                break;
        }
    }
}
