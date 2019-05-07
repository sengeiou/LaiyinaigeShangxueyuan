package com.jianzhong.lyag;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentTabHost;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.baselib.util.ActivityManager;
import com.baselib.util.ListUtils;
import com.baselib.util.StringUtils;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.db.dao.AudioRecordDao;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.helper.JurisdictionHelper;
import com.jianzhong.lyag.logic.CommonLogic;
import com.jianzhong.lyag.model.AudioDetailModel;
import com.jianzhong.lyag.model.MainDataModel;
import com.jianzhong.lyag.ui.exam.DryCargoDetailActivity;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.util.MediaPlayerUtils;
import com.tencent.bugly.Bugly;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.rong.imkit.RongIM;
import io.rong.imlib.RongIMClient;
import io.rong.imlib.model.UserInfo;

import static com.jianzhong.lyag.util.MediaPlayerUtils.mMediaPlayer;

public class MainActivity extends ToolbarActivity {

    @BindView(android.R.id.tabhost)
    FragmentTabHost mTabhost;
    @BindView(R.id.iv_cover)
    ImageView mIvCover;
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_dsc)
    TextView mTvDsc;
    @BindView(R.id.ll_audio)
    LinearLayout mLlAudio;
    private MainDataModel mainDataModel;
    private AudioRecordDao mAudioRecordDao;  //音频播放记录的数据库

    private AudioDetailModel item;
    private int isNormalFinish = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        EventBus.getDefault().register(this);
        //初始化升级的sdk
        Bugly.init(this, "", false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        EventBus.getDefault().unregister(this);
        if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
            mMediaPlayer.stop();
        }
    }

    @Override
    public void initData() {
        super.initData();
        mAudioRecordDao = new AudioRecordDao(mContext);
        initTab();
        initPush();
        if (!StringUtils.isEmpty(AppUserModel.getInstance().getmLoginInfoModel().getRc_token())) {
            connect(AppUserModel.getInstance().getmLoginInfoModel().getRc_token());
        }
    }

    /**
     * 判断是否未点击推送进来的
     */
    private void initPush() {
        Bundle bundle = getIntent().getBundleExtra(AppConstants.KEY_PUSH_MSG);
        if (bundle != null) {
            String assetType = bundle.getString(AppConstants.KEY_PUSH_ASSET_TYPE);
            String foreignId = bundle.getString(AppConstants.KEY_PUSH_FOREIGN_ID);
            CommonLogic.startActivityPush(mContext, assetType, foreignId, true);
        }
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getAudioDetail(AudioDetailModel item) {
        this.item = item;
        isNormalFinish = 0;
        if (item != null) {
            if(MediaPlayerUtils.getInstance().getMediaPlayer() != null && MediaPlayerUtils.getInstance().isPlaying()){
                MediaPlayerUtils.getInstance().stop();
                GroupVarManager.getInstance().mAudioDetailModel = null;
            }
            if (item.getIsPlay() == 0) {
                GroupVarManager.getInstance().mAudioDetailModel = null;
                mLlAudio.setVisibility(View.GONE);
            }else {
                GroupVarManager.getInstance().mAudioDetailModel = item;
                mTvTitle.setText(item.getTitle());
                GlideUtils.load(mIvCover,item.getCover());
                mLlAudio.setVisibility(View.VISIBLE);
                MediaPlayerUtils.getInstance().initStart(this, item.getAudio_cdn_url(), 0, new MediaPlayer.OnPreparedListener() {
                    @Override
                    public void onPrepared(MediaPlayer mp) {
                        isNormalFinish = 1;
                        setSeekBar();
                        MediaPlayerUtils.getInstance().start();
                    }
                }, new MediaPlayer.OnCompletionListener() {
                    @Override
                    public void onCompletion(MediaPlayer mp) {
                        if(isNormalFinish == 1){
                            Message msg = new Message();
                            msg.what = UPDATE_MEDIA_VIEW;
                            handler.sendMessage(msg);
                        }
                    }
                });
            }
        }
    }

    private static final int UPDATE_MEDIA_VIEW = 1009;
    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() { //更新UI 必须通知主线程更新
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPDATE_MEDIA_VIEW) {
                mLlAudio.setVisibility(View.GONE);
                EventBus.getDefault().post(AppConstants.TAG_CLOSE_AUDIO);
            }
        }
    };

    //音频课程设置进度条
    private void setSeekBar() {
        new Thread(new Runnable() {
            public void run() {
                while (MediaPlayerUtils.getInstance().getMediaPlayer() != null && MediaPlayerUtils.getInstance().getMediaPlayer().getCurrentPosition() < MediaPlayerUtils.getInstance().getMediaPlayer().getDuration()) {
                    if (MediaPlayerUtils.getInstance().getMediaPlayer().isPlaying()) {
                        final String currentTime = MediaPlayerUtils.getInstance().getCurrentTime();
                        runOnUiThread(new Runnable() {
                            @SuppressLint("SetTextI18n")
                            @Override
                            public void run() {
                                mTvDsc.setText(currentTime + "  "+item.getSub_title());
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
    protected void onResume() {
        super.onResume();

        if(mMediaPlayer != null && mMediaPlayer.isPlaying() && GroupVarManager.getInstance().mAudioDetailModel != null){
            EventBus.getDefault().post(AppConstants.TAG_UPDATE_AUDIO);
            mLlAudio.setVisibility(View.VISIBLE);
            mTvTitle.setText(GroupVarManager.getInstance().mAudioDetailModel.getTitle());
            GlideUtils.load(mIvCover,GroupVarManager.getInstance().mAudioDetailModel.getCover());
            item = GroupVarManager.getInstance().mAudioDetailModel;
            setSeekBar();
        }else {
            mLlAudio.setVisibility(View.GONE);
        }
    }

    /**
     * 获取音频播放记录
     *
     * @return
     */
    private int getAudioRecord(AudioDetailModel item) {
        int playRecord = 0;
        Map<String, Object> map = new HashMap<>();
        map.put("audio_id", item.getAudio_id());
        if (!ListUtils.isEmpty(mAudioRecordDao.queryForValue(map)) && mAudioRecordDao.queryForValue(map).get(0).getAudio_record() != 0) {
            playRecord = (int) mAudioRecordDao.queryForValue(map).get(0).getAudio_record();
        }

        return playRecord;
    }


    @OnClick({R.id.ll_close, R.id.ll_audio})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_close:
                mLlAudio.setVisibility(View.GONE);
                if(mMediaPlayer != null && mMediaPlayer.isPlaying()){
                    mMediaPlayer.stop();
                }
                GroupVarManager.getInstance().mAudioDetailModel = null;
                EventBus.getDefault().post(AppConstants.TAG_CLOSE_AUDIO);
                break;
            case R.id.ll_audio:
                Intent intent = new Intent(mContext,DryCargoDetailActivity.class);
                intent.putExtra("audio_id",item.getAudio_id());
                startActivity(intent);
                break;
        }
    }

    /**
     * 初始化底部栏
     */
    private void initTab() {
        //实例化TabHost对象，得到TabHost
        mTabhost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mainDataModel = JurisdictionHelper.getMainDataTab();
        // 分隔线
        mTabhost.getTabWidget().setDividerDrawable(R.color.color_white);
        //得到fragment的个数
        int count = mainDataModel.getFragmentArray().size();
        for (int i = 0; i < count; i++) {
            //为每一个Tab按钮设置图标、文字和内容
            TabHost.TabSpec tabSpec = mTabhost.newTabSpec(mainDataModel.getTextViewArray().get(i)).setIndicator(getTabItemView(i));
            //将Tab按钮添加进Tab选项卡中
            mTabhost.addTab(tabSpec, mainDataModel.getFragmentArray().get(i), null);
            //设置Tab按钮的背景
            mTabhost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_home_tab_bg);
        }
    }

    /**
     * 给Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = mInflater.inflate(R.layout.item_home_tab, null);

        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mainDataModel.getImageViewArray().get(index));

        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mainDataModel.getTextViewArray().get(index));

        return view;
    }

    private long preTime = 0;

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - preTime > 2000) {
            Toast.makeText(this, "再按一次退出程序", Toast.LENGTH_SHORT).show();
            preTime = System.currentTimeMillis();
        } else {
            ActivityManager.getActivityManager().finishAllActivity();
            // 杀掉进程，退出系统
        }
    }

    private void connect(String token) {
        if (getApplicationInfo().packageName.equals(BaseApplication.getCurProcessName(getApplicationContext())) ||
                "io.rong.push".equals(BaseApplication.getCurProcessName(getApplicationContext()))) {

            RongIMClient.connect(token, new RongIMClient.ConnectCallback() {
                /**
                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
                 *  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
                 */
                @Override
                public void onTokenIncorrect() {

                }

                /**
                 * 连接融云成功
                 * @param userid 当前 token 对应的用户 id
                 */
                @Override
                public void onSuccess(String userid) {
                    RongIM.getInstance().setCurrentUserInfo(new UserInfo(AppUserModel.getInstance().getmUserModel().getUser_id()
                            ,AppUserModel.getInstance().getmUserModel().getRealname(), Uri.parse(AppUserModel.getInstance().getmUserModel().getAvatar())));
                    RongIM.getInstance().setMessageAttachedUserInfo(true);
                }

                /**
                 * 连接融云失败
                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
                 */
                @Override
                public void onError(RongIMClient.ErrorCode errorCode) {

                }
            });
        }
    }
}
