package com.jianzhong.lyag.ui.live;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.livecloud.live.AlivcMediaFormat;
import com.baselib.util.DeviceInfoUtil;
import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.baselib.widget.CustomListView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.AvatarAdapter;
import com.jianzhong.lyag.adapter.LiveRecordAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.LiveDetailModel;
import com.jianzhong.lyag.model.LiveWatcherModel;
import com.jianzhong.lyag.model.RecordUrlModel;
import com.jianzhong.lyag.model.UserModel;
import com.jianzhong.lyag.ui.exam.MemberConActivity;
import com.jianzhong.lyag.ui.live.livepush.LiveCameraActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.DialogUtil;

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
 * 直播详情
 * Created by zhengwencheng on 2018/3/14 0014.
 * package com.jianzhong.bs.ui.live
 */

public class LiveDetailFragment extends BaseFragment {

    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.tv_identify)
    TextView mTvIdentify;
    @BindView(R.id.text_summary)
    TextView mTextSummary;
    @BindView(R.id.tv_status)
    TextView mTvStatus;
    @BindView(R.id.iv_spread)
    ImageView mIvSpread;
    @BindView(R.id.iv_shrink_up)
    ImageView mIvShrinkUp;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ll_collect)
    LinearLayout mLlCollect;
    @BindView(R.id.ll_live_room)
    LinearLayout mLlLiveRoom;
    @BindView(R.id.tv_record)
    TextView mTvRecord;
    @BindView(R.id.tv_expert)
    TextView mTvExpert;
    @BindView(R.id.tv_count_txt)
    TextView mTvCountTxt;
    @BindView(R.id.ll_watch)
    LinearLayout mLlWatch;
    @BindView(R.id.lv_section)
    CustomListView mLvSection;
    @BindView(R.id.ll_publish)
    LinearLayout mLlPublish;

    private List<RecordUrlModel> mList = new ArrayList<>();
    private LiveRecordAdapter mAdapter;
    private static final int SHRINK_UP_STATE = 1; // 收起状态
    private static final int SPREAD_STATE = 2; // 展开状态
    private static int mState = SPREAD_STATE;//默认收起状态
    private static final int VIDEO_CONTENT_DESC_MAX_LINE = 2;// 默认展示最大行数2行

    private LiveDetailModel data;

    private AvatarAdapter mAvatarAdapter;
    private List<UserModel> mContactsModels = new ArrayList<>();

    private String totalNUm;

    public static LiveDetailFragment newInstance(LiveDetailModel data) {

        Bundle args = new Bundle();
        LiveDetailFragment fragment = new LiveDetailFragment();
        args.putSerializable("data", data);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_live_detail, null);
        return mRootView;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateLiveSection(LiveDetailModel data) {
        if (data != null && !ListUtils.isEmpty(data.getRecord_url())) {
            mList.clear();
            mList.addAll(data.getRecord_url());
            mAdapter.notifyDataSetChanged();
        }
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

    @Override
    protected void initData() {
        super.initData();
        data = (LiveDetailModel) getArguments().getSerializable("data");

        initController();

    }

    /**
     * 给控件初始化数据
     */
    @SuppressLint("SetTextI18n")
    private void initController() {
        mTvTitle.setText(data.getTitle());
        if(data.getExpert() != null){
            mTvExpert.setText("主播："+data.getExpert().getRealname());
        }
        mTvIdentify.setText(CommonUtils.getExpertDontent(data.getDocent()));
        mTextSummary.setText("简介：" + data.getSummary());
        mTvCount.setText(data.getOnline_user_num());
        if (!data.getPush_user_id().equals(AppUserModel.getInstance().getmUserModel().getUser_id()) || data.getIs_finish().equals("1") || data.getIs_publish().equals("0")) {
            mLlLiveRoom.setVisibility(View.GONE);
        }
        if(data.getIs_publish().equals("1") && data.getIs_finish().equals("1")){
            mTvCount.setVisibility(View.GONE);
            mTvCountTxt.setVisibility(View.GONE);
            mLlWatch.setVisibility(View.GONE);
            mLvSection.setVisibility(View.VISIBLE);
            mTvRecord.setText("分段直播回放");
            if(!ListUtils.isEmpty(data.getRecord_url())){
                mList.addAll(data.getRecord_url());
            }
            mAdapter = new LiveRecordAdapter(mContext,mList);
            mLvSection.setAdapter(mAdapter);
        }else if(data.getIs_publish().equals("0")){
            mLlPublish.setVisibility(View.GONE);
        }else {
            getNoticeUnreadUser();
        }
    }

    /**
     * 获取通知未读人列表
     */
    private void getNoticeUnreadUser() {
        Map<String, Object> params = new HashMap<>();
        params.put("live_id", data.getLive_id());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_LIVE_WATCHER_SIMPLE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<LiveWatcherModel> result = GsonUtils.json2Bean(s, LiveWatcherModel.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS && result != null) {
                    totalNUm = result.getData().getTotal();
                    mTvCount.setText(totalNUm);
                    if(totalNUm.equals("0")){
                        mLlPublish.setVisibility(View.GONE);
                    }else {
                        if (mAvatarAdapter == null) {
                            mContactsModels.addAll(result.getData().getList());
                            LinearLayoutManager ms = new LinearLayoutManager(getActivity());
                            ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
                            mRecyclerView.setLayoutManager(ms);  //给RecyClerView 添加设置好的布局样式
                            mAvatarAdapter = new AvatarAdapter(mContext, mContactsModels);
                            mRecyclerView.setAdapter(mAvatarAdapter);
                        } else {
                            mContactsModels.clear();
                            mContactsModels.addAll(result.getData().getList());
                            mAvatarAdapter.notifyDataSetChanged();
                        }
                    }
                } else {
//                    mIvAll
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    @OnClick({R.id.ll_status, R.id.iv_all, R.id.ll_collect, R.id.ll_live_room})
    public void onViewClicked(View view) {
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
            case R.id.iv_all:
                Intent intent = new Intent(mContext, MemberConActivity.class);
                intent.putExtra("live_id", data.getLive_id());
                intent.putExtra("title", "观看直播用户");
                startActivity(intent);
                break;
            case R.id.ll_collect:
                if (data.getHas_favor().equals("1")) {
                    cancelCollect();
                } else {
                    addCollect();
                }
                break;
            case R.id.ll_live_room:
                DialogUtil.getInstance().showLiveTip(getActivity(), totalNUm,data);
                break;
        }
    }

    /**
     * 添加收藏
     */
    private void addCollect() {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", data.getAsset_type());
        params.put("foreign_id", data.getLive_id());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_ADD_FAVOR, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<String> result = GsonUtils.json2Bean(s, String.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    mLlCollect.setSelected(true);
                    data.setHas_favor("1");
                }
                ToastUtils.show(mContext, result.getMessage());
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 添加收藏
     */
    private void cancelCollect() {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", data.getAsset_type());
        params.put("foreign_id", data.getLive_id());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DEL_FAVOR, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    mLlCollect.setSelected(false);
                    data.setHas_favor("0");
                }
                ToastUtils.show(mContext, result.getMessage());
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 初始化直播的参数
     */
    private void initLive() {
        data.setPush_url("rtmp://video-center.alivecdn.com/lovica/Stream2?vhost=live.lovica.com.cn&auth_key=1521200826-0-0-91e0c5c8c376a3a177432bf805cbb726");
        int videoResolution;
        int cameraFrontFacing;
        videoResolution = AlivcMediaFormat.OUTPUT_RESOLUTION_540P; //默认设置540p
        cameraFrontFacing = AlivcMediaFormat.CAMERA_FACING_BACK;   //默认后置摄像头
        boolean screenOrientation = true;       //默认设置横屏
        if (StringUtils.isEmpty(data.getPush_url())) {
            ToastUtils.show(mContext, "Push url is null");
            return;
        }

        String watermark = data.getCover(); //水印图片
        int dx = 14; //水平和垂直的margin
        int dy = 14;
        int site = 1; //水印图片位置
        int minBitrate = 500;
        int maxBitrate = 800;
        int bestBitrate = 600;
        int initBitrate = 600;
        int watermarkWidth = 50;
        int watermarkHeight;
//        BitmapFactory.Options options = loadWatermarkInfo(watermark);
//        if(options == null) {
//            return;
//        }
        watermarkHeight = DeviceInfoUtil.getScreenWidth(mContext);
        int frameRate = 30;

        LiveCameraActivity.RequestBuilder builder = new LiveCameraActivity.RequestBuilder()
                .bestBitrate(bestBitrate)
                .cameraFacing(cameraFrontFacing)
                .dx(dx)
                .dy(dy)
                .site(site)
                .rtmpUrl(data.getPush_url())
                .videoResolution(videoResolution)
                .portrait(screenOrientation)
                .watermarkUrl(watermark)
                .minBitrate(minBitrate)
                .maxBitrate(maxBitrate)
                .frameRate(frameRate)
                .watermarkWidth(watermarkWidth)
                .watermarkHeight(watermarkHeight)
                .initBitrate(initBitrate);

        LiveCameraActivity.startActivity(mContext, builder);
    }

}
