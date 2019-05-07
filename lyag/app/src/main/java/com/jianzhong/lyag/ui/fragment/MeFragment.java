package com.jianzhong.lyag.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.baselib.widget.xlistview.XScrollView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.ui.exam.HomeExamActivity;
import com.jianzhong.lyag.ui.exam.StudyTaskActivity;
import com.jianzhong.lyag.ui.user.EditPersonInfoActivity;
import com.jianzhong.lyag.ui.user.SettingActivity;
import com.jianzhong.lyag.ui.user.cache.MyCahceActivty;
import com.jianzhong.lyag.ui.user.collect.CollectActivity;
import com.jianzhong.lyag.ui.user.history.HistoryCourseActivity;
import com.jianzhong.lyag.ui.user.message.MessageActivity;
import com.jianzhong.lyag.ui.user.studyrank.StudyRankActivity;
import com.jianzhong.lyag.ui.user.task.AssignTaskActivity;
import com.jianzhong.lyag.ui.user.task.ReceiveTaskActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 我的页面
 * Created by zhengwencheng on 2018/1/15 0015.
 * package com.jianzhong.bs.ui.fragment
 */

public class MeFragment extends BaseFragment implements XScrollView.IXScrollViewListener {

    @BindView(R.id.scroll_view)
    XScrollView mScrollView;
    private View mView; //要添加的头view
    private ViewHolder mViewHolder;

    public static MeFragment getInstance() {
        MeFragment fragment = new MeFragment();
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_me, null);
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
    public void updateUserInfo(String tag) {
        if (tag != null && tag.equals("avatar")) {
            GlideUtils.loadAvatarImage(mViewHolder.mIvAvatar, AppUserModel.getInstance().getmUserModel().getAvatar());
        }
    }

    @Override
    protected void initData() {
        super.initData();

        initView();
    }

    private void initView() {
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(this);
        mScrollView.setRefreshTime(CommonUtils.getTime());

        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.content_me, null);
            mViewHolder = new ViewHolder(mView);
            mScrollView.setView(mView);
        }

        GlideUtils.loadAvatarImage(mViewHolder.mIvAvatar, AppUserModel.getInstance().getmUserModel().getAvatar());
        mViewHolder.mTvName.setText(AppUserModel.getInstance().getmUserModel().getRealname());
        mViewHolder.mTvCredit.setText("学分 "+AppUserModel.getInstance().getmUserModel().getAc_point());
        if(AppUserModel.getInstance().getmUserModel().getExpire_at().equals("0")){
            mViewHolder.mTvTime.setText("长期会员");
        }else {
            mViewHolder.mTvTime.setText(CommonUtils.timeStampToDate(AppUserModel.getInstance().getmUserModel().getExpire_at(),"yyyy-MM-dd"));
        }
    }

    @Override
    public void onRefresh() {
        mScrollView.stopRefresh();
    }

    @Override
    public void onLoadMore() {

    }

    class ViewHolder {
        @BindView(R.id.iv_avatar)
        CircleImageView mIvAvatar;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_credit)
        TextView mTvCredit;
        @BindView(R.id.tv_time)
        TextView mTvTime;
        @BindView(R.id.tv_record)
        TextView mTvRecord;

        @OnClick({R.id.ll_personal, R.id.ll_collect, R.id.ll_send, R.id.ll_receive, R.id.ll_news, R.id.ll_group, R.id.ll_exam,
                R.id.ll_record, R.id.ll_study_task, R.id.ll_study_rank, R.id.iv_set,R.id.ll_cache})
        public void onViewClicked(View view) {
            Intent intent;
            switch (view.getId()) {
                case R.id.ll_personal: //点击编辑资料
                    intent = new Intent(mContext, EditPersonInfoActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_record:
                    intent = new Intent(mContext, HistoryCourseActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_collect: //我的收藏
                    intent = new Intent(mContext, CollectActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_send:    //我发出的学习任务
                    intent = new Intent(mContext, AssignTaskActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_receive: //我收到的学习任务
                    intent = new Intent(mContext, ReceiveTaskActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_news:      //我的消息
                    intent = new Intent(mContext, MessageActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_group:     //我的帮派

                    break;
                case R.id.ll_exam:      //成长路线
                    intent = new Intent(mContext, HomeExamActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_study_task: //学习任务
                    intent = new Intent(mContext, StudyTaskActivity.class);
                    intent.putExtra("title", "学习任务");
                    startActivity(intent);
                    break;
                case R.id.ll_study_rank: //学习排名
                    intent = new Intent(mContext, StudyRankActivity.class);
                    startActivity(intent);
                    break;
                case R.id.iv_set:
                    intent = new Intent(mContext, SettingActivity.class);
                    startActivity(intent);
                    break;
                case R.id.ll_cache:
                    intent = new Intent(mContext, MyCahceActivty.class);
                    startActivity(intent);
                    break;
            }
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

}
