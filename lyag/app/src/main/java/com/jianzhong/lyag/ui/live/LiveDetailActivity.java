package com.jianzhong.lyag.ui.live;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PowerManager;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.DisplayMetrics;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.alivc.player.AliVcMediaPlayer;
import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.AbFragmentPagerAdapter;
import com.jianzhong.lyag.base.BaseActivity;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.db.dao.LiveRecordDao;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.DataSource;
import com.jianzhong.lyag.model.LiveDetailModel;
import com.jianzhong.lyag.model.LiveRecorModel;
import com.jianzhong.lyag.model.RecordUrlModel;
import com.jianzhong.lyag.ui.exam.AudioPlayActivity;
import com.jianzhong.lyag.ui.live.rongyun.ConversationFragment;
import com.jianzhong.lyag.util.CommonMethodLogic;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.util.SystemUtlis;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import chuangyuan.ycj.videolibrary.listener.ExoPlayerListener;
import chuangyuan.ycj.videolibrary.listener.OnPauseListener;
import chuangyuan.ycj.videolibrary.listener.OnPlayListener;
import chuangyuan.ycj.videolibrary.listener.TransferAudioListener;
import chuangyuan.ycj.videolibrary.listener.VideoInfoListener;
import chuangyuan.ycj.videolibrary.video.ExoUserPlayer;
import chuangyuan.ycj.videolibrary.video.GestureVideoPlayer;
import chuangyuan.ycj.videolibrary.widget.VideoPlayerView;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.Conversation;

import static com.jianzhong.lyag.util.MediaPlayerUtils.mMediaPlayer;

/**
 * 直播详情页面
 * Created by zhengwencheng on 2018/3/14 0014.
 * package com.jianzhong.bs.ui.live
 */

public class LiveDetailActivity extends ToolbarActivity {

    @BindView(R.id.tabLayout)
    TabLayout mTabLayout;
    @BindView(R.id.viewPager)
    ViewPager mViewPager;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_count_down)
    TextView mTvCountDown;
    @BindView(R.id.tv_live_time)
    TextView mTvLiveTime;
    @BindView(R.id.ll_advance)
    LinearLayout mLlAdvance;
    @BindView(R.id.progressBar2)
    ProgressBar mProgressBar2;
    @BindView(R.id.video_player_view)
    VideoPlayerView mVideoPlayerView;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.iv_fullscreen)
    ImageView mIvFullscreen;
    @BindView(R.id.rl_toolbar)
    RelativeLayout mRlToolbar;
    @BindView(R.id.rl_video)
    RelativeLayout mRlVideo;
    @BindView(R.id.ll_horizontal)
    LinearLayout mLlHorizontal;
    @BindView(R.id.ll_vertical)
    LinearLayout mLlVertical;
    @BindView(R.id.fl_advance)
    FrameLayout mFlAdvance;
    @BindView(R.id.surface_view)
    SurfaceView mSurfaceView;
    @BindView(R.id.ll_media)
    LinearLayout mLlMedia;

    private String live_id;
    private ArrayList<Fragment> mFragments = new ArrayList<>();
    private AbFragmentPagerAdapter mAbFragmentPagerAdapter;
    private LiveDetailModel data;

    private AliVcMediaPlayer mAliVcMediaPlayer;
    private String pullUrl;

    private ConversationFragment mSpeakerFragment;
    private ConversationFragment mInteractFragment;

    private GestureVideoPlayer mExoPlayerManager;
    private long countDown;
    private CountDownThread mCountDownThread;
    private static final int UPDATE_COUNT_DOWN = 1008;

    //当前分段课程是是否完成
    private String curSectionId;
    //当前播放的分段
    private int curPosition = 0;
    private int initVideo = 0;   //是否是已经初始化过的播放 是的话 可以使用暂停或者重新启动
    private int isPlayDirect = 0;
    private LiveRecordDao mLiveRecordDao;
    private String path; //播放地址

    //保持屏幕常亮
    private PowerManager mPowerManager = null;
    private PowerManager.WakeLock mWakeLock = null;

    private int isAddChat;
    private int isAddTalk;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.activity_live_detail);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);
    }


    @Override
    public void initData() {
        super.initData();

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            EventBus.getDefault().post(AppConstants.TAG_CLOSE_AUDIO);
        }

        mPowerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        mWakeLock = this.mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");

        mLiveRecordDao = new LiveRecordDao(mContext);
        live_id = getIntent().getStringExtra("live_id");

        setHeadTitle("直播");
        showHeadTitle();

        getLiveDetail();

    }

    private void getLiveDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("live_id", live_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_LIVE_DETAIL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<LiveDetailModel> result = GsonUtils.json2Bean(s, LiveDetailModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    data = result.getData();
                    initController();
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage() : "数据解析错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    private void initController() {
        if (data == null)
            return;
        if (data.getIs_publish().equals("0")) { //0是公告
            GlideUtils.load(mIvCover, data.getCover());
            //mTvCountDown.setText("倒计时："+ CommonUtils.secToTime());
            mTvLiveTime.setText(CommonMethodLogic.getLiveStartAt(data.getStart_at()) + "直播");
            mFlAdvance.setVisibility(View.VISIBLE);
            mRlVideo.setVisibility(View.GONE);
            countDown = getLiveDownCount();
            setCountDownTime(mTvCountDown);
            mCountDownThread = new CountDownThread();
            mCountDownThread.start();
        } else if (data.getIs_publish().equals("1") && data.getIs_finish().equals("0") && data.getExpert() != null && !data.getExpert().getUser_id().equals(AppUserModel.getInstance().getmUserModel().getUser_id())) {
            mFlAdvance.setVisibility(View.GONE);
            mRlVideo.setVisibility(View.GONE);
            mLlMedia.setVisibility(View.VISIBLE);
            initVideoLive();
        } else if (data.getIs_publish().equals("1") && data.getIs_finish().equals("1")) {
            mFlAdvance.setVisibility(View.GONE);
            mRlVideo.setVisibility(View.GONE);
            mLlMedia.setVisibility(View.VISIBLE);

            setVideoPlayerHeight();
            initSection(); //初始化所有分段课程 看看有没有已经看过了的
            initPlayer();
            initMedia();
        }else if(data.getIs_publish().equals("1") && data.getIs_finish().equals("0") && data.getExpert() != null && data.getExpert().getUser_id().equals(AppUserModel.getInstance().getmUserModel().getUser_id())){
            GlideUtils.load(mIvCover, data.getCover_in());
            mFlAdvance.setVisibility(View.VISIBLE);
            mRlVideo.setVisibility(View.GONE);
        }
        initViewPager();
    }

    /**
     * 初始化回放的视频播放器
     */
    private void initPlayer() {
        mExoPlayerManager = new GestureVideoPlayer(this, mVideoPlayerView, new DataSource(this));
        mVideoPlayerView.setIvHeadesrHide(true);
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
                if(data.getIs_publish().equals("1") && data.getIs_finish().equals("1")){
                    if (initVideo == 0) {
                        if (SystemUtlis.isWifi(mContext)) {
                            if (mExoPlayerManager != null) {
                                mExoPlayerManager.setPosition(getLiveRecord());
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
            }
        });

        mVideoPlayerView.setOnPauseListener(new OnPauseListener() {
            @Override
            public void onPause() {
                if (mExoPlayerManager != null) {
                    data.getRecord_url().get(curPosition).setIsPlay(0);
                    mExoPlayerManager.onPause();
                    mExoPlayerManager.setHandPause(true);
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
        if (ListUtils.isEmpty(data.getRecord_url()))
            return;
        initVideo = 0;
        if (!ListUtils.isEmpty(data.getRecord_url())) {
            path = data.getRecord_url().get(curPosition).getVideo_url();
            mExoPlayerManager.setPlayUri(data.getRecord_url().get(curPosition).getVideo_cdn_url());
            curSectionId = data.getRecord_url().get(curPosition).getSection_id();
        }

        if (data.getRecord_url().get(curPosition).getIs_section_finish() == 0) {
            mExoPlayerManager.setSeekBarSeek(false);
        } else {
            mExoPlayerManager.setSeekBarSeek(false);
        }

        mExoPlayerManager.setPosition(getLiveRecord());
        //是否直接播放
        if (isPlayDirect == 1) { //默认刚进来不直接播放 怕给别人听到
            mExoPlayerManager.startPlayer();
        }

        mExoPlayerManager.setVideoInfoListener(new VideoInfoListener() {
            @Override
            public void onPlayStart() {
                initVideo = 1;
                if (mExoPlayerManager.isPlaying()) {
                    data.getRecord_url().get(curPosition).setIsPlay(1);
                } else {
                    data.getRecord_url().get(curPosition).setIsPlay(0);
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
                if (data.getRecord_url().get(curPosition).getIs_section_finish() == 0) {
                    getLiveFinish();
                }

                if (!ListUtils.isEmpty(data.getRecord_url())) {
                    data.getRecord_url().get(curPosition).setIsPlay(0);
                }
                setPlayHistory(0);
                EventBus.getDefault().post(data);
            }

            @Override
            public void isPlaying(boolean playWhenReady) {

            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        if(mExoPlayerManager != null && initVideo == 1){
            mExoPlayerManager.onResume();
            mExoPlayerManager.setHandPause(false);
        }
        this.mWakeLock.acquire();
    }


    @Override
    public void onBackPressed() {
        if (mExoPlayerManager != null && !mExoPlayerManager.onBackPressed()) {
            return;
        }
        if (mExoPlayerManager != null && data.getIs_publish().equals("1") && data.getIs_finish().equals("1")) {
            setPlayHistory(mExoPlayerManager.getCurrentPosition());
        }
        LiveDetailActivity.this.finish();
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mExoPlayerManager != null && data != null && mExoPlayerManager.isPlaying()) {
            if(data.getIs_publish().equals("1") && data.getIs_finish().equals("1")){
                setPlayHistory(mExoPlayerManager.getCurrentPosition());
            }

            if (!ListUtils.isEmpty(data.getRecord_url())) {
                data.getRecord_url().get(curPosition).setIsPlay(0);
            }
            EventBus.getDefault().post(data);
            mExoPlayerManager.setHandPause(false);
            mExoPlayerManager.onPause();
        }else {
            if(mExoPlayerManager != null){
                mExoPlayerManager.setHandPause(true);
            }
        }
        this.mWakeLock.release();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mExoPlayerManager != null){
            mExoPlayerManager.setHandPause(false);
            mExoPlayerManager.releasePlayers();
        }
        EventBus.getDefault().unregister(this);
    }


    private void getLiveFinish(){
        Map<String,Object> params = new HashMap<>();
        params.put("live_id",live_id);
        params.put("is_record","1");
        params.put("is_edit",data.getRecord_url().get(curPosition).getIs_edit());
        params.put("section_id",curSectionId);
        params.put("section_num",data.getRecord_url().size()+"");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_LIVE_FINISH, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s,Result.class);
                if(result != null && result.getCode() == HttpConfig.STATUS_SUCCESS){
                    ToastUtils.show(mContext, result.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    /**
     * 获取当前的播放记录
     *
     * @return
     */
    private long getLiveRecord() {
        long courseRecord = 0;
        if(!ListUtils.isEmpty(data.getRecord_url())){
            if (data.getRecord_url().get(curPosition).getIs_section_finish() == 1) { //已完成
                Map<String, Object> map = new HashMap<>();
                map.put("live_id", live_id);
                map.put("user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
                if (!ListUtils.isEmpty(mLiveRecordDao.queryForValue(map))) {
                    for (int i = 0; i < mLiveRecordDao.queryForValue(map).size(); i++) {
                        if (mLiveRecordDao.queryForValue(map).get(i).getSection_id().equals(curSectionId)) {
                            courseRecord = mLiveRecordDao.queryForValue(map).get(i).getCourse_record();
                            break;
                        }
                    }
                }
            }
        }
        return courseRecord;
    }


    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchLiveSection(RecordUrlModel item) {
        if (item != null) {
            if (curSectionId.equals(item.getSection_id())) {
                if (mExoPlayerManager.isPlaying()) {
                    mExoPlayerManager.onPause();
                    mExoPlayerManager.setHandPause(true);
                    if (mExoPlayerManager.isPlaying()) {
                        data.getRecord_url().get(curPosition).setIsPlay(1);
                    } else {
                        data.getRecord_url().get(curPosition).setIsPlay(0);
                    }
                    EventBus.getDefault().post(data);
                } else {
                    mExoPlayerManager.onResume();
                }
            } else {
                //不理是否播放当前的视频 先把上一个置于停止状态
                if (mExoPlayerManager != null) {
                    setPlayHistory(mExoPlayerManager.getCurrentPosition());
                }
                data.getRecord_url().get(curPosition).setIsPlay(0);
                EventBus.getDefault().post(data);

                for (int i = 0; i < data.getRecord_url().size(); i++) {
                    if (item.getSection_id().equals(data.getRecord_url().get(i).getSection_id())) {
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
            for (int i = 0; i < data.getRecord_url().size(); i++) {
                for (int j = 0; j < data.getStudy().size(); j++) {
                    if (data.getRecord_url().get(i).getSection_id().equals(data.getStudy().get(j).getSection_id())) {
                        data.getRecord_url().get(i).setIs_section_finish(1);
                    }
                }
            }
        }
    }

    /**
     * 开始播放
     */
    private void startPlay() {
        new AlertDialog.Builder(LiveDetailActivity.this).setTitle("提示").
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
        map.put("live_id", live_id);
        map.put("user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
        if (ListUtils.isEmpty(mLiveRecordDao.queryForValue(map))) {
            LiveRecorModel item = new LiveRecorModel();
            item.setLive_id(live_id);
            item.setSection_id(curSectionId);
            item.setCourse_record(recordTime);
            item.setUser_id(AppUserModel.getInstance().getmUserModel().getUser_id());
            mLiveRecordDao.insert(item);
        } else {
            boolean isHadInsert = true;
            for (int i = 0; i < mLiveRecordDao.queryForValue(map).size(); i++) {
                if(!ListUtils.isEmpty(mLiveRecordDao.queryForValue(map)) && !StringUtils.isEmpty(curSectionId)){
                    if (mLiveRecordDao.queryForValue(map).get(i).getSection_id().equals(curSectionId)) {
                        isHadInsert = false;
                        LiveRecorModel item = mLiveRecordDao.queryForValue(map).get(i);
                        item.setCourse_record(recordTime);
                        mLiveRecordDao.update(item);
                        break;
                    }
                }
            }

            if (!isHadInsert) {
                LiveRecorModel item = new LiveRecorModel();
                item.setLive_id(live_id);
                item.setSection_id(curSectionId);
                item.setCourse_record(recordTime);
                item.setUser_id(AppUserModel.getInstance().getmUserModel().getUser_id());
                mLiveRecordDao.insert(item);
            }
        }
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
     * 获取距离直播还有多少时间
     *
     * @return
     */
    private long getLiveDownCount() {
        SimpleDateFormat mSimpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String nowTime = mSimpleDateFormat.format(date);
        String liveTime = CommonUtils.timeStampToDate(data.getStart_at(), "yyyy-MM-dd HH:mm:ss");

        try {
            return (mSimpleDateFormat.parse(liveTime).getTime() - mSimpleDateFormat.parse(nowTime).getTime()) / 1000;
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return 0;
    }

    /**
     * 倒计时的线程
     */
    private class CountDownThread extends Thread {
        @Override
        public void run() {
            try {
                while (countDown > 0) {
                    Thread.sleep(1000);
                    countDown--;
                    Message msg = new Message();
                    msg.what = UPDATE_COUNT_DOWN;
                    handler.sendMessage(msg);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() { //更新UI 必须通知主线程更新
        @SuppressLint("SetTextI18n")
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_COUNT_DOWN) {
                setCountDownTime(mTvCountDown);
            }
        }
    };

    @SuppressLint("SetTextI18n")
    private void setCountDownTime(TextView tv) {
        if (tv != null) {
            if (countDown >= 0 && countDown < 60) {
                tv.setText("倒计时：" + countDown + "秒");
            } else if (countDown >= 60 && countDown < 3600) {
                tv.setText("倒计时：" + countDown / 60 + "分钟" + (countDown % 60 != 0 ? countDown % 60 + "秒" : ""));
            } else if (countDown >= 3600 && countDown < 86400) {
                tv.setText("倒计时：" + countDown / 3600 + "小时" + (countDown % 3600 / 60 != 0 ? countDown % 3600 / 60 + "分钟" : "") + (countDown % 3600 % 60 != 0 ? countDown % 3600 % 60 + "秒" : ""));
            } else {
                tv.setText("倒计时：" + countDown / 86400 + "天" + (countDown % 86400 / 3600 != 0 ? countDown % 86400 / 3600 + "小时" : "")
                        + (countDown % 86400 % 3600 / 60 != 0 ? countDown % 86400 % 3600 / 60 + "分钟" : "")
                        + (countDown % 86400 % 3600 % 60 != 0 ? countDown % 86400 % 3600 % 60 + "秒" : ""));
            }
        }
    }


    /**
     * 初始化viewpager
     */
    private void initViewPager() {
        if (data == null)
            return;

        //设置是否为主讲人
        if (!ListUtils.isEmpty(data.getDocent())) {
            for (int i = 0; i < data.getDocent().size(); i++) {
                if (data.getDocent().get(i).getUser_id().equals(AppUserModel.getInstance().getmUserModel().getUser_id())) {
                    mSpeakerFragment = ConversationFragment.newInstance(0);
                    Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                            .appendPath("conversation").appendPath(Conversation.ConversationType.CHATROOM.getName().toLowerCase(Locale.US))
                            .appendQueryParameter("targetId", data.getLive_talk_room()).build();
                    mSpeakerFragment.setUri(uri);
                    break;
                }
            }
        }

        //主播那个傻逼是自己
        if(data.getExpert() != null && data.getExpert().getUser_id().equals(AppUserModel.getInstance().getmUserModel().getUser_id()) && mSpeakerFragment== null){
            mSpeakerFragment = ConversationFragment.newInstance(0);
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversation").appendPath(Conversation.ConversationType.CHATROOM.getName().toLowerCase(Locale.US))
                    .appendQueryParameter("targetId", data.getLive_talk_room()).build();
            mSpeakerFragment.setUri(uri);
        }

        //若依旧null 则不是主讲人
        if (mSpeakerFragment == null) {
            mSpeakerFragment = ConversationFragment.newInstance(1);
            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                    .appendPath("conversation").appendPath(Conversation.ConversationType.CHATROOM.getName().toLowerCase(Locale.US))
                    .appendQueryParameter("targetId", data.getLive_talk_room()).build();
            mSpeakerFragment.setUri(uri);
        }

        //初始化互动聊天室
        mInteractFragment = ConversationFragment.newInstance(0);
        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
                .appendPath("conversation").appendPath(Conversation.ConversationType.CHATROOM.getName().toLowerCase(Locale.US))
                .appendQueryParameter("targetId", data.getLive_chat_room()).build();
        mInteractFragment.setUri(uri);

        final String[] tabTitle = new String[]{"详情", "主讲", "互动"};
        mFragments.add(LiveDetailFragment.newInstance(data));
        mFragments.add(mSpeakerFragment);
        mFragments.add(mInteractFragment);

        mAbFragmentPagerAdapter = new AbFragmentPagerAdapter(getSupportFragmentManager(), mFragments, tabTitle);
        mViewPager.setAdapter(mAbFragmentPagerAdapter);
        mTabLayout.setupWithViewPager(mViewPager);
        //
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 1) {
                    quitChatRoom();
                } else if (position == 2) {
                    quitTalkToom();
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    public void addTalkRoom() {
        if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) && !TextUtils.isEmpty(data.getLive_talk_room())) {


            RongIM.getInstance().joinChatRoom(data.getLive_talk_room(), 50, new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    public void addChatRoom() {
        if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) && !TextUtils.isEmpty(data.getLive_chat_room())) {
            RongIM.getInstance().joinChatRoom(data.getLive_chat_room(), 50, new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {

                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    public void quitTalkToom() {
        if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) && !TextUtils.isEmpty(data.getLive_talk_room())) {
            RongIM.getInstance().quitChatRoom(data.getLive_talk_room(), new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {
                    if(isAddChat == 1){
                        EventBus.getDefault().post(AppConstants.TAG_CLEAR_MSG);
                    }

                    addChatRoom();
                    isAddChat = 1;
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    public void quitChatRoom() {
        if (RongIM.getInstance().getCurrentConnectionStatus().equals(RongIMClient.ConnectionStatusListener.ConnectionStatus.CONNECTED) && !TextUtils.isEmpty(data.getLive_chat_room())) {
            RongIM.getInstance().quitChatRoom(data.getLive_chat_room(), new RongIMClient.OperationCallback() {
                @Override
                public void onSuccess() {
                    if(isAddTalk == 1){
                        EventBus.getDefault().post(AppConstants.TAG_CLEAR_MSG);
                    }
                    addTalkRoom();
                    isAddTalk =1;
                }

                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }

    /**
     * 初始化视频直播
     */
    private void initVideoLive() {
        mVideoPlayerView.setIvHeadesrHide(true);
//        mVideoPlayerView.getPreviewImage().setScaleType(ImageView.ScaleType.CENTER_CROP);
        mExoPlayerManager = new GestureVideoPlayer(this, mVideoPlayerView, new DataSource(this));
        mExoPlayerManager.hideSeekBar();
        mExoPlayerManager.setIsLive(true);
//        pullUrl = "http://live.lovica.com.cn/lovica/Stream3.flv?auth_key=1521461416-0-0-955981a5c1c39a9beb580fd39edfdec3";
        mExoPlayerManager.setPlayUri(data.getPull_url());
//        开始播放
        mExoPlayerManager.startPlayer();

        mVideoPlayerView.setOnPlayListener(new OnPlayListener() {
            @Override
            public void onPlay() {
                if (mExoPlayerManager != null) {
                    mExoPlayerManager.setHandPause(false);
                    mExoPlayerManager.onResume();
                }
            }
        });

        mVideoPlayerView.setOnPauseListener(new OnPauseListener() {
            @Override
            public void onPause() {
                if (mExoPlayerManager != null) {
                    mExoPlayerManager.onPause();
                    mExoPlayerManager.setHandPause(true);
                }
            }
        });

        mExoPlayerManager.setVideoInfoListener(new VideoInfoListener() {
            @Override
            public void onPlayStart() {

            }

            @Override
            public void onLoadingChanged() {

            }

            @Override
            public void onPlayerError(ExoPlaybackException e) {
                GlideUtils.load(mVideoPlayerView.getPreviewImage(), data.getCover_in());
                ToastUtils.show(mContext,"主播正在赶来的路上...");
            }

            @Override
            public void onPlayEnd() {

            }

            @Override
            public void isPlaying(boolean playWhenReady) {

            }
        });
    }

    public int getWidth(Context mContext) {
        DisplayMetrics dm = new DisplayMetrics();
        ((Activity) mContext).getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenWidth = dm.widthPixels;
        return screenWidth;
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        //  exoPlayerManager.onConfigurationChanged(newConfig);//横竖屏切换
        super.onConfigurationChanged(newConfig);
    }
}
