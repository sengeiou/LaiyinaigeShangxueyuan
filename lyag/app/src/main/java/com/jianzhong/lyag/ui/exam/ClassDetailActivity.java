package com.jianzhong.lyag.ui.exam;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.LogUtil;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.AbFragmentPagerAdapter;
import com.jianzhong.lyag.base.BaseActivity;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.db.dao.CourseRecordDao;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnScoreListener;
import com.jianzhong.lyag.model.ClassDetailModel;
import com.jianzhong.lyag.model.CourseRecorModel;
import com.jianzhong.lyag.model.DataSource;
import com.jianzhong.lyag.model.DownbodyModel;
import com.jianzhong.lyag.model.RecordCommitModel;
import com.jianzhong.lyag.model.SectionModel;
import com.jianzhong.lyag.ui.user.history.HistoryCourseActivity;
import com.jianzhong.lyag.util.DialogHelper;
import com.jianzhong.lyag.util.DialogUtil;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.util.SystemUtlis;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.db.DownloadDBManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import chuangyuan.ycj.videolibrary.listener.ExoPlayerListener;
import chuangyuan.ycj.videolibrary.listener.OnPauseListener;
import chuangyuan.ycj.videolibrary.listener.OnPlayListener;
import chuangyuan.ycj.videolibrary.listener.TransferAudioListener;
import chuangyuan.ycj.videolibrary.listener.VideoInfoListener;
import chuangyuan.ycj.videolibrary.video.ExoUserPlayer;
import chuangyuan.ycj.videolibrary.video.GestureVideoPlayer;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;

import static com.jianzhong.lyag.util.MediaPlayerUtils.mMediaPlayer;

/**
 * 视频播放
 * Created by zhengwencheng on 2018/2/27 0027.
 * package com.jianzhong.bs.ui.exam
 */

public class ClassDetailActivity extends ToolbarActivity {
    @BindView(R.id.head_title)
    TextView mHeadTitle;
    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.ll_media)
    LinearLayout mLlMedia;
    @BindView(R.id.video_player_view)
    VideoPlayerView mVideoPlayerView;
    private String course_id;
    private String[] tabTitle = new String[]{"详情", "课件", "评论"};
    private ClassDetailModel data;
    private String path; //播放地址

    //保持屏幕常亮
    private PowerManager mPowerManager = null;
    private PowerManager.WakeLock mWakeLock = null;
    private GestureVideoPlayer mExoPlayerManager;
    //当前分段课程是是否完成
    private String curSectionId;
    private int curPosition = 0; //当前播放的分段课程
    private int initVideo = 0;   //是否是已经初始化过的播放 是的话 可以使用暂停或者重新启动
    private int isPlayDirect = 0;

    private CourseRecordDao mCourseRecordDao;
    //是否正常播放完 主要是有两段视频时 切换 第一段将结束停止 这时如果则不会正常播放完
//    private int isNormalFinish;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_class_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }


    @Override
    public void initData() {
        super.initData();

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
        }

        mCourseRecordDao = new CourseRecordDao(mContext);
        course_id = getIntent().getStringExtra("course_id");
        mHeadTitle.setText("视频播放");

        mPowerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        mWakeLock = this.mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        setVideoPlayerHeight();

        getClassDetail();
    }

    /**
     * 跟进屏幕大小重新设置播放页面高度
     */
    private void setVideoPlayerHeight() {
        /**获取屏幕宽度 设计图片的高度*/
        WindowManager wm = ((BaseActivity) mContext).getWindowManager();
        DisplayMetrics dm = new DisplayMetrics();
        wm.getDefaultDisplay().getMetrics(dm);

        //获取控件现在的布局参数对象
        int mScreenWidth = dm.widthPixels;
        ViewGroup.LayoutParams layoutParams = mLlMedia.getLayoutParams();
        //图片的分辨率为mWidth x mHeight
        layoutParams.height = (mScreenWidth * 360) / 750;
        mLlMedia.setLayoutParams(layoutParams);
    }

    /**
     * 获取课程详情
     */
    private void getClassDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("course_id", course_id); //测试 10有课程
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COURSE_DETAIL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<ClassDetailModel> result = GsonUtils.json2Bean(s, ClassDetailModel.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    data = result.getData();
                    hideCommonLoading();
                    initSection(); //初始化所有分段课程 看看有没有已经看过了的
                    initController();
                    initMedia();
                    initViewPager();
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage() : "数据解析出错");
                    finish();
                }
                isShowNoDataView(data);
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
                showErrorView();
            }
        });
    }

    /**
     * 初始化一些空间以及监听
     */
    private void initController() {
        mExoPlayerManager = new GestureVideoPlayer(this, mVideoPlayerView, new DataSource(this));

        if (!isHadAudio()) {
            mVideoPlayerView.setIvHeadesrHide(true);
        } else {
            mVideoPlayerView.setIvHeadesrHide(false);
        }

        mVideoPlayerView.getPreviewImage().setScaleType(ImageView.ScaleType.CENTER_CROP);
        GlideUtils.load(mVideoPlayerView.getPreviewImage(), data.getCover_in());
        mVideoPlayerView.setTransferAudioListener(new TransferAudioListener() {
            @Override
            public void transferAudio() {
                Intent intent = new Intent(mContext, AudioPlayActivity.class);
                intent.putExtra("data", data);
                startActivity(intent);
            }
        });

        mVideoPlayerView.setOnPlayListener(new OnPlayListener() {
            @Override
            public void onPlay() {
                if (initVideo == 0) {
                    if (SystemUtlis.isWifi(mContext)) {
                        if (mExoPlayerManager != null) {
                            mExoPlayerManager.setPosition(getCourseRecord());
                            mExoPlayerManager.startPlayer();
                        }
                    } else {
                        startPlay();
                    }
                } else {
                    if (!mExoPlayerManager.isPlaying()) {
                        mExoPlayerManager.setHandPause(false);
                        mExoPlayerManager.onResume();
                    }
                }
            }
        });

        mVideoPlayerView.setOnPauseListener(new OnPauseListener() {
            @Override
            public void onPause() {
                if (mExoPlayerManager != null) {
                    getCourseStudy(mExoPlayerManager.getCurrentPosition() / 1000 + "", "0");
                    setPlayHistory(mExoPlayerManager.getCurrentPosition());
                    mExoPlayerManager.onPause();
                    mExoPlayerManager.setHandPause(true);
                    if (mExoPlayerManager.isPlaying()) {
                        data.getSection().get(curPosition).setIsPlay(1);
                    } else {
                        data.getSection().get(curPosition).setIsPlay(0);
                    }
                    EventBus.getDefault().post(data);
                }
            }
        });

        mVideoPlayerView.setExoPlayerListener(new ExoPlayerListener() {
            @Override
            public void onCreatePlayers() {

            }

            @Override
            public void replayPlayers() {

            }

            @Override
            public void switchUri(int position) {

            }

            @Override
            public void playVideoUri() {

            }

            @Override
            public ExoUserPlayer getPlay() {
                return null;
            }

            @Override
            public void startPlayers() {

            }

            @Override
            public View.OnClickListener getClickListener() {
                return null;
            }
        });
    }

    /**
     * 初始化播放器
     */
    private void initMedia() {
        if (ListUtils.isEmpty(data.getSection()))
            return;
        initVideo = 0;
        if (!ListUtils.isEmpty(data.getSection())) {
            DownloadInfo mDownloadInfo = BaseApplication.mDownloadManager.getDownloadInfo(data.getSection().get(curPosition).getVideo_cdn_url());
            if (mDownloadInfo != null && mDownloadInfo.getState() == DownloadManager.FINISH) {
                path = mDownloadInfo.getTargetPath();
                File file = new File(path);
                if (file.exists()) {
                    mExoPlayerManager.setPlayUri(path);
                } else {
                    path = data.getSection().get(curPosition).getVideo_cdn_url();
                    mExoPlayerManager.setPlayUri(path);
                }
            } else {
                path = data.getSection().get(curPosition).getVideo_cdn_url();
                mExoPlayerManager.setPlayUri(path);
            }
            curSectionId = data.getSection().get(curPosition).getSection_id();
        }

        if (data.getSection().get(curPosition).getIs_section_finish() == 0) {
            mExoPlayerManager.setSeekBarSeek(false);
            mExoPlayerManager.setIsScrollSpeed(false);
        } else {
            mExoPlayerManager.setSeekBarSeek(true);
            mExoPlayerManager.setIsScrollSpeed(true);
        }

        mExoPlayerManager.setPosition(getCourseRecord());
        //是否直接播放
        if (isPlayDirect == 1) { //默认刚进来不直接播放 怕给别人听到
            mExoPlayerManager.startPlayer();
        }

        mExoPlayerManager.setVideoInfoListener(new VideoInfoListener() {
            @Override
            public void onPlayStart() {
                initVideo = 1;
                if (mExoPlayerManager.isPlaying()) {
                    data.getSection().get(curPosition).setIsPlay(1);
                } else {
                    data.getSection().get(curPosition).setIsPlay(0);
                }
                EventBus.getDefault().post(data);
            }

            @Override
            public void onLoadingChanged() {

            }

            @Override
            public void onPlayerError(ExoPlaybackException e) {

            }

            @Override
            public void onPlayEnd() {

                if (data.getSection().get(curPosition).getIs_section_finish() == 0) {
                    getCourseStudy(data.getDuration_sec(), "1");
                }
                LogUtil.e("onPlayEnd", "播放完结");
                setPlayHistory(0);
                data.getSection().get(curPosition).setIsPlay(0);
                EventBus.getDefault().post(data);
            }

            @Override
            public void isPlaying(boolean playWhenReady) {

            }
        });

    }

    /**
     * 分段里面是否有音频
     *
     * @return
     */
    private boolean isHadAudio() {
        if (!ListUtils.isEmpty(data.getSection())) {
            for (int i = 0; i < data.getSection().size(); i++) {
                if (!StringUtils.isEmpty(data.getSection().get(i).getAudio_cdn_url())) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 获取当前的播放记录
     *
     * @return
     */
    private long getCourseRecord() {
        long courseRecord = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("course_id", course_id);
        map.put("user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
        if (!ListUtils.isEmpty(mCourseRecordDao.queryForValue(map))) {
            for (int i = 0; i < mCourseRecordDao.queryForValue(map).size(); i++) {
                if (mCourseRecordDao.queryForValue(map).get(i).getSection_id().equals(curSectionId)) {
                    courseRecord = mCourseRecordDao.queryForValue(map).get(i).getCourse_record();
                    break;
                }
            }
        }
        return courseRecord;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchSection(SectionModel item) {
        if (item != null) {
            if (curSectionId.equals(item.getSection_id())) {
                if (mExoPlayerManager.isPlaying()) {
                    mExoPlayerManager.onPause();
                    mExoPlayerManager.setHandPause(true);
                    if (mExoPlayerManager.isPlaying()) {
                        data.getSection().get(curPosition).setIsPlay(1);
                    } else {
                        data.getSection().get(curPosition).setIsPlay(0);
                    }
                    EventBus.getDefault().post(data);
                } else {
                    mExoPlayerManager.onResume();
                }
            } else {
                //不理是否播放当前的视频 先把上一个置于停止状态
                if (mExoPlayerManager != null && mExoPlayerManager.isPlaying()) {
                    getCourseStudy(mExoPlayerManager.getCurrentPosition() / 1000 + "", "0");
                    setPlayHistory(mExoPlayerManager.getCurrentPosition());
                    mExoPlayerManager.onPause();
                }

                data.getSection().get(curPosition).setIsPlay(0);
//                EventBus.getDefault().post(data);

                for (int i = 0; i < data.getSection().size(); i++) {
                    if (item.getSection_id().equals(data.getSection().get(i).getSection_id())) {
                        curPosition = i;
                        curSectionId = item.getSection_id();
                        break;
                    }
                }

                isPlayDirect = 1; //切换的时候设置直接播放
                if (SystemUtlis.isWifi(mContext)) {
                    initMedia();
                } else {
                    startPlay();
                }
            }
        }
    }

    /**
     * 这里主要是后台数据是否完成与否分开了 两个list 好傻逼的
     */
    private void initSection() {
        if (!ListUtils.isEmpty(data.getStudy())) {
            for (int i = 0; i < data.getSection().size(); i++) {
                for (int j = 0; j < data.getStudy().size(); j++) {
                    if (data.getSection().get(i).getSection_id().equals(data.getStudy().get(j).getSection_id())) {
                        data.getSection().get(i).setIs_section_finish(1);
                    }
                }
            }
        }
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        new AlertDialog.Builder(ClassDetailActivity.this).setTitle("提示").
                setMessage("当前为非wifi网络，继续观看会消耗手机流量").
                setPositiveButton("继续", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isPlayDirect = 1;
                        initMedia();
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).create().show();
    }

    /**
     * 设置播放记录
     */
    private void setPlayHistory(long recordTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("course_id", course_id);
        map.put("user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
        if (ListUtils.isEmpty(mCourseRecordDao.queryForValue(map))) {
            CourseRecorModel item = new CourseRecorModel();
            item.setCourse_id(course_id);
            item.setSection_id(curSectionId);
            item.setCourse_record(recordTime);
            item.setUser_id(AppUserModel.getInstance().getmUserModel().getUser_id());
            mCourseRecordDao.insert(item);
        } else {
            boolean isHadInsert = false;
            for (int i = 0; i < mCourseRecordDao.queryForValue(map).size(); i++) {
                if (mCourseRecordDao.queryForValue(map).get(i).getSection_id().equals(curSectionId)) {
                    isHadInsert = true;
                    CourseRecorModel item = mCourseRecordDao.queryForValue(map).get(i);
                    item.setCourse_record(recordTime);
                    mCourseRecordDao.update(item);
                    break;
                }
            }

            if (!isHadInsert) {
                CourseRecorModel item = new CourseRecorModel();
                item.setCourse_id(course_id);
                item.setSection_id(curSectionId);
                item.setCourse_record(recordTime);
                item.setUser_id(AppUserModel.getInstance().getmUserModel().getUser_id());
                mCourseRecordDao.insert(item);
            }
        }
    }

    /**
     * 课程学习记录 第一次学习时提交
     */
    private void getCourseStudy(String duration_sec, final String is_finish) {
        Map<String, Object> parmas = new HashMap<>();
        parmas.put("section_id", curSectionId);
        parmas.put("course_id", data.getCourse_id());
        parmas.put("column_id", data.getColumn() != null ? data.getColumn().getColumn_id() : "0");
        parmas.put("is_section_finish", is_finish);
        parmas.put("study_sec", duration_sec);
        parmas.put("section_num", data.getSection().size() + "");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COURSE_STUDY, parmas, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<RecordCommitModel> result = GsonUtils.json2Bean(s, RecordCommitModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    if (is_finish.equals("1")) {
                        data.getSection().get(curPosition).setIs_section_finish(1);
                    }
                    if (result.getData() != null && !StringUtils.isEmpty(result.getData().getFinish_course_id())) {
                        if (!result.getData().getFinish_course_id().equals("0")) {
                            DialogUtil.getInstance().showCourseComment(ClassDetailActivity.this, new OnScoreListener() {
                                @Override
                                public void onScoreCallback(String score) {
                                    getCourseJudge(score);
                                }
                            });
                        }
                    }
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage() : "数据解析错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                DialogHelper.dismissDialog();
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 提交课程评价
     *
     * @param judge_star
     */
    private void getCourseJudge(String judge_star) {
        Map<String, Object> params = new HashMap<>();
        params.put("course_id", course_id);
        params.put("judge_star", judge_star);
        DialogHelper.showDialog(mContext);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COURSE_JUDGE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                DialogHelper.dismissDialog();
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    ToastUtils.show(mContext, "课程评分提交成功");
                }
            }

            @Override
            public void onFailure(String msg) {
                DialogHelper.dismissDialog();
                ToastUtils.show(mContext, msg);
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //  exoPlayerManager.onConfigurationChanged(newConfig);//横竖屏切换
        super.onConfigurationChanged(newConfig);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if(GroupVarManager.getInstance().isCheckWare == 0){
            if (mExoPlayerManager != null && data != null && mExoPlayerManager.isPlaying()) {

                if (data.getSection().get(curPosition).getIs_section_finish() == 0 && mExoPlayerManager.getCurrentPosition() != 0) {
                    getCourseStudy(mExoPlayerManager.getCurrentPosition() / 1000 + "", data.getSection().get(curPosition).getIs_section_finish() + "");
                }

                if (mExoPlayerManager.getCurrentPosition() != 0) {
                    setPlayHistory(mExoPlayerManager.getCurrentPosition());
                    LogUtil.e("activity停止", mExoPlayerManager.getCurrentPosition() + "");
                }

                if (!ListUtils.isEmpty(data.getSection())) {
                    data.getSection().get(curPosition).setIsPlay(1);
                } else {
                    if (!ListUtils.isEmpty(data.getSection())) {
                        data.getSection().get(curPosition).setIsPlay(0);
                    }
                }
                mExoPlayerManager.setHandPause(false);
                mExoPlayerManager.onPause();
            }else {
                if(mExoPlayerManager != null){
                    mExoPlayerManager.setHandPause(true);
                }
            }
            EventBus.getDefault().post(data);
        }

        this.mWakeLock.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mExoPlayerManager != null) {
            mExoPlayerManager.setHandPause(false);
            mExoPlayerManager.releasePlayers();
        }
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if (mExoPlayerManager != null && initVideo == 1 && GroupVarManager.getInstance().isCheckWare == 0) {
            mExoPlayerManager.onResume();
            mExoPlayerManager.setHandPause(false);
        }
        GroupVarManager.getInstance().isCheckWare = 0;
        this.mWakeLock.acquire();
    }

    @Override
    public void onBackPressed() {
        if (mExoPlayerManager != null && !mExoPlayerManager.onBackPressed()) {
            return;
        }
        ClassDetailActivity.this.finish();
        super.onBackPressed();
    }

    private void initViewPager() {

        ArrayList<Fragment> fragmentList = new ArrayList<>();
        fragmentList.add(ClassDetailFragment.newInstance(data));
        fragmentList.add(CoursewareFragment.newInstance(course_id));
        fragmentList.add(CommentFragment.newInstance(course_id));
        AbFragmentPagerAdapter mAbFragmentPagerAdapter = new AbFragmentPagerAdapter(getSupportFragmentManager(), fragmentList, tabTitle);
        mViewPager.setAdapter(mAbFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        //
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @OnClick({R.id.head_ll_back, R.id.iv_history, R.id.iv_download, R.id.iv_share})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.head_ll_back:
                ClassDetailActivity.this.finish();
                break;
            case R.id.iv_history:
                intent = new Intent(mContext, HistoryCourseActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_download:
                if (BaseApplication.mDownloadManager.getDownloadInfo(data.getSection().get(curPosition).getVideo_cdn_url()) != null) {
                    ToastUtils.show(mContext, "任务已经在下载列表中");
                    DownloadInfo mDownloadInfo = BaseApplication.mDownloadManager.getDownloadInfo(data.getSection().get(curPosition).getVideo_cdn_url());
                    DownbodyModel mDownbodyModel = (DownbodyModel) mDownloadInfo.getData();
                    if (!ListUtils.isEmpty(mDownbodyModel.getUserList())) {
                        boolean isHadInsert = false;
                        for (int i = 0; i < mDownbodyModel.getUserList().size(); i++) {
                            if (mDownbodyModel.getUserList().get(i).equals(AppUserModel.getInstance().getmUserModel().getUser_id())) {
                                isHadInsert = true;
                                break;
                            }
                        }

                        if (!isHadInsert) {
                            mDownbodyModel.getUserList().add(AppUserModel.getInstance().getmUserModel().getUser_id());
                            DownloadDBManager.INSTANCE.update(mDownloadInfo);
                        }
                    }
                } else {
                    DownbodyModel info = new DownbodyModel();
                    info.setType(1);
                    info.setMediaType(1);
                    info.setHead_id(data.getCourse_id());
                    info.setUrl(data.getSection().get(curPosition).getVideo_cdn_url());
                    info.setTitle(data.getTitle());
                    info.setDuration_sec(data.getDuration_sec());
                    info.setSection_id(data.getSection().get(curPosition).getSection_id());
                    info.setImg_url(data.getCover());
                    if (data.getExpert() != null) {
                        info.setName(data.getExpert().getRealname());
                        info.setPost(data.getExpert().getPos_duty());
                    }
                    List<String> userCache = new ArrayList<>();
                    userCache.add(AppUserModel.getInstance().getmUserModel().getUser_id());
                    info.setUserList(userCache);
                    GetRequest request = OkGo.get(info.getUrl())//
                            .headers("headerKey1", "headerValue1")//
                            .headers("headerKey2", "headerValue2")//
                            .params("paramKey1", "paramValue1")//
                            .params("paramKey2", "paramValue2");
                    BaseApplication.mDownloadManager.addTask(info.getUrl(), info, request, null);
                    ToastUtils.show(mContext, "已添加到下载列表中");
                }
                break;
            case R.id.iv_share:

                break;
        }
    }
}
