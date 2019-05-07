package com.jianzhong.lyag.ui.exam;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.ResultList;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.baselib.widget.CustomListView;
import com.jianzhong.lyag.CommonWebActivity;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.AudioAboutAdapter;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.db.dao.AudioRecordDao;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnAudioListener;
import com.jianzhong.lyag.model.AudioDetailModel;
import com.jianzhong.lyag.model.AudioFinishModel;
import com.jianzhong.lyag.model.AudioRecorModel;
import com.jianzhong.lyag.model.DownbodyModel;
import com.jianzhong.lyag.ui.user.history.HistoryAudioActivity;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.util.MediaPlayerUtils;
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

import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jianzhong.lyag.util.MediaPlayerUtils.mMediaPlayer;

/**
 * 每日音频详情
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.ui.exam
 */
public class DryCargoDetailActivity extends ToolbarActivity {

//    @BindView(R.id.head_title)
    TextView mHeadTitle;
//    @BindView(R.id.iv_cover)
    ImageView mIvCover;
//    @BindView(R.id.tv_title)
    TextView mTvTitle;
//    @BindView(R.id.tv_sub_title)
    TextView mTvSubTitle;
//    @BindView(R.id.tv_current_time)
    TextView mTvCurrentTime;
//    @BindView(R.id.seek_bar)
    SeekBar mSeekBar;
//    @BindView(R.id.tv_total_time)
    TextView mTvTotalTime;
//    @BindView(R.id.tv_rate)
    TextView mTvRate;
//    @BindView(R.id.lv_section)
    CustomListView mLvSection;
//    @BindView(R.id.iv_play_type)
    ImageView mIvPlayType;
//    @BindView(R.id.iv_play)
    ImageView mIvPlay;
//    @BindView(R.id.ll_collect)
    LinearLayout mLlCollect;
//    @BindView(R.id.iv_next)
    ImageView mIvNext;
//    @BindView(R.id.iv_previous)
    ImageView mIvPrevious;
//    @BindView(R.id.ll_asign)
    LinearLayout mLlAsign;

    private String audio_id;//音频音频id
    private AudioDetailModel data;
    private String path; //音频播放地址
//    private List<ContentDetailModel> mDatas;
    //是否已初始化过语音播放
    private List<AudioDetailModel> mList = new ArrayList<>();
    private AudioAboutAdapter mAdapter;
    private String curAudioId;      //当前播放音频的id
    private int curPostion = 0;     //当前播放的音频位置
    //是否收藏要单独拿出来设置
    private String has_favor;
    private int playType = 0; //默认列表循环
    //正常情况下结束的语音播放
    private int isCompleteNormal = 1;
    //播放的速率
    private String[] mRateArr = new String[]{"1.0", "1.5", "2.0"};
    private int curRate = 0;
    private int curPlayTime = 0;
    private AudioRecordDao mAudioRecordDao;  //音频播放记录的数据库

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dry_cargo_detail);
        ButterKnife.bind(this);
        EventBus.getDefault().register(this);
    }

    @Override
    public void initData() {
        super.initData();

        mIvCover = findViewById(R.id.iv_cover);
        mHeadTitle = findViewById(R.id.head_title);
        mTvTitle = findViewById(R.id.tv_title);
        mTvSubTitle = findViewById(R.id.tv_sub_title);
        mTvCurrentTime = findViewById(R.id.tv_current_time);
        mSeekBar = findViewById(R.id.seek_bar);
        mTvTotalTime = findViewById(R.id.tv_total_time);
        mTvRate = findViewById(R.id.tv_rate);
        mLvSection = findViewById(R.id.lv_section);
        mIvPlayType = findViewById(R.id.iv_play_type);
        mIvPlay = findViewById(R.id.iv_play);
        mLlCollect = findViewById(R.id.ll_collect);
        mIvNext = findViewById(R.id.iv_next);
        mIvPrevious = findViewById(R.id.iv_previous);
        mLlAsign = findViewById(R.id.ll_asign);

        GroupVarManager.getInstance().isAtAudio = 1;
        mAudioRecordDao = new AudioRecordDao(mContext);

        mHeadTitle.setText("每日音频");
        mHeadTitle.setVisibility(View.VISIBLE);
//        mDatas = (List<ContentDetailModel>) getIntent().getSerializableExtra("mDatas");
        audio_id = getIntent().getStringExtra("audio_id");
        getAudioDetail();
    }

    /**
     * 获取音频详细
     */
    private void getAudioDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("audio_id", audio_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_AUDIO_DETAIL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<AudioDetailModel> result = GsonUtils.json2Bean(s, AudioDetailModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    has_favor = result.getData().getHas_favor();
                    data = result.getData();
                    mList.add(data);
                    mList.get(0).setIsPlay(1);
                    initController();
                    getAboutAudio(result.getData().getCat_id());
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage() : "数据解析错误");
                    finish();
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
                finish();
            }
        });
    }

    /**
     * 获取相关音频列表
     */
    private void getAboutAudio(String cat_id) {
        Map<String, Object> params = new HashMap<>();
        params.put("cat_id", cat_id);
        params.put("audio_id", audio_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_AUDIO_RELATE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<AudioDetailModel> resultList = GsonUtils.json2List(s, AudioDetailModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    if (!ListUtils.isEmpty(resultList.getData())) {
                        mList.addAll(resultList.getData());
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    /**
     * 提交音频完成
     */
    private void getAudioFinish() {
        Map<String, Object> parmas = new HashMap<>();
        parmas.put("audio_id", curAudioId);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_AUDIO_FINISH, parmas, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<AudioFinishModel> result = GsonUtils.json2Bean(s, AudioFinishModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    ToastUtils.show(mContext, result.getData().getMsg());
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    /**
     * 初始化界面
     */
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void initController() {
        if (data == null)
            return;
        //初始化数值以及标志位
        curAudioId = data.getAudio_id(); //当前播放的id
//        curPlayTime = 0;    //当前播放到的位置
        //UI初始化
        GlideUtils.load(mIvCover, data.getCover());
        mTvTitle.setText(data.getTitle());
        mTvSubTitle.setText(data.getSub_title());

        if (!StringUtils.isEmpty(has_favor)) {
            if (has_favor.equals("0")) {
                mLlCollect.setSelected(false);
            } else {
                mLlCollect.setSelected(true);
            }
        }

        DownloadInfo mDownloadInfo = BaseApplication.mDownloadManager.getDownloadInfo(data.getAudio_cdn_url());
        if (mDownloadInfo != null && mDownloadInfo.getState() == DownloadManager.FINISH) {
            path = mDownloadInfo.getTargetPath();
            File file = new File(path);
            if (!file.exists()) {
                path = data.getAudio_cdn_url();
            }
        } else {
            path = data.getAudio_cdn_url();
        }

        //判断是是否有指派权限
//        if (AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion() != null &&
//                !ListUtils.isEmpty(AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion().getOperation())) {
//            for (int i = 0; i < AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion().getOperation().size(); i++) {
//                if (AppUserModel.getInstance().getmLoginInfoModel().getUser_info_all().getMotion().getOperation().get(i).equals(AppConstants.TAG_DICTATE_SEND)) {
//                    mLlAsign.setVisibility(View.VISIBLE);
//                    break;
//                }
//            }
//        }

        mSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(data.getIs_finish().equals("0")){
                    return true;
                }
                return false;
            }
        });

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
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            initAudioPlay();
        } else {
            if (GroupVarManager.getInstance().mAudioDetailModel != null && data.getAudio_id().equals(GroupVarManager.getInstance().mAudioDetailModel.getAudio_id())) {
                setSeekBar();
            } else {
                MediaPlayerUtils.getInstance().stop();
                initAudioPlay();
            }
        }

        if (mAdapter == null) {
            mAdapter = new AudioAboutAdapter(mContext, mList, new OnAudioListener() {
                @Override
                public void callBack(int position, AudioDetailModel item) {
                    curPostion = position;
                    if (curAudioId.equals(item.getAudio_id())) {
                        if (item.getIsPlay() == 0) {
                            setPlayHistory(curPlayTime);
                            MediaPlayerUtils.getInstance().pause();
                            mIvPlay.setBackgroundResource(R.drawable.ypbf_play);
                        } else {
                            MediaPlayerUtils.getInstance().start();
                            mIvPlay.setBackgroundResource(R.drawable.ypbf_stop);
                        }
                    } else {
                        isCompleteNormal = 0;
                        MediaPlayerUtils.getInstance().stop();
                        data = item;
                        initController();
                    }
                }
            });
            mLvSection.setAdapter(mAdapter);
        }
    }

    private static final int UPDATE_PLAY = 1001;
    private static final int IS_DRAG_CODE = 1002;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() { //更新UI 必须通知主线程更新
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_PLAY) {
                initController();
                updateAudioList();
            } else if (msg.what == IS_DRAG_CODE) {

            }
        }
    };

    /**
     * 初始化音频播放
     */
    private void initAudioPlay() {
        MediaPlayerUtils.getInstance().initStart(this, path, getAudioRecord(), new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                setSeekBar();
                MediaPlayerUtils.getInstance().start();
                GroupVarManager.getInstance().mAudioDetailModel = data;
                isCompleteNormal = 1;
                MediaPlayerUtils.getInstance().setRate(Float.valueOf(mRateArr[0]));
                mIvNext.setEnabled(true);
                mIvNext.setClickable(true);
                mIvPrevious.setClickable(true);
                mIvPrevious.setEnabled(true);
            }
        }, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isCompleteNormal == 1 && GroupVarManager.getInstance().isAtAudio == 1) { //是否正常播放完 不是指有没有拖动 只是不是切换播放停止
                    isCompleteNormal = 0;
                    if (data.getIs_finish().equals("0")) {
                        getAudioFinish();
                    }

                    if (playType == 0) {
                        if (curPostion != (mList.size() - 1)) {
                            curPostion = curPostion + 1;
                            data = mList.get(curPostion);
                        } else {
                            curPostion = 0;
                            data = mList.get(curPostion);
                        }
                    }
                    Message msg = new Message();
                    msg.what = UPDATE_PLAY;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void switchAudio(String tag) {
        if(tag.equals(AppConstants.TAG_CLOSE_AUDIO)){ //首页小窗口播放的时候结束

            mIvNext.setEnabled(false);
            mIvNext.setClickable(false);
            mIvPrevious.setClickable(false);
            mIvPrevious.setEnabled(false);
            setPlayHistory(0);
            isCompleteNormal = 0;
            if(MediaPlayerUtils.getInstance().getMediaPlayer() != null && MediaPlayerUtils.getInstance().isPlaying()){
                MediaPlayerUtils.getInstance().stop();
            }
            if(playType == 0){
                if (curPostion != (mList.size() - 1)) {
                    curPostion = curPostion + 1;
                    data = mList.get(curPostion);
                } else {
                    curPostion = 0;
                    data = mList.get(curPostion);
                }
            }
            initController();
            updateAudioList();
        }
    }

    /**
     * 获取音频播放记录
     *
     * @return
     */
    private int getAudioRecord() {
        int playRecord = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("audio_id", curAudioId);
        if (!ListUtils.isEmpty(mAudioRecordDao.queryForValue(map)) && mAudioRecordDao.queryForValue(map).get(0).getAudio_record() != 0) {
            playRecord = (int) mAudioRecordDao.queryForValue(map).get(0).getAudio_record();
        }

        return playRecord;
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

                        curPlayTime = mMediaPlayer.getCurrentPosition();
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

    /**
     * 设置播放记录
     */
    private void setPlayHistory(int recordTime) {

        Map<String, Object> map = new HashMap<>();
        map.put("audio_id", curAudioId);
        map.put("user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
        if (ListUtils.isEmpty(mAudioRecordDao.queryForValue(map))) {
            AudioRecorModel item = new AudioRecorModel();
            item.setAudio_id(curAudioId);
            item.setAudio_record(recordTime);
            item.setDuration_sec(data.getDuration_sec());
            item.setCreate_at(System.currentTimeMillis() + "");
            item.setTitle(data.getTitle());
            item.setUser_id(AppUserModel.getInstance().getmUserModel().getUser_id());
            mAudioRecordDao.insert(item);
        } else {
            AudioRecorModel item = null;
            List<AudioRecorModel> mAudioRecordList = mAudioRecordDao.queryForValue(map);
            for (int i = 0; i < mAudioRecordList.size(); i++) {
                if (curAudioId.equals(mAudioRecordList.get(i).getAudio_id())) {
                    item = mAudioRecordList.get(i);
                    break;
                }
            }
            if (item != null) {
                item.setAudio_record(recordTime);
                mAudioRecordDao.update(item);
            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
//        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
//            setPlayHistory(curPlayTime);
//            mIvPlay.setBackgroundResource(R.drawable.ypbf_play);
//            for (int i = 0; i < mList.size(); i++) {
//                if (curAudioId.equals(mList.get(i).getAudio_id())) {
//                    mList.get(i).setIsPlay(0);
//                }
//            }
//            mAdapter.notifyDataSetChanged();
//            MediaPlayerUtils.getInstance().pause();
//        }

        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            GroupVarManager.getInstance().mAudioDetailModel = data;
        }
    }

    @Override
    protected void onDestroy() {
        EventBus.getDefault().unregister(this);
        GroupVarManager.getInstance().isAtAudio = 0;
        if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            setPlayHistory(curPlayTime);
            MediaPlayerUtils.getInstance().stop();
        } else if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            GroupVarManager.getInstance().mAudioDetailModel = data;
        }
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();

        /*if (mMediaPlayer != null && !mMediaPlayer.isPlaying()) {
            mIvPlay.setBackgroundResource(R.drawable.ypbf_stop);
            MediaPlayerUtils.getInstance().start();
            for (int i = 0; i < mList.size(); i++) {
                if (curAudioId.equals(mList.get(i).getAudio_id())) {
                    mList.get(i).setIsPlay(1);
                } else {
                    mList.get(i).setIsPlay(0);
                }
            }
            if (mAdapter != null) {
                mAdapter.notifyDataSetChanged();
            }
        }*/
    }

    @OnClick({R.id.head_ll_back, R.id.iv_history, R.id.iv_download, R.id.iv_share, R.id.ll_collect, R.id.ll_manuscript, R.id.iv_play_type, R.id.iv_previous, R.id.iv_play, R.id.iv_next, R.id.tv_rate})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.head_ll_back:
                setPlayHistory(curPlayTime);
                DryCargoDetailActivity.this.finish();
                break;
            case R.id.iv_history:
                intent = new Intent(mContext, HistoryAudioActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_download:
                if (BaseApplication.mDownloadManager.getDownloadInfo(data.getAudio_cdn_url()) != null) {
                    ToastUtils.show(mContext, "任务已经在下载列表中");
                    DownloadInfo mDownloadInfo = BaseApplication.mDownloadManager.getDownloadInfo(data.getAudio_cdn_url());
                    DownbodyModel mDownbodyModel = (DownbodyModel) mDownloadInfo.getData();
                    if(!ListUtils.isEmpty(mDownbodyModel.getUserList())){
                        boolean isHadInsert = false;
                        for (int i = 0; i < mDownbodyModel.getUserList().size(); i++) {
                            if(mDownbodyModel.getUserList().get(i).equals(AppUserModel.getInstance().getmUserModel().getUser_id())){
                                isHadInsert = true;
                                break;
                            }
                        }

                        if(!isHadInsert){
                            mDownbodyModel.getUserList().add(AppUserModel.getInstance().getmUserModel().getUser_id());
                            DownloadDBManager.INSTANCE.update(mDownloadInfo);
                        }
                    }
                } else {
                    DownbodyModel info = new DownbodyModel();
                    info.setType(0);
                    info.setMediaType(0);
                    info.setHead_id(data.getAudio_id());
                    info.setUrl(data.getAudio_cdn_url());
                    info.setTitle(data.getTitle());
                    info.setDuration_sec(data.getDuration_sec());
                    info.setSection_id("");
                    info.setImg_url(data.getCover());
                    info.setName("");
                    info.setPost("");
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
            case R.id.ll_collect:
                if (data.getHas_favor().equals("0")) {
                    addCollect();
                } else {
                    delCollect();
                }
                break;
            case R.id.ll_manuscript:
                intent = new Intent(mContext, CommonWebActivity.class);
                intent.putExtra("title", "音频文稿");
                intent.putExtra("url", HttpConfig.WEB_URL_BASE + HttpConfig.URL_AUDIO_TEXT + curAudioId);
                startActivity(intent);
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
                setPlayHistory(curPlayTime);
                MediaPlayerUtils.getInstance().stop();
                if (curPostion != 0) {
                    curPostion = curPostion - 1;
                    data = mList.get(curPostion);
                } else {
                    curPostion = mList.size() - 1;
                    data = mList.get(curPostion);
                }
                initController();
                updateAudioList();
                break;
            case R.id.iv_play:
                if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
                    setPlayHistory(curPlayTime);
                    mIvPlay.setBackgroundResource(R.drawable.ypbf_play);
                    MediaPlayerUtils.getInstance().pause();
                    for (int i = 0; i < mList.size(); i++) {
                        if (curAudioId.equals(mList.get(i).getAudio_id())) {
                            mList.get(i).setIsPlay(0);
                        }
                    }
                    mAdapter.notifyDataSetChanged();

                } else {
                    mIvPlay.setBackgroundResource(R.drawable.ypbf_stop);
                    MediaPlayerUtils.getInstance().start();
                    for (int i = 0; i < mList.size(); i++) {
                        if (curAudioId.equals(mList.get(i).getAudio_id())) {
                            mList.get(i).setIsPlay(1);
                        } else {
                            mList.get(i).setIsPlay(0);
                        }
                    }
                    mAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.iv_next:
                mIvNext.setEnabled(false);
                mIvNext.setClickable(false);
                isCompleteNormal = 0;
                setPlayHistory(curPlayTime);
                MediaPlayerUtils.getInstance().stop();
                if (curPostion != (mList.size() - 1)) {
                    curPostion = curPostion + 1;
                    data = mList.get(curPostion);
                } else {
                    curPostion = 0;
                    data = mList.get(curPostion);
                }
                initController();
                updateAudioList();
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

    /**
     * 更新相关音频
     */
    private void updateAudioList() {
        for (int i = 0; i < mList.size(); i++) {
            if (curAudioId.equals(mList.get(i).getAudio_id())) {
                mList.get(i).setIsPlay(1);
            } else {
                mList.get(i).setIsPlay(0);
            }
        }
        mAdapter.notifyDataSetChanged();
    }

    /**
     * 添加收藏
     */
    private void addCollect() {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", data.getAsset_type());
        params.put("foreign_id", data.getAudio_id());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_ADD_FAVOR, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<String> result = GsonUtils.json2Bean(s, String.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    ToastUtils.show(mContext,result.getMessage());
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
        params.put("foreign_id", data.getAudio_id());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DEL_FAVOR, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<String> result = GsonUtils.json2Bean(s, String.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    ToastUtils.show(mContext,result.getMessage());
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
