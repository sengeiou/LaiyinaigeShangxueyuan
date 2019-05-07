package com.jianzhong.lyag.ui.exam;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import com.baselib.util.ActivityManager;
import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.FragmentViewPageAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnItemsSelectInterface;
import com.jianzhong.lyag.model.ExaminationModel;
import com.jianzhong.lyag.util.DialogHelper;
import com.jianzhong.lyag.util.DialogUtil;
import com.jianzhong.lyag.widget.customview.NoScrollViewPager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 通关开始答题界面
 * Created by zhengwencheng on 2018/2/2 0002.
 * package com.jianzhong.bs.ui.exam
 */
public class ClearanceExamActivity extends ToolbarActivity {
    @BindView(R.id.viewpager)
    NoScrollViewPager mViewpager;
    @BindView(R.id.head_right)
    TextView mHeadRight;
    @BindView(R.id.btn_next)
    Button mBtnNext;
    private String task_id;
    private String top_task_id;
    private List<Fragment> mFragments = new ArrayList<>();

    private int curPosition;
    private static final int UPDATE_VIEWPAGER_HEIGHT = 1001;
    private static final int UPDATE_COUNT_DOWN = 1002;
    private static final int EXAM_POSt = 1003;
    private static final int FAIL_SEC = 2000;
    private static final int RIGHT_SEC = 1000;
    private int countDown = 10;
    private List<ExaminationModel> mExaminationModels = new ArrayList<>();
    private CountDownThread mCountDownThread;
    private ExamThread mExamThread;
    private int examCount;
    private AlertDialog mDialog;
    private String title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_clearance_exam);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        GroupVarManager.getInstance().mExamMap.clear();
    }

    @Override
    public void initData() {
        super.initData();
        GroupVarManager.getInstance().is_right = 0;
        GroupVarManager.getInstance().isSelectedAnswer = 0;
        task_id = getIntent().getStringExtra("task_id");
        top_task_id = getIntent().getStringExtra("top_task_id");
        title = getIntent().getStringExtra("title");
        setHeadTitle("通关考试");
        showHeadTitle();
        getTaskExamList();
        mCountDownThread = new CountDownThread();
        mExamThread = new ExamThread();
        mExamThread.start();
    }

    /**
     * 获取试题
     */
    private void getTaskExamList() {
        Map<String, Object> parasm = new HashMap<>();
        parasm.put("task_id", task_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_TASK_EXAM_START, parasm, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                hideCommonLoading();
                ResultList<ExaminationModel> resultList = GsonUtils.json2List(s, ExaminationModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    if(!ListUtils.isEmpty(resultList.getData())){
                        mExaminationModels.addAll(resultList.getData());
                    }
                    initViewPager(resultList.getData());
                } else {
                    ToastUtils.show(mContext, resultList != null ? resultList.getMessage() : "数据错误");
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
     * 提交试卷
     */
    private void confirmExam(){
        Map<String,Object> params = new HashMap<>();
        params.put("task_id",task_id);
        params.put("top_task_id",top_task_id);
        params.put("is_pass","1");
        DialogHelper.showDialog(mContext);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_TASK_PICK, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                DialogHelper.dismissDialog();
                Result result = GsonUtils.json2Bean(s,Result.class);
                if(result != null && result.getCode() == HttpConfig.STATUS_SUCCESS){
                    DialogUtil.getInstance().showExamRight(ClearanceExamActivity.this,title, new OnItemsSelectInterface() {
                        @Override
                        public void OnClick(int which) {
                            ClearanceExamActivity.this.finish();
                        }
                    }).setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                ActivityManager.getActivityManager().finishActivity(ClearanceExamActivity.class);
                                ActivityManager.getActivityManager().finishActivity(TaskReferActiVity.class);
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                }else {
                    ToastUtils.show(mContext,result != null ? result.getMessage():"数据解析错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                DialogHelper.dismissDialog();
                ToastUtils.show(mContext,msg);
            }
        });
    }

    private void initViewPager(List<ExaminationModel> data) {
        for (int i = 0; i < data.size(); i++) {
            mFragments.add(ClearanceExamFragment.newInstance(data.get(i), i,data.size()));
            countDown = Integer.valueOf(data.get(i).getReact_sec());
        }

        //ViewPager设置适配器
        FragmentViewPageAdapter mFragmentViewPageAdapter = new FragmentViewPageAdapter(getSupportFragmentManager(), mFragments);
        mViewpager.setAdapter(mFragmentViewPageAdapter);
        mViewpager.setNoScroll(true);

        if(GroupVarManager.getInstance().examIndex+1 == mExaminationModels.size()){
            mBtnNext.setText("交卷");
        }
        mViewpager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                curPosition = position;
                mViewpager.setLayoutParams(GroupVarManager.getInstance().mExamMap.get(position));
                //重新开始计时
                countDown = Integer.valueOf(mExaminationModels.get(curPosition).getReact_sec());
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getViewPagerHeight(String tag) {
        //通过setLayoutParams方式，给ViewPager动态设置高度 主要针对初始化的时候第一个Fragment调用
        if (tag.equals(AppConstants.KEY_EXAM)) {
            //重新设置高度
            Message msg = new Message();
            msg.what = UPDATE_VIEWPAGER_HEIGHT;
            handler.sendMessage(msg);
        } else if (tag.equals(AppConstants.EVENT_IS_RIGHT)) {
            if (GroupVarManager.getInstance().is_right == 0) {
                examCount = 2000;
            } else {
                examCount = 1000;
            }
        }else if(tag.equals(AppConstants.EVENT_TIME_OUT_BACK)){
            examCount = 2000;
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() { //更新UI 必须通知主线程更新
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_VIEWPAGER_HEIGHT) {
                if (GroupVarManager.getInstance().mExamMap.get(curPosition) != null) {
                    mViewpager.setLayoutParams(GroupVarManager.getInstance().mExamMap.get(curPosition));
                    hideCommonLoading();
                    setHeadRight(countDown + "秒");
                    showHeadRight();
                    mCountDownThread.start();
                }
            } else if (msg.what == UPDATE_COUNT_DOWN) { //更新倒计时
                if (countDown >= 0) {
                    setHeadRight(countDown + "秒");
                    showHeadRight();
                    countDown--;
                    if(countDown == -1 && GroupVarManager.getInstance().isSelectedAnswer == 0){ //如果等于0 并且没有选答案
                        GroupVarManager.getInstance().is_right = 0;
                        GroupVarManager.getInstance().examIndex = curPosition;
                        EventBus.getDefault().post(AppConstants.EVENT_TIME_OUT);
                    }else if(countDown == -1 && GroupVarManager.getInstance().isSelectedAnswer == 1){
                        GroupVarManager.getInstance().examIndex = curPosition;
                        EventBus.getDefault().post(AppConstants.EVENT_ANSWER);
                    }
                }
            } else if (msg.what == EXAM_POSt) { //答题后的延时
                if (GroupVarManager.getInstance().is_right == 0) {
                    DialogUtil.getInstance().showExamFail(ClearanceExamActivity.this, new OnItemsSelectInterface() {
                        @Override
                        public void OnClick(int which) {
                            ClearanceExamActivity.this.finish();
                        }
                    }).setOnKeyListener(new DialogInterface.OnKeyListener() {
                        @Override
                        public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {
                            if (keyCode == KeyEvent.KEYCODE_BACK) {
                                ClearanceExamActivity.this.finish();
                                return true;
                            } else {
                                return false;
                            }
                        }
                    });
                } else if(GroupVarManager.getInstance().is_right == 1){
                    if ((GroupVarManager.getInstance().examIndex+1) < mExaminationModels.size()) {
                        mViewpager.setCurrentItem(GroupVarManager.getInstance().examIndex+1);
                        GroupVarManager.getInstance().isSelectedAnswer = 0;
                        if(GroupVarManager.getInstance().examIndex+1 == mExaminationModels.size()-1){
                            mBtnNext.setText("交卷");
                        }
                    }else if(mBtnNext.getText().equals("交卷")){
                        confirmExam();
                    }
                }
            }
        }
    };


    /**
     * 倒计时的线程
     */
    private class CountDownThread extends Thread {
        @Override
        public void run() {
            try {
                while (countDown >= 0) {
                    Thread.sleep(1100);
                    Message msg = new Message();
                    msg.what = UPDATE_COUNT_DOWN;
                    handler.sendMessage(msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 通关的倒数计时
     */
    private class ExamThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(2000);
                    if (examCount == 1000 || examCount == 2000) {
                        examCount = 0;
                        Message msg = new Message();
                        msg.what = EXAM_POSt;
                        handler.sendMessage(msg);
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    @OnClick(R.id.btn_next)
    public void onViewClicked() { //下一题
        if (GroupVarManager.getInstance().isSelectedAnswer == 0) {
            ToastUtils.show(mContext, "请选择一个答案");
        } else {
            GroupVarManager.getInstance().examIndex = curPosition;
            EventBus.getDefault().post(AppConstants.EVENT_ANSWER);
        }
    }
}
