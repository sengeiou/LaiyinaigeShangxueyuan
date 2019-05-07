package com.jianzhong.lyag.ui.exam;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.util.ToastUtils;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.BaseActivity;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.db.dao.CourseRecordDao;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.model.DataSource;
import com.jianzhong.lyag.model.DownbodyModel;
import com.jianzhong.lyag.util.GlideUtils;
import com.lzy.okserver.download.DownloadInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.File;

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
public class ClassDeatailSingleActivity extends ToolbarActivity {
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
    private String[] tabTitle = new String[]{"详情", "课件", "评论"};
    private DownloadInfo data;
    private String path; //播放地址

    //保持屏幕常亮
    private PowerManager mPowerManager = null;
    private PowerManager.WakeLock mWakeLock = null;
    private GestureVideoPlayer mExoPlayerManager;
    //当前分段课程是是否完成
    private String curSectionId;
    private int curPosition = 0; //当前播放的分段课程
    private int initVideo = 0;   //是否是已经初始化过的播放 是的话 可以使用暂停或者重新启动

    private CourseRecordDao mCourseRecordDao;
    private String course_id;
    private String url;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_class_detail_single);
        ButterKnife.bind(this);
    }


    @Override
    public void initData() {
        super.initData();

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            EventBus.getDefault().post(AppConstants.TAG_CLOSE_AUDIO);
        }

        mCourseRecordDao = new CourseRecordDao(mContext);
        course_id = getIntent().getStringExtra("course_id");
        url = getIntent().getStringExtra("url");
        data = BaseApplication.mDownloadManager.getDownloadInfo(url);

        mHeadTitle.setText("视频播放");
        mPowerManager = (PowerManager) this.getSystemService(Context.POWER_SERVICE);
        mWakeLock = this.mPowerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");
        setVideoPlayerHeight();

        initController();

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
     * 初始化一些空间以及监听
     */
    private void initController() {
        if (data == null) {
            return;
        }
        hideCommonLoading();
        mExoPlayerManager = new GestureVideoPlayer(this, mVideoPlayerView, new DataSource(this));
        mVideoPlayerView.setIvHeadesrHide(true);
        mVideoPlayerView.getPreviewImage().setScaleType(ImageView.ScaleType.CENTER_CROP);
        DownbodyModel info = (DownbodyModel) data.getData();
        GlideUtils.load(mVideoPlayerView.getPreviewImage(), info.getImg_url());
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
                    if (mExoPlayerManager != null) {
                        mExoPlayerManager.startPlayer();
                    }
                } else {
                    if (!mExoPlayerManager.isPlaying()) {
                        mExoPlayerManager.onResume();
                    }
                }
            }
        });

        mVideoPlayerView.setOnPauseListener(new OnPauseListener() {
            @Override
            public void onPause() {
                if (mExoPlayerManager != null) {
                    mExoPlayerManager.onPause();
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

        initMedia();
    }

    /**
     * 初始化播放器
     */
    private void initMedia() {

        initVideo = 0;
        path = data.getTargetPath();
        File file = new File(path);
        if(file.exists()){
            mExoPlayerManager.setPlayUri(path);
        }else {
            ToastUtils.show(mContext,"下载视频已经删除");
            finish();
        }
        mExoPlayerManager.setSeekBarSeek(true);
        //是否直接播放
        mExoPlayerManager.startPlayer();
        mExoPlayerManager.setVideoInfoListener(new VideoInfoListener() {
            @Override
            public void onPlayStart() {
                initVideo = 1;
            }

            @Override
            public void onLoadingChanged() {
            }

            @Override
            public void onPlayerError(ExoPlaybackException e) {
            }

            @Override
            public void onPlayEnd() {

            }

            @Override
            public void isPlaying(boolean playWhenReady) {
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
        mExoPlayerManager.onPause();
        this.mWakeLock.release();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mExoPlayerManager != null) {
            mExoPlayerManager.onResume();
        }
        this.mWakeLock.acquire(10*60*1000L /*10 minutes*/);
    }

    @Override
    public void onBackPressed() {
        if (mExoPlayerManager != null && !mExoPlayerManager.onBackPressed()) {
            return;
        }
        ClassDeatailSingleActivity.this.finish();
        super.onBackPressed();
    }


    @OnClick({R.id.head_ll_back, R.id.iv_history, R.id.iv_download, R.id.iv_share})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.head_ll_back:
                ClassDeatailSingleActivity.this.finish();
                break;
            case R.id.iv_history:
                break;
            case R.id.iv_download:

                break;
            case R.id.iv_share:

                break;
        }
    }
}
