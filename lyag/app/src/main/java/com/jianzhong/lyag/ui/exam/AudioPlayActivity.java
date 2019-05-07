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
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.baselib.widget.CustomListView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.AudioSectionAdapter;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.db.dao.CourseRecordDao;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnAudioClassListener;
import com.jianzhong.lyag.listener.OnScoreListener;
import com.jianzhong.lyag.model.ClassDetailModel;
import com.jianzhong.lyag.model.CourseRecorModel;
import com.jianzhong.lyag.model.DownbodyModel;
import com.jianzhong.lyag.model.RecordCommitModel;
import com.jianzhong.lyag.model.SectionModel;
import com.jianzhong.lyag.ui.organization.AssignStudyActivity;
import com.jianzhong.lyag.util.DialogHelper;
import com.jianzhong.lyag.util.DialogUtil;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.util.MediaPlayerUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.download.db.DownloadDBManager;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jianzhong.lyag.util.MediaPlayerUtils.mMediaPlayer;

/**
 * 音频播放详情
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.ui.exam
 */
public class AudioPlayActivity extends ToolbarActivity {

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
    private ClassDetailModel data;
    private String path;
    //
    private AudioSectionAdapter mAdapter;
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

    private CourseRecordDao mCourseRecordDao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_audio_play);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        mCourseRecordDao = new CourseRecordDao(mContext);
        data = (ClassDetailModel) getIntent().getSerializableExtra("data");
        mHeadTitle.setText("音频播放");

        initController();
    }

    /**
     * 初始化控件
     */
    private void initController() {
        if (data == null)
            return;
        hideCommonLoading();
        mTvTitle.setText(data.getTitle());
        mTvSubTitle.setText(data.getSection().get(curPostion).getTitle());
        GlideUtils.load(mIvCover, data.getCover());
        if (!ListUtils.isEmpty(data.getSection())) {
            for (int i = 0; i < data.getSection().size(); i++) {
                if (!StringUtils.isEmpty(data.getSection().get(i).getAudio_cdn_url())) {
                    mList.add(data.getSection().get(i));
                }
            }
        }

        if (data.getHas_favor().equals("0")) {
            mLlCollect.setSelected(false);
        } else {
            mLlCollect.setSelected(true);
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

        if (mAdapter == null) {
            mAdapter = new AudioSectionAdapter(mContext, mList, data.getCover(),new OnAudioClassListener() {
                @Override
                public void callBack(int position, SectionModel item) {
                    curPostion = position;
                    if (curAudioId.equals(item.getSection_id())) {
                        if (item.getIsPlay() == 0) {
                            MediaPlayerUtils.getInstance().pause();
                            mIvPlay.setBackgroundResource(R.drawable.ypbf_play);
                        } else {
                            MediaPlayerUtils.getInstance().start();
                            mIvPlay.setBackgroundResource(R.drawable.ypbf_stop);
                        }
                    } else {
                        isCompleteNormal = 0;
                        setPlayHistory(mMediaPlayer.getCurrentPosition());
                        MediaPlayerUtils.getInstance().stop();
                        initAudio();
                    }
                }
            });
            mLvSection.setAdapter(mAdapter);
        }

        initAudio();
    }

    /**
     * 初始化音频播放
     */
    @SuppressLint({"SetTextI18n", "ClickableViewAccessibility"})
    private void initAudio() {
        if (StringUtils.isEmpty(data.getSection().get(curPostion).getAudio_url()) &&
                StringUtils.isEmpty(data.getSection().get(curPostion).getAudio_cdn_url()))
            return;
        //初始化数值以及标志位
        curAudioId = data.getSection().get(curPostion).getSection_id(); //当前播放的id
        curPlayTime = 0;    //当前播放到的位置
        DownloadInfo mDownloadInfo = BaseApplication.mDownloadManager.getDownloadInfo(data.getSection().get(curPostion).getAudio_cdn_url());
        if (mDownloadInfo != null && mDownloadInfo.getState() == DownloadManager.FINISH) {
            path = mDownloadInfo.getTargetPath();
            File file = new File(path);
            if (!file.exists()) {
                path = data.getSection().get(curPostion).getAudio_cdn_url();
            }
        } else {
            path = data.getSection().get(curPostion).getAudio_cdn_url();
        }
        curAudioId = data.getSection().get(curPostion).getSection_id();
        mSeekBar.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if(data.getSection().get(curPostion).getIs_section_finish() == 0){
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
        curRate = 0;
        MediaPlayerUtils.getInstance().initStart(this, path, getPlayRecord(), new MediaPlayer.OnPreparedListener() {
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

                Message msg = new Message();
                msg.what = UPDATE_LIST;
                handler.sendMessage(msg);
            }
        }, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                if (isCompleteNormal == 1) { //是否正常播放完 不是指有没有拖动 只是不是切换播放停止
                    isCompleteNormal = 0;
                    if (data.getSection().get(curPostion).getIs_section_finish() == 0) {
                        getCourseStudy();
                    }
                    if (playType == 0) {
                        if (curPostion != (mList.size() - 1)) {
                            curPostion = curPostion + 1;
                        } else {
                            curPostion = 0;
                        }
                    }

                    Message msg = new Message();
                    msg.what = UPDATE_PLAY;
                    handler.sendMessage(msg);
                }
            }
        });
    }

    private static final int UPDATE_PLAY = 1001;
    private static final int UPDATE_LIST = 1002;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() { //更新UI 必须通知主线程更新
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_PLAY) {
                initAudio();
            } else if (msg.what == UPDATE_LIST) {
                updateAudioList();
            }
        }
    };

    /**
     * 获取播放记录
     *
     * @return
     */
    private int getPlayRecord() {
        int courseRecord = 0;
        if (data.getSection().get(curPostion).getIs_section_finish() == 1) { //课程已完成 不用从头开始
            Map<String, Object> map = new HashMap<>();
            map.put("course_id", data.getCourse_id());
            map.put("user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
            if (!ListUtils.isEmpty(mCourseRecordDao.queryForValue(map))) {
                for (int i = 0; i < mCourseRecordDao.queryForValue(map).size(); i++) {
                    if (mCourseRecordDao.queryForValue(map).get(i).getSection_id().equals(curAudioId)) {
                        courseRecord = (int) mCourseRecordDao.queryForValue(map).get(i).getCourse_record();
                        break;
                    }
                }
            }
        }

        return courseRecord;
    }

    /**
     * 设置播放记录
     */
    private void setPlayHistory(long recordTime) {
        Map<String, Object> map = new HashMap<>();
        map.put("course_id", data.getCourse_id());
        map.put("user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
        if (ListUtils.isEmpty(mCourseRecordDao.queryForValue(map))) {
            CourseRecorModel item = new CourseRecorModel();
            item.setCourse_id(data.getCourse_id());
            item.setSection_id(curAudioId);
            item.setCourse_record(recordTime);
            item.setUser_id(AppUserModel.getInstance().getmUserModel().getUser_id());
            mCourseRecordDao.insert(item);
        } else {
            boolean isHadInsert = false;
            for (int i = 0; i < mCourseRecordDao.queryForValue(map).size(); i++) {
                if (mCourseRecordDao.queryForValue(map).get(i).getSection_id().equals(curAudioId)) {
                    isHadInsert = true;
                    CourseRecorModel item = mCourseRecordDao.queryForValue(map).get(i);
                    item.setCourse_record(recordTime);
                    mCourseRecordDao.update(item);
                    break;
                }
            }

            if (!isHadInsert) {
                CourseRecorModel item = new CourseRecorModel();
                item.setCourse_id(data.getCourse_id());
                item.setSection_id(curAudioId);
                item.setCourse_record(recordTime);
                item.setUser_id(AppUserModel.getInstance().getmUserModel().getUser_id());
                mCourseRecordDao.insert(item);
            }
        }
    }

    /**
     * 更新相关的播放列表状态
     */
    private void updateAudioList() {
        for (int i = 0; i < mList.size(); i++) {
            if (curAudioId.equals(mList.get(i).getSection_id())) {
                mList.get(i).setIsPlay(1);
            } else {
                mList.get(i).setIsPlay(0);
            }
        }
        mAdapter.notifyDataSetChanged();
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
     * 课程学习记录 第一次学习时提交
     */
    private void getCourseStudy() {
        Map<String, Object> parmas = new HashMap<>();
        parmas.put("section_id", curAudioId);
        parmas.put("course_id", data.getCourse_id());
        parmas.put("column_id", data.getColumn() != null ? data.getColumn().getColumn_id() : "0");
        parmas.put("is_section_finish", "1");
        parmas.put("section_num", data.getSection().size() + "");
        DialogHelper.showDialog(mContext, "课程学习记录提交中...");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COURSE_STUDY, parmas, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<RecordCommitModel> result = GsonUtils.json2Bean(s, RecordCommitModel.class);
                DialogHelper.dismissDialog();
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    ToastUtils.show(mContext, "课程学习记录提交成功");
                    data.getSection().get(curPostion).setIs_section_finish(1);
                    if (!result.getData().getFinish_course_id().equals("0")) {
                        DialogUtil.getInstance().showCourseComment(AudioPlayActivity.this, new OnScoreListener() {
                            @Override
                            public void onScoreCallback(String score) {
                                getCourseJudge(score);
                            }
                        });
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
        params.put("course_id", data.getCourse_id());
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
    protected void onPause() {
        super.onPause();
        if (mMediaPlayer != null) {
            mIvPlay.setBackgroundResource(R.drawable.ypbf_play);
            MediaPlayerUtils.getInstance().pause();
            for (int i = 0; i < mList.size(); i++) {
                if (curAudioId.equals(mList.get(i).getSection_id())) {
                    mList.get(i).setIsPlay(0);
                }
            }
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onDestroy() {

        if (mMediaPlayer != null) {
            isCompleteNormal = 0;
            setPlayHistory(curPlayTime);
            MediaPlayerUtils.getInstance().stop();
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
//            mAdapter.notifyDataSetChanged();
//        }
    }


    @OnClick({R.id.head_ll_back, R.id.iv_history, R.id.iv_download, R.id.iv_share, R.id.ll_collect, R.id.ll_asign, R.id.iv_play_type, R.id.iv_previous, R.id.iv_play, R.id.iv_next, R.id.tv_rate})
    public void onViewClicked(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.head_ll_back:
                AudioPlayActivity.this.finish();
                break;
            case R.id.iv_history:

                break;
            case R.id.iv_download:

                if (BaseApplication.mDownloadManager.getDownloadInfo(data.getSection().get(curPostion).getAudio_cdn_url()) != null) {
                    ToastUtils.show(mContext, "任务已经在下载列表中");
                    DownloadInfo mDownloadInfo = BaseApplication.mDownloadManager.getDownloadInfo(data.getSection().get(curPostion).getAudio_cdn_url());
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
                    info.setType(1);
                    info.setMediaType(0);
                    info.setHead_id(data.getCourse_id());
                    info.setUrl(data.getSection().get(curPostion).getAudio_cdn_url());
                    info.setTitle(data.getTitle());
                    info.setDuration_sec(data.getDuration_sec());
                    info.setSection_id(data.getSection().get(curPostion).getSection_id());
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
            case R.id.ll_asign:
                intent = new Intent(mContext, AssignStudyActivity.class);
                intent.putExtra("foreign_id", data.getCourse_id());
                intent.putExtra("asset_type", data.getAsset_type());
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
                MediaPlayerUtils.getInstance().stop();
                if (curPostion != 0) {
                    curPostion = curPostion - 1;
                } else {
                    curPostion = mList.size() - 1;
                }
                initAudio();
                updateAudioList();
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
                    mAdapter.notifyDataSetChanged();
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
                    mAdapter.notifyDataSetChanged();
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
