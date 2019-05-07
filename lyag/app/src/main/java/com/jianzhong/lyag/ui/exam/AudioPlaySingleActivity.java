package com.jianzhong.lyag.ui.exam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.baselib.widget.CustomListView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.model.DownbodyModel;
import com.jianzhong.lyag.model.SectionModel;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.util.MediaPlayerUtils;
import com.lzy.okserver.download.DownloadInfo;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jianzhong.lyag.util.MediaPlayerUtils.mMediaPlayer;

/**
 * 音频播放详情
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.ui.exam
 */
public class AudioPlaySingleActivity extends ToolbarActivity {

    @BindView(R.id.head_title)
    TextView mHeadTitle;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_sub_title)
    TextView mTvSubTitle;
    @BindView(R.id.tv_current_time)
    TextView mTvCurrentTime;
    @BindView(R.id.seek_bar)
    SeekBar mSeekBar;
    @BindView(R.id.tv_total_time)
    TextView mTvTotalTime;
    @BindView(R.id.tv_rate)
    TextView mTvRate;
    @BindView(R.id.lv_section)
    CustomListView mLvSection;
    @BindView(R.id.iv_play_type)
    ImageView mIvPlayType;
    @BindView(R.id.iv_play)
    ImageView mIvPlay;
    @BindView(R.id.ll_collect)
    LinearLayout mLlCollect;
    @BindView(R.id.iv_previous)
    ImageView mIvPrevious;
    @BindView(R.id.iv_next)
    ImageView mIvNext;
    @BindView(R.id.ll_asign)
    LinearLayout mLlAsign;


    private DownloadInfo downloadInfo;
    private String path;
    private List<SectionModel> mList = new ArrayList<>();

    private String curAudioId;
    private int curPostion = 0;
    //是否收藏要单独拿出来设置
    private String has_favor;
    private int playType = 0; //默认列表循环
    //正常情况下结束的语音播放
    private int isCompleteNormal = 1;
    //播放的速率
    private String[] mRateArr = new String[]{"1.0", "1.5", "2.0"};
    private int curRate = 0;

    private int curPlayTime = 0;

    private DownbodyModel downbodyModel;
    private String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_audio_play_single);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            mMediaPlayer.stop();
            EventBus.getDefault().post(AppConstants.TAG_CLOSE_AUDIO);
        }

        url = getIntent().getStringExtra("url");
        downloadInfo = BaseApplication.mDownloadManager.getDownloadInfo(url);
        if (downloadInfo != null && downloadInfo.getData() != null){
            downbodyModel = (DownbodyModel) downloadInfo.getData();
        }else {
            return;
        }
        mHeadTitle.setText("音频播放");
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
            EventBus.getDefault().post(AppConstants.TAG_CLOSE_AUDIO);
        }
        initController();
    }

    /**
     * 初始化控件
     */
    private void initController() {
        if (downloadInfo == null)
            return;
        hideCommonLoading();
        mTvTitle.setText(downbodyModel.getTitle());
        mTvSubTitle.setText(downbodyModel.getTitle());
        GlideUtils.load(mIvCover, downbodyModel.getImg_url());

        initAudio();
    }

    /**
     * 初始化音频播放
     */
    @SuppressLint("SetTextI18n")
    private void initAudio() {
        if (StringUtils.isEmpty(downloadInfo.getUrl()))
            return;
        //初始化数值以及标志位
        curAudioId = downbodyModel.getSection_id(); //当前播放的id
        curPlayTime = 0;    //当前播放到的位置
        path = downloadInfo.getTargetPath();
        File file = new File(path);
        if(!file.exists()){
            ToastUtils.show(mContext,"文件已经被删除了");
            finish();
        }
        mSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (mMediaPlayer != null && mMediaPlayer.isPlaying() && fromUser) {
                    mMediaPlayer.seekTo(progress);
                }
            }
        });

        mIvPlay.setBackgroundResource(R.drawable.ypbf_stop);
        mTvRate.setText(mRateArr[0] + "倍速");

        MediaPlayerUtils.getInstance().initStart(this, path, 0, new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setSeekBar();
                MediaPlayerUtils.getInstance().start();
                isCompleteNormal = 1;
                MediaPlayerUtils.getInstance().setRate(Float.valueOf(mRateArr[curRate]));
                mIvNext.setEnabled(true);
                mIvNext.setClickable(true);
                mIvPrevious.setClickable(true);
                mIvPrevious.setEnabled(true);

            }
        }, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

            }
        });
    }





    //音频课程设置进度条
    private void setSeekBar() {
        new Thread(new Runnable() {
            public void run() {
                while (MediaPlayerUtils.getInstance().getMediaPlayer() != null && MediaPlayerUtils.getInstance().getMediaPlayer().getCurrentPosition() < MediaPlayerUtils.getInstance().getMediaPlayer().getDuration()) {
                    if (MediaPlayerUtils.getInstance().getMediaPlayer().isPlaying()) {

                        final int duration = MediaPlayerUtils.getInstance().getMediaPlayer().getDuration();
                        final int currentPosition = MediaPlayerUtils.getInstance().getMediaPlayer().getCurrentPosition();
                        final String currentTime = MediaPlayerUtils.getInstance().getCurrentTime();
                        final String totalTime = MediaPlayerUtils.getInstance().getTotalTime();

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (mSeekBar != null) {
                                    mSeekBar.setMax(duration);
                                    mSeekBar.setProgress(currentPosition);
                                    mTvTotalTime.setText(totalTime);
                                    mTvCurrentTime.setText(currentTime);
                                }
                            }
                        });
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {

                    }
                }
            }
        }).start();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        if (mMediaPlayer != null) {
//            setPlayHistory(curPlayTime);
            mMediaPlayer.stop();
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

//        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
//            mIvPlay.setBackgroundResource(R.drawable.ypbf_stop);
//            MediaPlayerUtils.getInstance().start();
//            for (int i = 0; i < mList.size(); i++) {
//                if (curAudioId.equals(mList.get(i).getSection_id())) {
//                    mList.get(i).setIsPlay(1);
//                } else {
//                    mList.get(i).setIsPlay(0);
//                }
//            }
//        }
    }


    @OnClick({R.id.head_ll_back, R.id.iv_history, R.id.iv_download, R.id.iv_share, R.id.ll_collect, R.id.ll_asign, R.id.iv_play_type, R.id.iv_previous, R.id.iv_play, R.id.iv_next, R.id.tv_rate})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.head_ll_back:
                AudioPlaySingleActivity.this.finish();
                break;
            case R.id.iv_history:

                break;
            case R.id.iv_download:
                break;
            case R.id.iv_share:

                break;
            case R.id.ll_collect:
                break;
            case R.id.ll_asign:
                break;
            case R.id.iv_play_type:
                if (playType == 0) {
                    playType = 1;
                    mIvPlayType.setImageResource(R.drawable.ypbf_circulation3);
                } else {
                    playType = 0;
                    mIvPlayType.setImageResource(R.drawable.ypbf_circulation2);
                }
                break;
            case R.id.iv_previous:
                mIvPrevious.setClickable(false);
                mIvPrevious.setEnabled(false);
                isCompleteNormal = 0;
                MediaPlayerUtils.getInstance().stop();
                if (curPostion != 0) {
                    curPostion = curPostion - 1;
                } else {
                    curPostion = mList.size() - 1;
                }
                initAudio();
                break;
            case R.id.iv_play:
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    mIvPlay.setBackgroundResource(R.drawable.ypbf_play);
                    MediaPlayerUtils.getInstance().pause();
                    for (int i = 0; i < mList.size(); i++) {
                        if (curAudioId.equals(mList.get(i).getSection_id())) {
                            mList.get(i).setIsPlay(0);
                        }
                    }
                } else {
                    mIvPlay.setBackgroundResource(R.drawable.ypbf_stop);
                    MediaPlayerUtils.getInstance().start();
                    for (int i = 0; i < mList.size(); i++) {
                        if (curAudioId.equals(mList.get(i).getSection_id())) {
                            mList.get(i).setIsPlay(1);
                        } else {
                            mList.get(i).setIsPlay(0);
                        }
                    }
                }
                break;
            case R.id.iv_next:
                mIvNext.setClickable(false);
                mIvNext.setEnabled(false);
                isCompleteNormal = 0;
                MediaPlayerUtils.getInstance().stop();
                if (curPostion != (mList.size() - 1)) {
                    curPostion = curPostion + 1;
                } else {
                    curPostion = 0;
                }
                initAudio();
                break;
            case R.id.tv_rate:
                if (curRate != (mRateArr.length - 1)) {
                    curRate++;
                } else {
                    curRate = 0;
                }
                mTvRate.setText(mRateArr[curRate] + "倍速");
                MediaPlayerUtils.getInstance().setRate(Float.valueOf(mRateArr[curRate]));
                break;
        }
    }

}
