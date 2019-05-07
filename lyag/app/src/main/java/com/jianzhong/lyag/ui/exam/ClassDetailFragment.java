package com.jianzhong.lyag.ui.exam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.ResultList;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.baselib.widget.CustomListView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.AboutClassAdapter;
import com.jianzhong.lyag.adapter.AvatarAdapter;
import com.jianzhong.lyag.adapter.SectionAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.ClassDetailModel;
import com.jianzhong.lyag.model.ClassListModel;
import com.jianzhong.lyag.model.SectionModel;
import com.jianzhong.lyag.model.UserModel;
import com.jianzhong.lyag.ui.organization.AssignStudyActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.OnClick;
/**
 * 课程详情fragment
 * Created by zhengwencheng on 2018/2/27 0027.
 * package com.jianzhong.bs.ui.exam
 */
public class ClassDetailFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_identify)
    TextView mTvIdentify;
    @BindView(R.id.ll_blank)
    LinearLayout mLlBlank;
    @BindView(R.id.ll_score)
    LinearLayout mLlScore;
    @BindView(R.id.tv_score)
    TextView mTvScore;
    @BindView(R.id.text_summary)
    TextView mTextSummary;
    @BindView(R.id.iv_spread)
    ImageView mIvSpread;
    @BindView(R.id.iv_shrink_up)
    ImageView mIvShrinkUp;
    @BindView(R.id.lv_section)
    CustomListView mLvSection;
    @BindView(R.id.lv_about)
    CustomListView mLvAbout;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.iv_score)
    ImageView mIvScore;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_collect)
    LinearLayout mLlCollect;
    @BindView(R.id.ll_about_class)
    LinearLayout mLlAboutClass;
    @BindView(R.id.ll_asign)
    LinearLayout mLlAsign;
    @BindView(R.id.ll_learned)
    LinearLayout mLlLearned;

    private static final int SHRINK_UP_STATE = 1; // 收起状态
    private static final int SPREAD_STATE = 2; // 展开状态
    private static int mState = SPREAD_STATE;//默认收起状态
    private static final int VIDEO_CONTENT_DESC_MAX_LINE = 2;// 默认展示最大行数2行
    private static final int SCORE_IMG_HEIGHT = 57;
    private static final int SCORE_IMG_MARGIN = 8;
    @BindView(R.id.fl_score)
    FrameLayout mFlScore;

    private SectionAdapter mSectionAdapter; //分段播放列表的适配器
    private List<SectionModel> mList = new ArrayList<>();
    private ClassDetailModel data;
    //相关课程列表
    private List<ClassListModel> mAboutClassModels = new ArrayList<>();
    private AboutClassAdapter mAboutClassAdapter;
    //已学习人员列表
    private List<UserModel> mUserModels = new ArrayList<>();
    private AvatarAdapter mAvatarAdapter;

    public static ClassDetailFragment newInstance(ClassDetailModel data) {
        Bundle args = new Bundle();
        ClassDetailFragment fragment = new ClassDetailFragment();
        args.putSerializable("data", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_class_detail, null);
        return mRootView;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSection(ClassDetailModel data) {
        if (data != null && !ListUtils.isEmpty(data.getSection())) {
            mList.clear();
            mList.addAll(data.getSection());
            mSectionAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void initData() {
        super.initData();

        data = (ClassDetailModel) getArguments().getSerializable("data");
        //
        initContoller();
    }

    /**
     * 初始化控件赋值
     */
    @SuppressLint("SetTextI18n")
    private void initContoller() {
        mLvSection.setFocusable(false);
        mLvAbout.setFocusable(false);
        mTvTitle.setText(data.getTitle());
        if (data.getExpert() != null) {
            mTvIdentify.setText(data.getExpert().getRealname() + "/" + data.getExpert().getPos_duty());
        }
        mTvScore.setText(data.getJudge_star() + "分");
        mTextSummary.setText(data.getSummary());
        if (data.getHas_favor().equals("0")) {
            mLlCollect.setSelected(false);
        } else {
            mLlCollect.setSelected(true);
        }
        //设置评分的长度
        int mScoreWith;
        if (!StringUtils.isEmpty(data.getJudge_star())) {
            try {
                mFlScore.measure(0, 0);
                int with = mFlScore.getMeasuredWidth();
                mScoreWith = (int) ((with * Double.valueOf(data.getJudge_star())) / 10);
                mLlScore.setLayoutParams(new FrameLayout.LayoutParams(mScoreWith, ViewGroup.LayoutParams.WRAP_CONTENT));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        //判断是是否有指派权限
        if (AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion() != null &&
                !ListUtils.isEmpty(AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion().getOperation())) {
            for (int i = 0; i < AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion().getOperation().size(); i++) {
                if (AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion().getOperation().get(i).equals(AppConstants.TAG_DICTATE_SEND)) {
                    mLlAsign.setVisibility(View.VISIBLE);
                    break;
                }
            }
        }

        //分段播放课程
        if (!ListUtils.isEmpty(data.getSection())) {
            mList.addAll(data.getSection());
            mSectionAdapter = new SectionAdapter(mContext, mList, data.getCover(), data.getExpert());
            mLvSection.setAdapter(mSectionAdapter);
        } else {

        }
        //获取已学习人员
        getHadStudy();
        //获取相关课程列表
        getAboutClass();
    }

    /**
     * 获取相关课程列表
     */
    private void getAboutClass() {
        Map<String, Object> params = new HashMap<>();
        params.put("course_id", data.getCourse_id());
        params.put("expert_user_id", data.getExpert_user_id());
        params.put("cat_id", "");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COURSE_RELATE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<ClassListModel> resultList = GsonUtils.json2List(s, ClassListModel.class);
                if (resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    if (!ListUtils.isEmpty(resultList.getData())) {
                        mAboutClassModels.clear();
                        mAboutClassModels.addAll(resultList.getData());
                        if (mAboutClassAdapter == null) {
                            mAboutClassAdapter = new AboutClassAdapter(mContext, mAboutClassModels);
                            mLvAbout.setAdapter(mAboutClassAdapter);
                        } else {
                            mAboutClassAdapter.notifyDataSetChanged();
                        }
                    } else {
                        mLlAboutClass.setVisibility(View.GONE);
                    }
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

    /**
     * 获取已学习成员
     */
    private void getHadStudy() {
        Map<String, Object> params = new HashMap<>();
        params.put("course_id", data.getCourse_id());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_HAS_STUDY_USER, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<UserModel> resultList = GsonUtils.json2List(s, UserModel.class);
                if (resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    mUserModels.addAll(resultList.getData());
                    LinearLayoutManager ms = new LinearLayoutManager(getActivity());
                    ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
                    mRecyclerView.setLayoutManager(ms);               //给RecyClerView 添加设置好的布局样式
                    mAvatarAdapter = new AvatarAdapter(mContext, mUserModels);
                    mRecyclerView.setAdapter(mAvatarAdapter);
                    if (!ListUtils.isEmpty(resultList.getData())) {
                        mLlLearned.setVisibility(View.VISIBLE);
                    } else {
                        mLlLearned.setVisibility(View.GONE);
                    }
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

    @OnClick({R.id.ll_status, R.id.tv_change, R.id.iv_all, R.id.ll_collect, R.id.ll_asign})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.ll_status:
                if (mState == SPREAD_STATE) {
                    mTextSummary.setMaxLines(Integer.MAX_VALUE);
                    mTextSummary.requestLayout();
                    mIvShrinkUp.setVisibility(View.VISIBLE);
                    mIvSpread.setVisibility(View.GONE);
                    mTvStatus.setText("收起");
                    mState = SHRINK_UP_STATE;
                } else if (mState == SHRINK_UP_STATE) {
                    mTextSummary.setMaxLines(VIDEO_CONTENT_DESC_MAX_LINE);
                    mTextSummary.requestLayout();
                    mIvShrinkUp.setVisibility(View.GONE);
                    mIvSpread.setVisibility(View.VISIBLE);
                    mTvStatus.setText("展开");
                    mState = SPREAD_STATE;
                }
                break;
            case R.id.tv_change:
                getAboutClass();
                break;

            case R.id.iv_all: //查看全部已学习成员
                intent = new Intent(mContext, MemberConActivity.class);
                intent.putExtra("title", "已学习的同学");
                intent.putExtra("course_id", data.getCourse_id());
                startActivity(intent);
                break;
            case R.id.ll_collect: //收藏
                if (data.getHas_favor().equals("0")) {
                    addCollect();
                } else {
                    delCollect();
                }
                break;
            case R.id.ll_asign: //指派学习
                intent = new Intent(mContext, AssignStudyActivity.class);
                intent.putExtra("foreign_id", data.getCourse_id());
                intent.putExtra("asset_type", data.getAsset_type());
                startActivity(intent);
                break;

        }
    }

    /**
     * 添加收藏
     */
    private void addCollect() {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", data.getAsset_type());
        params.put("foreign_id", data.getCourse_id());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_ADD_FAVOR, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<String> result = GsonUtils.json2Bean(s, String.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    ToastUtils.show(mContext, result.getMessage());
                    data.setHas_favor("1");
                    mLlCollect.setSelected(true);
                } else {
                    ToastUtils.show(mContext, result.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 删除收藏
     */
    private void delCollect() {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", data.getAsset_type());
        params.put("foreign_id", data.getCourse_id());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DEL_FAVOR, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<String> result = GsonUtils.json2Bean(s, String.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    ToastUtils.show(mContext, result.getMessage());
                    data.setHas_favor("0");
                    mLlCollect.setSelected(false);
                } else {
                    ToastUtils.show(mContext, result.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }
}
