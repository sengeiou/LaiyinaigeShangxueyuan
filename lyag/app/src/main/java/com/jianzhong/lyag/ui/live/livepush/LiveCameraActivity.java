package com.jianzhong.lyag.ui.live.livepush;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.PermissionChecker;
import android.text.TextUtils;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.ToggleButton;

import com.alibaba.livecloud.event.AlivcEvent;
import com.alibaba.livecloud.event.AlivcEventResponse;
import com.alibaba.livecloud.event.AlivcEventSubscriber;
import com.alibaba.livecloud.live.AlivcMediaFormat;
import com.alibaba.livecloud.live.AlivcMediaRecorder;
import com.alibaba.livecloud.live.AlivcMediaRecorderFactory;
import com.alibaba.livecloud.live.AlivcRecordReporter;
import com.alibaba.livecloud.live.AlivcStatusCode;
import com.alibaba.livecloud.live.OnLiveRecordErrorListener;
import com.alibaba.livecloud.live.OnNetworkStatusListener;
import com.alibaba.livecloud.live.OnRecordStatusListener;
import com.alibaba.livecloud.model.AlivcWatermark;
import com.baselib.util.GsonUtils;
import com.baselib.util.Result;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnLiveListener;
import com.jianzhong.lyag.model.OnLineUserModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.PopWindowUtil;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

;

/**
 * author:杭州短趣网络传媒技术有限公司
 * date:2016/6/27
 * description:LiveCameraActivity
 */
public class LiveCameraActivity extends FragmentActivity {

    @BindView(R.id.view_line)
    View mViewLine;
    @BindView(R.id.iv_live_start)
    ImageView mIvLiveStart;
    @BindView(R.id.tv_live_time)
    TextView mTvLiveTime;
    @BindView(R.id.ll_online_user)
    LinearLayout mLlOnlineUser;
    @BindView(R.id.tv_online_user)
    TextView mTvOnlineUser;

    public static class RequestBuilder {
        String rtmpUrl;
        int videoResolution;
        boolean isPortrait;
        int cameraFacing;
        String watermarkUrl;
        int dx;
        int dy;
        int site;
        int watermarkWidth;
        int watermarkHeight;
        int bestBitrate;
        int minBitrate;
        int maxBitrate;
        int initBitrate;
        int frameRate;
        String onlineUser;
        String live_id;

        public RequestBuilder live_id(String live_id) {
            this.live_id = live_id;
            return this;
        }

        public RequestBuilder onlineUser(String onlineUser) {
            this.onlineUser = onlineUser;
            return this;
        }

        public RequestBuilder rtmpUrl(String url) {
            this.rtmpUrl = url;
            return this;
        }

        public RequestBuilder videoResolution(int resolution) {
            this.videoResolution = resolution;
            return this;
        }

        public RequestBuilder portrait(boolean isPortrait) {
            this.isPortrait = isPortrait;
            return this;
        }

        public RequestBuilder cameraFacing(int cameraFacing) {
            this.cameraFacing = cameraFacing;
            return this;
        }

        public RequestBuilder watermarkUrl(String url) {
            this.watermarkUrl = url;
            return this;
        }

        public RequestBuilder dx(int dx) {
            this.dx = dx;
            return this;
        }

        public RequestBuilder dy(int dy) {
            this.dy = dy;
            return this;
        }

        public RequestBuilder site(int site) {
            this.site = site;
            return this;
        }

        public RequestBuilder bestBitrate(int bestBitrate) {
            this.bestBitrate = bestBitrate;
            return this;
        }

        public RequestBuilder minBitrate(int minBitrate) {
            this.minBitrate = minBitrate;
            return this;
        }

        public RequestBuilder maxBitrate(int maxBitrate) {
            this.maxBitrate = maxBitrate;
            return this;
        }

        public RequestBuilder initBitrate(int initBitrate) {
            this.initBitrate = initBitrate;
            return this;
        }

        public RequestBuilder frameRate(int frameRate) {
            this.frameRate = frameRate;
            return this;
        }

        public RequestBuilder watermarkWidth(int width) {
            this.watermarkWidth = width;
            return this;
        }

        public RequestBuilder watermarkHeight(int height) {
            this.watermarkHeight = height;
            return this;
        }

        public Intent build(Context context) {
            Intent intent = new Intent(context, LiveCameraActivity.class);
            intent.putExtra(URL, rtmpUrl);
            intent.putExtra(VIDEO_RESOLUTION, videoResolution);
            intent.putExtra(SCREEN_ORIENTATION, isPortrait);
            intent.putExtra(FRONT_CAMERA_FACING, cameraFacing);
            intent.putExtra(WATERMARK_PATH, watermarkUrl);
            intent.putExtra(WATERMARK_DX, dx);
            intent.putExtra(WATERMARK_DY, dy);
            intent.putExtra(WATERMARK_SITE, site);
            intent.putExtra(BEST_BITRATE, bestBitrate);
            intent.putExtra(MIN_BITRATE, minBitrate);
            intent.putExtra(MAX_BITRATE, maxBitrate);
            intent.putExtra(INIT_BITRATE, initBitrate);
            intent.putExtra(FRAME_RATE, frameRate);
            intent.putExtra(WATERMARK_WIDTH, watermarkWidth);
            intent.putExtra(WATERMARK_HEIGHT, watermarkHeight);
            intent.putExtra(ONLINE_USER, onlineUser);
            intent.putExtra(LIVE_ID, live_id);
            return intent;
        }

    }


    private static final String TAG = "AlivcLiveDemo";

    public final static String URL = "url";
    public final static String VIDEO_RESOLUTION = "video_resolution"; //视频分辨率 720P 1080P
    public final static String SCREEN_ORIENTATION = "screen_orientation";  //屏幕方向
    public final static String FRONT_CAMERA_FACING = "front_camera_face"; //前置摄像头的脸

    public final static String WATERMARK_PATH = "watermark_path";  //水印的路径
    public final static String WATERMARK_DX = "watermark_dx";
    public final static String WATERMARK_DY = "watermark_dy";
    public final static String WATERMARK_SITE = "watermark_site";  //水印的位置
    public final static String WATERMARK_WIDTH = "watermark_width";
    public final static String WATERMARK_HEIGHT = "watermark_height";

    public final static String BEST_BITRATE = "best-bitrate"; //最佳的比特率
    public final static String MIN_BITRATE = "min-bitrate";   //最小比特率
    public final static String MAX_BITRATE = "max-bitrate";
    public final static String INIT_BITRATE = "init-bitrate";
    public final static String FRAME_RATE = "frame-rate";  //帧速率
    public final static String ONLINE_USER = "online_user";  //帧速率
    public final static String LIVE_ID = "live_id";  //帧速率

    private static final int PERMISSION_REQUEST_CODE = 1;
    private static final String[] permissionManifest = {
            Manifest.permission.CAMERA,
            Manifest.permission.RECORD_AUDIO
    };
    private final int PERMISSION_DELAY = 100;
    private boolean mHasPermission = false;

    private SurfaceView _CameraSurface;
    private AlivcMediaRecorder mMediaRecorder;
    private AlivcRecordReporter mRecordReporter;
    private ToggleButton mTbtnMute;
    private ToggleButton mTbtnMirrored;

    private Map<String, Object> mConfigure = new HashMap<>();
    private boolean isRecording = false;
    private int mPreviewWidth = 0;
    private int mPreviewHeight = 0;
    private DataStatistics mDataStatistics = new DataStatistics(1000);
    private AdvancedSettingDialog mSettingDialog;
    private Button mBtnAdavanceSetting;

    public static void startActivity(Context context,
                                     RequestBuilder builder) {
        Intent intent = builder.build(context);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_live_camera);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= 23) {
            permissionCheck();
        } else {
            mHasPermission = true;
        }

        getExtraData();

        mTvOnlineUser.setText(onlineUser);
        mTvLiveTime.setText(CommonUtils.timimg(countDown));
        getOnlineUserNum();
        initView();

        setRequestedOrientation(screenOrientation ? ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE : ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        //采集
        _CameraSurface = (SurfaceView) findViewById(R.id.camera_surface);
        _CameraSurface.getHolder().addCallback(_CameraSurfaceCallback);
        _CameraSurface.setOnTouchListener(mOnTouchListener);

        //对焦，缩放
        mDetector = new GestureDetector(_CameraSurface.getContext(), mGestureDetector);
        mScaleDetector = new ScaleGestureDetector(_CameraSurface.getContext(), mScaleGestureListener);

        mMediaRecorder = AlivcMediaRecorderFactory.createMediaRecorder();
        mMediaRecorder.init(this);
        mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
        mDataStatistics.setReportListener(mReportListener);

        /**
         * this method only can be called after mMediaRecorder.init(),
         * otherwise will return null;
         */
        mRecordReporter = mMediaRecorder.getRecordReporter();

        mDataStatistics.start();
        mMediaRecorder.setOnRecordStatusListener(mRecordStatusListener);
        mMediaRecorder.setOnNetworkStatusListener(mOnNetworkStatusListener);
        mMediaRecorder.setOnRecordErrorListener(mOnErrorListener);

        mConfigure.put(AlivcMediaFormat.KEY_CAMERA_FACING, cameraFrontFacing);
        mConfigure.put(AlivcMediaFormat.KEY_MAX_ZOOM_LEVEL, 3);
        mConfigure.put(AlivcMediaFormat.KEY_OUTPUT_RESOLUTION, resolution);
        mConfigure.put(AlivcMediaFormat.KEY_MAX_VIDEO_BITRATE, maxBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_BEST_VIDEO_BITRATE, bestBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_MIN_VIDEO_BITRATE, minBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_INITIAL_VIDEO_BITRATE, initBitrate * 1000);
        mConfigure.put(AlivcMediaFormat.KEY_DISPLAY_ROTATION, screenOrientation ? AlivcMediaFormat.DISPLAY_ROTATION_90 : AlivcMediaFormat.DISPLAY_ROTATION_0);
        mConfigure.put(AlivcMediaFormat.KEY_EXPOSURE_COMPENSATION, -1);//曝光度
        mConfigure.put(AlivcMediaFormat.KEY_WATERMARK, mWatermark);
        mConfigure.put(AlivcMediaFormat.KEY_FRAME_RATE, frameRate);
        btn_switch_beauty.setChecked(true);

        mSettingDialog = new AdvancedSettingDialog();
        mSettingDialog.setAdvancedSettingListener(new AdvancedSettingDialog.AdvancedSettingListener() {
            @Override
            public void onAdvancedSettingChange(int event, Bundle bundle) {
                switch (event) {
                    case AdvancedSettingDialog.EVENT_BEAUTY_LEVEL_CHANGED:
                        int beautyLevel = bundle.getInt(AdvancedSettingDialog.KEY_BEAUTY_LEVEL);
                        mMediaRecorder.setBeautyLevel(beautyLevel);
                        break;
                }
            }
        });
    }

    @OnClick({R.id.iv_camera, R.id.iv_close, R.id.iv_live_start})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_camera:
                int currFacing = mMediaRecorder.switchCamera();
                if (currFacing == AlivcMediaFormat.CAMERA_FACING_FRONT) {
                    mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
                }
                mConfigure.put(AlivcMediaFormat.KEY_CAMERA_FACING, currFacing);
                break;
            case R.id.iv_close:
                PopWindowUtil.getInstance().showLiveClose(this, mViewLine, onlineUser, new OnLiveListener() {
                    @Override
                    public void closeLive() {
                        LiveCameraActivity.this.finish();
                    }

                    @Override
                    public void goingOn() {

                    }
                });
                break;
            case R.id.iv_live_start:
                try {
                    mIvLiveStart.setVisibility(View.GONE);
                    mCountDownThread = new CountDownThread();
                    mCountDownThread.start();
                    mMediaRecorder.startRecord(pushUrl);
                } catch (Exception e) {
                }
                isRecording = true;
                break;
        }
    }

    private int countDown;
    private static final int UPDATE_COUNT_DOWN = 1001;
    private CountDownThread mCountDownThread;
    /**
     * 倒计时的线程
     */
    private class CountDownThread extends Thread {
        @Override
        public void run() {
            try {
                while (true) {
                    Thread.sleep(1000);
                    countDown++;
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
                mTvLiveTime.setText(CommonUtils.timimg(countDown));
                if(countDown > 0 && countDown/60 == 0){
                    getOnlineUserNum();
                }
            }
        }
    };

    /**
     * 获取在线用户
     */
    private void getOnlineUserNum(){
        Map<String,Object> params = new HashMap<>();
        params.put("live_id",live_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_ONLINE_USER_NUM, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<OnLineUserModel> result = GsonUtils.json2Bean(s,OnLineUserModel.class);
                if(result != null && result.getCode() == HttpConfig.STATUS_SUCCESS){
                    onlineUser = result.getData().getOnline_user_num();
                    mTvOnlineUser.setText(result.getData().getOnline_user_num());
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private String pushUrl;
    private int resolution;
    private boolean screenOrientation;
    private int cameraFrontFacing;
    private AlivcWatermark mWatermark;
    private int bestBitrate;
    private int minBitrate;
    private int maxBitrate;
    private int initBitrate;
    private int frameRate;
    private String onlineUser;
    private String live_id;

    private void getExtraData() {
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            pushUrl = bundle.getString(URL);
            resolution = bundle.getInt(VIDEO_RESOLUTION);
            screenOrientation = bundle.getBoolean(SCREEN_ORIENTATION);
            cameraFrontFacing = bundle.getInt(FRONT_CAMERA_FACING);
            mWatermark = new AlivcWatermark.Builder()
                    .watermarkUrl(bundle.getString(WATERMARK_PATH))
                    .paddingX(bundle.getInt(WATERMARK_DX))
                    .paddingY(bundle.getInt(WATERMARK_DY))
                    .site(bundle.getInt(WATERMARK_SITE))
                    .width(bundle.getInt(WATERMARK_WIDTH))
                    .height(bundle.getInt(WATERMARK_HEIGHT))
                    .build();
            minBitrate = bundle.getInt(MIN_BITRATE);
            maxBitrate = bundle.getInt(MAX_BITRATE);
            bestBitrate = bundle.getInt(BEST_BITRATE);
            initBitrate = bundle.getInt(INIT_BITRATE);
            frameRate = bundle.getInt(FRAME_RATE);
            onlineUser = bundle.getString(ONLINE_USER);
            live_id = bundle.getString(LIVE_ID);
        }
    }

    private void permissionCheck() {
        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (String permission : permissionManifest) {
            if (PermissionChecker.checkSelfPermission(this, permission)
                    != PackageManager.PERMISSION_GRANTED) {
                permissionCheck = PackageManager.PERMISSION_DENIED;
            }
        }
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, permissionManifest, PERMISSION_REQUEST_CODE);
        } else {
            mHasPermission = true;
        }
    }

    private ToggleButton btn_live_push;
    private ToggleButton toggle_camera;
    private ToggleButton btn_switch_beauty;
    private ToggleButton toggle_flash_light;

    private TextView tv_video_capture_fps;
    private TextView tv_audio_encoder_fps;
    private TextView tv_video_encoder_fps;
    private TextView tv_output_bitrate;
    private TextView tv_av_output_diff;
    private TextView tv_audio_out_fps;
    private TextView tv_video_output_fps;
    private TextView tv_video_delay_duration;
    private TextView tv_audio_delay_duration;
    private TextView tv_video_cache_frame_cnt;
    private TextView tv_audio_cache_frame_cnt;
    private TextView tv_video_cache_byte_size;
    private TextView tv_audio_cache_byte_size;
    private TextView tv_video_frame_discard_cnt;
    private TextView tv_audio_frame_discard_cnt;
    private TextView tv_cur_video_bueaty_duration;
    private TextView tv_cur_video_encoder_duration;
    private TextView tv_cur_video_encode_birate;

    private TextView tv_video_output_frame_count;
    private TextView tv_video_data;
    private TextView tv_video_buffer_count;
    private TextView tv_audio_data;

    private void initView() {
        btn_live_push = (ToggleButton) findViewById(R.id.toggle_live_push);
        btn_live_push.setOnCheckedChangeListener(_PushOnCheckedChange);
        toggle_camera = (ToggleButton) findViewById(R.id.toggle_camera);
        toggle_camera.setOnCheckedChangeListener(_CameraOnCheckedChange);
        mTbtnMute = (ToggleButton) findViewById(R.id.btn_mute);
        mTbtnMute.setOnCheckedChangeListener(mMuteCheckedChange);
        mTbtnMirrored = (ToggleButton) findViewById(R.id.btn_mirrored);
        mTbtnMirrored.setOnCheckedChangeListener(mMirroredCheckedChange);

        btn_switch_beauty = (ToggleButton) findViewById(R.id.btn_switch_beauty);
        btn_switch_beauty.setOnCheckedChangeListener(_SwitchBeautyOnCheckedChange);

        toggle_flash_light = (ToggleButton) findViewById(R.id.toggle_flash_light);
        toggle_flash_light.setOnCheckedChangeListener(_SwitchFlashLightOnCheckedChange);

        tv_video_capture_fps = (TextView) findViewById(R.id.tv_video_capture_fps);
        tv_audio_encoder_fps = (TextView) findViewById(R.id.tv_audio_encoder_fps);
        tv_video_encoder_fps = (TextView) findViewById(R.id.tv_video_encoder_fps);
        tv_output_bitrate = (TextView) findViewById(R.id.tv_output_bitrate);
        tv_av_output_diff = (TextView) findViewById(R.id.tv_av_output_diff);
        tv_audio_out_fps = (TextView) findViewById(R.id.tv_audio_out_fps);
        tv_video_output_fps = (TextView) findViewById(R.id.tv_video_output_fps);
//        tv_stream_publish_time = (TextView) findViewById(R.id.tv_video_capture_fps);
//        tv_stream_server_ip = (TextView) findViewById(R.id.tv_video_capture_fps);
        tv_video_delay_duration = (TextView) findViewById(R.id.tv_video_delay_duration);
        tv_audio_delay_duration = (TextView) findViewById(R.id.tv_audio_delay_duration);
        tv_video_cache_frame_cnt = (TextView) findViewById(R.id.tv_video_cache_frame_cnt);
        tv_audio_cache_frame_cnt = (TextView) findViewById(R.id.tv_audio_cache_frame_cnt);
        tv_video_cache_byte_size = (TextView) findViewById(R.id.tv_video_cache_byte_size);
        tv_audio_cache_byte_size = (TextView) findViewById(R.id.tv_audio_cache_byte_size);
        tv_video_frame_discard_cnt = (TextView) findViewById(R.id.tv_video_frame_discard_cnt);
        tv_audio_frame_discard_cnt = (TextView) findViewById(R.id.tv_audio_frame_discard_cnt);
        tv_cur_video_bueaty_duration = (TextView) findViewById(R.id.tv_cur_video_bueaty_duration);
        tv_cur_video_encoder_duration = (TextView) findViewById(R.id.tv_cur_video_encoder_duration);
        tv_cur_video_encode_birate = (TextView) findViewById(R.id.tv_video_encode_bitrate);

        tv_video_output_frame_count = (TextView) findViewById(R.id.tv_video_output_frame_count);
        tv_video_data = (TextView) findViewById(R.id.tv_video_data);
        tv_video_buffer_count = (TextView) findViewById(R.id.tv_video_buffer_count);
        tv_audio_data = (TextView) findViewById(R.id.tv_audio_data);
        mBtnAdavanceSetting = (Button) findViewById(R.id.btn_advance);
        mBtnAdavanceSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mSettingDialog.show(getSupportFragmentManager(), "Settings");
            }
        });
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

    }

    @Override
    protected void onResume() {
        super.onResume();
//        if (_CameraSurface != null) {
//            mMediaRecorder.prepare(mConfigure, _CameraSurface);
//            Log.d("AlivcMediaRecorder", " onResume==== isRecording =" + isRecording + "=====");
//        }
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_BITRATE_DOWN, mBitrateDownRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_BITRATE_RAISE, mBitrateUpRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_AUDIO_CAPTURE_OPEN_SUCC, mAudioCaptureSuccRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_DATA_DISCARD, mDataDiscardRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_INIT_DONE, mInitDoneRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_VIDEO_ENCODER_OPEN_SUCC, mVideoEncoderSuccRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_VIDEO_ENCODER_OPEN_FAILED, mVideoEncoderFailedRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_VIDEO_ENCODED_FRAMES_FAILED, mVideoEncodeFrameFailedRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_AUDIO_ENCODED_FRAMES_FAILED, mAudioEncodeFrameFailedRes));
        mMediaRecorder.subscribeEvent(new AlivcEventSubscriber(AlivcEvent.EventType.EVENT_AUDIO_CAPTURE_OPEN_FAILED, mAudioCaptureOpenFailedRes));
        mMediaRecorder.resume();
    }

    @Override
    protected void onPause() {
      /*  if (isRecording) {
            mMediaRecorder.stopRecord();
        }*/
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_BITRATE_DOWN);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_BITRATE_RAISE);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_AUDIO_CAPTURE_OPEN_SUCC);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_DATA_DISCARD);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_INIT_DONE);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_VIDEO_ENCODER_OPEN_SUCC);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_VIDEO_ENCODER_OPEN_FAILED);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_VIDEO_ENCODED_FRAMES_FAILED);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_AUDIO_ENCODED_FRAMES_FAILED);
        mMediaRecorder.unSubscribeEvent(AlivcEvent.EventType.EVENT_AUDIO_CAPTURE_OPEN_FAILED);
        /**
         * 如果要调用stopRecord和reset()方法，则stopRecord（）必须在reset之前调用，否则将会抛出IllegalStateException
         */
        //  mMediaRecorder.reset();
        mMediaRecorder.pause();
        super.onPause();
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        mDataStatistics.stop();
        mMediaRecorder.release();
    }

    private final CompoundButton.OnCheckedChangeListener _SwitchFlashLightOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_FLASH_MODE_ON);
                    } else {
                        mMediaRecorder.removeFlag(AlivcMediaFormat.FLAG_FLASH_MODE_ON);
                    }
                }
            };

    private final CompoundButton.OnCheckedChangeListener _SwitchBeautyOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
                    } else {
                        mMediaRecorder.removeFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
                    }
                }
            };

    private final CompoundButton.OnCheckedChangeListener _CameraOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int currFacing = mMediaRecorder.switchCamera();
                    if (currFacing == AlivcMediaFormat.CAMERA_FACING_FRONT) {
                        mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
                    }
                    mConfigure.put(AlivcMediaFormat.KEY_CAMERA_FACING, currFacing);
                }
            };
    private final CompoundButton.OnCheckedChangeListener mMuteCheckedChange =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_MUTE_ON);
                    } else {
                        mMediaRecorder.removeFlag(AlivcMediaFormat.FLAG_MUTE_ON);
                    }

                }
            };
    private final CompoundButton.OnCheckedChangeListener mMirroredCheckedChange =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_FRONT_CAMERA_MIRRORED);
                    } else {
                        mMediaRecorder.removeFlag(AlivcMediaFormat.FLAG_FRONT_CAMERA_MIRRORED);
                    }
                }
            };

    private final CompoundButton.OnCheckedChangeListener _PushOnCheckedChange =
            new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    if (isChecked) {
                        try {
                            mMediaRecorder.startRecord(pushUrl);
                        } catch (Exception e) {
                        }
                        isRecording = true;
                    } else {
                        mMediaRecorder.stopRecord();
                        isRecording = false;
                    }
                }
            };

//    public void testPublish(boolean isPublish, final String url) {
//        if(isPublish) {
//            mMediaRecorder.startRecord(url);
//            Log.d(TAG, "Start Record Time:" + System.currentTimeMillis());
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    testPublish(false, url);
//                }
//            }, 10000);
//        }else {
//            mMediaRecorder.stopRecord();
//            Log.d(TAG, "Stop Record Time:" + System.currentTimeMillis());
//            mHandler.postDelayed(new Runnable() {
//                @Override
//                public void run() {
//                    testPublish(true, url);
//                }
//            }, 500);
//        }
//    }

    private Handler mHandler = new Handler();
    private GestureDetector mDetector;
    private ScaleGestureDetector mScaleDetector;
    private GestureDetector.OnGestureListener mGestureDetector = new GestureDetector.OnGestureListener() {
        @Override
        public boolean onDown(MotionEvent motionEvent) {
            return false;
        }

        @Override
        public void onShowPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onSingleTapUp(MotionEvent motionEvent) {
            if (mPreviewWidth > 0 && mPreviewHeight > 0) {
                float x = motionEvent.getX() / mPreviewWidth;
                float y = motionEvent.getY() / mPreviewHeight;
                mMediaRecorder.focusing(x, y);
            }
            return true;
        }

        @Override
        public boolean onScroll(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }

        @Override
        public void onLongPress(MotionEvent motionEvent) {

        }

        @Override
        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent1, float v, float v1) {
            return false;
        }
    };

    private View.OnTouchListener mOnTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View view, MotionEvent motionEvent) {
            mDetector.onTouchEvent(motionEvent);
            mScaleDetector.onTouchEvent(motionEvent);
            return true;
        }
    };

    private float scaleFactor = 1.0f;
    private ScaleGestureDetector.OnScaleGestureListener mScaleGestureListener = new ScaleGestureDetector.OnScaleGestureListener() {
        @Override
        public boolean onScale(ScaleGestureDetector scaleGestureDetector) {
            if (scaleGestureDetector.getScaleFactor() > 1) {
                scaleFactor += 1;
            } else {
                scaleFactor -= 1;
            }
            if (scaleFactor <= 1) scaleFactor = 1;
            mMediaRecorder.setZoom(scaleFactor);
            return true;
        }

        @Override
        public boolean onScaleBegin(ScaleGestureDetector scaleGestureDetector) {
            return true;
        }

        @Override
        public void onScaleEnd(ScaleGestureDetector scaleGestureDetector) {
        }
    };

    private void startPreview(final SurfaceHolder holder) {
        if (!mHasPermission) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    startPreview(holder);
                }
            }, PERMISSION_DELAY);
            return;
        }
        mMediaRecorder.prepare(mConfigure, _CameraSurface);
        mMediaRecorder.setPreviewSize(_CameraSurface.getMeasuredWidth(), _CameraSurface.getMeasuredHeight());
        if ((int) mConfigure.get(AlivcMediaFormat.KEY_CAMERA_FACING) == AlivcMediaFormat.CAMERA_FACING_FRONT) {
            mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_BEAUTY_ON);
        }
    }

    private final SurfaceHolder.Callback _CameraSurfaceCallback = new SurfaceHolder.Callback() {
        @Override
        public void surfaceCreated(SurfaceHolder holder) {
            holder.setKeepScreenOn(true);
//            mPreviewSurface = holder.getSurface();
            startPreview(_CameraSurface.getHolder());
        }

        @Override
        public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

            mMediaRecorder.setPreviewSize(width, height);
            mPreviewWidth = width;
            mPreviewHeight = height;
        }

        @Override
        public void surfaceDestroyed(SurfaceHolder holder) {
//            _CameraSurface = null;

            //下面2句话取消就可以实现后台推流 但是部分手机不支持
            //mMediaRecorder.stopRecord();
            // mMediaRecorder.reset();
        }
    };

    private OnRecordStatusListener mRecordStatusListener = new OnRecordStatusListener() {
        @Override
        public void onDeviceAttach() {
            ToastUtils.show(LiveCameraActivity.this, getString(R.string.camera_success));
        }

        @Override
        public void onDeviceAttachFailed(int facing) {
            ToastUtils.show(LiveCameraActivity.this, getString(R.string.camera_fail));
        }

        @Override
        public void onSessionAttach() {
            if (isRecording && !TextUtils.isEmpty(pushUrl)) {
                mMediaRecorder.startRecord(pushUrl);
            }
            mMediaRecorder.focusing(0.5f, 0.5f);
//            mMediaRecorder.addFlag(AlivcMediaFormat.FLAG_FRONT_CAMERA_MIRRORED);
        }

        @Override
        public void onSessionDetach() {

        }

        @Override
        public void onDeviceDetach() {

        }

        @Override
        public void onIllegalOutputResolution() {
            Log.d(TAG, "selected illegal output resolution");
            ToastUtils.show(LiveCameraActivity.this, R.string.illegal_output_resolution);
        }
    };


    private OnNetworkStatusListener mOnNetworkStatusListener = new OnNetworkStatusListener() {
        @Override
        public void onNetworkBusy() {
            Log.d("network_status", "==== on network busy ====");
            ToastUtils.show(LiveCameraActivity.this, getString(R.string.network_message));
        }

        @Override
        public void onNetworkFree() {
            ToastUtils.show(LiveCameraActivity.this, "network free");
            Log.d("network_status", "===== on network free ====");
        }

        @Override
        public void onConnectionStatusChange(int status) {
            Log.d(TAG, "ffmpeg Live stream connection status-->" + status);

            switch (status) {
                case AlivcStatusCode.STATUS_CONNECTION_START:
//                    ToastUtils.show(LiveCameraActivity.this, "Start live stream connection!");
                    Log.d(TAG, "Start live stream connection!");
                    break;
                case AlivcStatusCode.STATUS_CONNECTION_ESTABLISHED:
                    Log.d(TAG, "Live stream connection is established!");
//                    showIllegalArgumentDialog("链接成功");
//                    ToastUtils.show(LiveCameraActivity.this, "Live stream connection is established!");
                    break;
                case AlivcStatusCode.STATUS_CONNECTION_CLOSED:
                    Log.d(TAG, "Live stream connection is closed!");
//                    ToastUtils.show(LiveCameraActivity.this, "Live stream connection is closed!");
//                    mLiveRecorder.stop();
//                    mLiveRecorder.release();
//                    mLiveRecorder = null;
//                    mMediaRecorder.stopRecord();
                    break;
            }
        }

//        @Override
//        public void onFirstReconnect() {
//            ToastUtils.showToast(LiveCameraActivity.this, "首次重连");
//        }


        @Override
        public boolean onNetworkReconnectFailed() {
            Log.d(TAG, "Reconnect timeout, not adapt to living");
            ToastUtils.show(LiveCameraActivity.this, getString(R.string.quit_live));
            mMediaRecorder.stopRecord();
            showIllegalArgumentDialog(getString(R.string.reconnect_fail));
            return false;
        }
    };

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE:
                boolean hasPermission = true;
                for (int i = 0; i < permissions.length; i++) {
                    if (grantResults[i] == PackageManager.PERMISSION_DENIED) {
                        int toastTip = 0;
                        if (Manifest.permission.CAMERA.equals(permissions[i])) {
                            toastTip = R.string.no_camera_permission;
                        } else if (Manifest.permission.RECORD_AUDIO.equals(permissions[i])) {
                            toastTip = R.string.no_record_audio_permission;
                        }
                        if (toastTip != 0) {
                            ToastUtils.show(this, toastTip);
                            hasPermission = false;
                        }
                    }
                }
                mHasPermission = hasPermission;
                break;
        }
    }


    public void showIllegalArgumentDialog(String message) {
        if (illegalArgumentDialog == null) {
            illegalArgumentDialog = new AlertDialog.Builder(this)
                    .setPositiveButton(getString(R.string.ok), new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            illegalArgumentDialog.dismiss();
                        }
                    })
                    .setTitle(getString(R.string.dialog_title))
                    .create();
        }
        illegalArgumentDialog.dismiss();
        illegalArgumentDialog.setMessage(message);
        illegalArgumentDialog.show();
    }

    AlertDialog illegalArgumentDialog = null;

    private OnLiveRecordErrorListener mOnErrorListener = new OnLiveRecordErrorListener() {
        @Override
        public void onError(int errorCode) {
            Log.d(TAG, "Live stream connection error-->" + errorCode);

            switch (errorCode) {
                case AlivcStatusCode.ERROR_ILLEGAL_ARGUMENT:
                    showIllegalArgumentDialog(getString(R.string.error_message));
                case AlivcStatusCode.ERROR_SERVER_CLOSED_CONNECTION:
                case AlivcStatusCode.ERORR_OUT_OF_MEMORY:
                case AlivcStatusCode.ERROR_CONNECTION_TIMEOUT:
                case AlivcStatusCode.ERROR_BROKEN_PIPE:
                case AlivcStatusCode.ERROR_IO:
                case AlivcStatusCode.ERROR_NETWORK_UNREACHABLE:
//                    ToastUtils.show(LiveCameraActivity.this, "Live stream connection error-->" + errorCode);

                    break;

                default:
//                    ToastUtils.show(LiveCameraActivity.this, "Live stream connection error-->" + errorCode);
            }
        }
    };

    DataStatistics.ReportListener mReportListener = new DataStatistics.ReportListener() {
        @Override
        public void onInfoReport() {
            runOnUiThread(mLoggerReportRunnable);
        }
    };

    private Runnable mLoggerReportRunnable = new Runnable() {
        @Override
        public void run() {
            mRecordReporter = mMediaRecorder.getRecordReporter();
            if (mRecordReporter != null) {
                tv_video_capture_fps.setText(mRecordReporter.getInt(AlivcRecordReporter.VIDEO_CAPTURE_FPS) + "fps");
                tv_audio_encoder_fps.setText(mRecordReporter.getInt(AlivcRecordReporter.AUDIO_ENCODER_FPS) + "fps");
                tv_video_encoder_fps.setText(mRecordReporter.getInt(AlivcRecordReporter.VIDEO_ENCODER_FPS) + "fps");

                /**
                 * OUTPUT_BITRATE的单位是byte / s，所以转换成bps需要要乘8
                 */
                tv_output_bitrate.setText(mRecordReporter.getLong(AlivcRecordReporter.OUTPUT_BITRATE) + "Kbps");

                tv_av_output_diff.setText(mRecordReporter.getLong(AlivcRecordReporter.AV_OUTPUT_DIFF) + "microseconds");
                tv_audio_out_fps.setText(mRecordReporter.getInt(AlivcRecordReporter.AUDIO_OUTPUT_FPS) + "fps");
                tv_video_output_fps.setText(mRecordReporter.getInt(AlivcRecordReporter.VIDEO_OUTPUT_FPS) + "fps");
                //                tv_stream_publish_time = (TextView) findViewById(R.id.tv_video_capture_fps);
                //                tv_stream_server_ip = (TextView) findViewById(R.id.tv_video_capture_fps);
                tv_video_delay_duration.setText(mRecordReporter.getLong(AlivcRecordReporter.VIDEO_DELAY_DURATION) + "microseconds");
                tv_audio_delay_duration.setText(mRecordReporter.getLong(AlivcRecordReporter.AUDIO_DELAY_DURATION) + "microseconds");
                tv_video_cache_frame_cnt.setText(mRecordReporter.getInt(AlivcRecordReporter.VIDEO_CACHE_FRAME_CNT) + "");
                tv_audio_cache_frame_cnt.setText(mRecordReporter.getInt(AlivcRecordReporter.AUDIO_CACHE_FRAME_CNT) + "");
                tv_video_cache_byte_size.setText(mRecordReporter.getLong(AlivcRecordReporter.VIDEO_CACHE_BYTE_SIZE) + "byte");
                tv_audio_cache_byte_size.setText(mRecordReporter.getLong(AlivcRecordReporter.AUDIO_CACHE_BYTE_SIZE) + "byte");
                tv_video_frame_discard_cnt.setText(mRecordReporter.getInt(AlivcRecordReporter.VIDEO_FRAME_DISCARD_CNT) + "");
                tv_audio_frame_discard_cnt.setText(mRecordReporter.getInt(AlivcRecordReporter.AUDIO_FRAME_DISCARD_CNT) + "");
                tv_cur_video_bueaty_duration.setText(mRecordReporter.getLong(AlivcRecordReporter.CUR_VIDEO_BEAUTY_DURATION) + "ms");
                tv_cur_video_encoder_duration.setText(mRecordReporter.getLong(AlivcRecordReporter.CUR_VIDEO_ENCODER_DURATION) + "ms");
                tv_cur_video_encode_birate.setText(mRecordReporter.getInt(AlivcRecordReporter.CUR_VIDEO_ENCODE_BITRATE) + "kbps");

                tv_video_output_frame_count.setText(mRecordReporter.getInt(AlivcRecordReporter.VIDEO_OUTPUT_FRAME_COUNT) + "");
                tv_video_data.setText(mRecordReporter.getLong(AlivcRecordReporter.VIDEO_OUTPUT_DATA_SIZE) + "");
                tv_video_buffer_count.setText(mRecordReporter.getInt(AlivcRecordReporter.VIDEO_BUFFER_COUNT) + "");
                tv_audio_data.setText(mRecordReporter.getLong(AlivcRecordReporter.AUDIO_OUTPUT_DATA_SIZE) + "");
            }
        }
    };

    private AlivcEventResponse mBitrateUpRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Bundle bundle = event.getBundle();
            int preBitrate = bundle.getInt(AlivcEvent.EventBundleKey.KEY_PRE_BITRATE);
            int currBitrate = bundle.getInt(AlivcEvent.EventBundleKey.KEY_CURR_BITRATE);
            Log.d(TAG, "event->up bitrate, previous bitrate is " + preBitrate +
                    "current bitrate is " + currBitrate);
//            ToastUtils.show(LiveCameraActivity.this, "event->up bitrate, previous bitrate is " + preBitrate +
//                    "current bitrate is " + currBitrate);
        }
    };
    private AlivcEventResponse mBitrateDownRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Bundle bundle = event.getBundle();
            int preBitrate = bundle.getInt(AlivcEvent.EventBundleKey.KEY_PRE_BITRATE);
            int currBitrate = bundle.getInt(AlivcEvent.EventBundleKey.KEY_CURR_BITRATE);
            Log.d(TAG, "event->down bitrate, previous bitrate is " + preBitrate +
                    "current bitrate is " + currBitrate);
//            ToastUtils.show(LiveCameraActivity.this, "event->down bitrate, previous bitrate is " + preBitrate +
//                    "current bitrate is " + currBitrate);
        }
    };
    private AlivcEventResponse mAudioCaptureSuccRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event->audio recorder start success");
//            ToastUtils.show(LiveCameraActivity.this, "event->audio recorder start success");
        }
    };

    private AlivcEventResponse mVideoEncoderSuccRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event->video encoder start success");
//            ToastUtils.show(LiveCameraActivity.this, "event->video encoder start success");
        }
    };
    private AlivcEventResponse mVideoEncoderFailedRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event->video encoder start failed");
//            ToastUtils.show(LiveCameraActivity.this, "event->video encoder start failed");
        }
    };
    private AlivcEventResponse mVideoEncodeFrameFailedRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event->video encode frame failed");
//            ToastUtils.show(LiveCameraActivity.this, "event->video encode frame failed");
        }
    };


    private AlivcEventResponse mInitDoneRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event->live recorder initialize completely");
//            ToastUtils.show(LiveCameraActivity.this, "event->live recorder initialize completely");
        }
    };

    private AlivcEventResponse mDataDiscardRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Bundle bundle = event.getBundle();
            int discardFrames = 0;
            if (bundle != null) {
                discardFrames = bundle.getInt(AlivcEvent.EventBundleKey.KEY_DISCARD_FRAMES);
            }
            Log.d(TAG, "event->data discard, the frames num is " + discardFrames);
//            ToastUtils.show(LiveCameraActivity.this, "event->data discard, the frames num is " + discardFrames);
        }
    };

    private AlivcEventResponse mAudioCaptureOpenFailedRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event-> audio capture device open failed");
//            ToastUtils.show(LiveCameraActivity.this, "event-> audio capture device open failed");
        }
    };

    private AlivcEventResponse mAudioEncodeFrameFailedRes = new AlivcEventResponse() {
        @Override
        public void onEvent(AlivcEvent event) {
            Log.d(TAG, "event-> audio encode frame failed");
//            ToastUtils.show(LiveCameraActivity.this, "event-> audio encode frame failed");
        }
    };
}
