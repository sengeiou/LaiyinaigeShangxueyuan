package com.jianzhong.lyag.ui.notice;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.baselib.util.FileUtils;
import com.baselib.util.GsonUtils;
import com.baselib.util.InputMethodUtil;
import com.baselib.util.ListUtils;
import com.baselib.util.LogUtil;
import com.baselib.util.Result;
import com.baselib.util.ResultList;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.baselib.widget.CustomListView;
import com.baselib.widget.xlistview.XScrollView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.AvatarAdapter;
import com.jianzhong.lyag.adapter.CommentWithLikeAdapter;
import com.jianzhong.lyag.adapter.AttachmentAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnCommentListener;
import com.jianzhong.lyag.listener.SoftKeyBoardListener;
import com.jianzhong.lyag.model.CommentListModel;
import com.jianzhong.lyag.model.CommentModel;
import com.jianzhong.lyag.model.ImgCompressModel;
import com.jianzhong.lyag.model.NoticeDetailModel;
import com.jianzhong.lyag.model.UserModel;
import com.jianzhong.lyag.record.AudioButton;
import com.jianzhong.lyag.ui.exam.MemberConActivity;
import com.jianzhong.lyag.ui.interact.CommentImgTxtActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.DialogHelper;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.util.MD5Utils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;

import net.bither.util.imagecompressor.XCImageCompressor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 重要通知详情
 * Created by zhengwencheng on 2018/2/26 0026.
 * package com.jianzhong.bs.ui.notice
 */

public class NoticeDetailActivity extends ToolbarActivity {

    @BindView(R.id.head_title)
    TextView mHeadTitle;
    @BindView(R.id.scroll_view)
    XScrollView mScrollView;
    @BindView(R.id.ll_comment)
    LinearLayout mLlComment;
    @BindView(R.id.ll_ngvi)
    LinearLayout mLlNgvi;
    @BindView(R.id.ll_input)
    LinearLayout mLlInput;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.ll_collect)
    LinearLayout mLlCollect;
    @BindView(R.id.btn_record_voice)
    AudioButton mBtnRecordVoice;
    @BindView(R.id.iv_keyboard)
    ImageView mIvKeyboard;
    @BindView(R.id.iv_voice_record)
    ImageView mIvVoiceRecord;


    private View mView; //要添加的头view
    private ViewHolder mViewHolder;
    private String notice_id;
    private NoticeDetailModel data;
    private AvatarAdapter mAvatarAdapter;
    private List<UserModel> mContactsModels = new ArrayList<>();
    private LinkedList<CommentModel> mCommentModels = new LinkedList<>();
    private CommentWithLikeAdapter mCommentAdapter;
    private String content = "";
    private int pageIndex = 1;
    private String top_id = "0"; //评论的时候使用 默认为0 回复别人时不为0

    //是否切换了语音录制
    private int isAudioRecord = 0;
    private InputMethodManager imm;
    private String audioTime;
    private String cacheName;
    //添加图片评论
    private ArrayList<ImageItem> mCommentImgList = new ArrayList<>();
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private int maxImgCount = 3;
    private List<String> mCommentImgUrlList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_notice_detail);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();
        notice_id = getIntent().getStringExtra("notice_id");
        mHeadTitle.setText("通知详情");
        getNoticeDetail();

        //获取评论
        getCommnetList();
        initListener();
    }

    private void initListener() {

        //键盘收起的监听
        SoftKeyBoardListener.setListener(NoticeDetailActivity.this, new SoftKeyBoardListener.OnSoftKeyBoardChangeListener() {
            @Override
            public void keyBoardShow(int height) {

            }

            @Override
            public void keyBoardHide(int height) {
                if (isAudioRecord == 0) {
                    mLlNgvi.setVisibility(View.VISIBLE);
                    mLlInput.setVisibility(View.GONE);
                } else {
                    mLlNgvi.setVisibility(View.GONE);
                    mLlInput.setVisibility(View.VISIBLE);
                }
            }
        });

        // 评论动态
        mEtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!StringUtils.isEmpty(mEtContent.getText().toString().trim()) && !content.equals(mEtContent.getText().toString().trim())) {
                        content = mEtContent.getText().toString().trim();
                        Map<String, Object> params = new HashMap<>();
                        params.put("foreign_id", notice_id);
                        params.put("content", content);
                        params.put("top_id", top_id);
                        params.put("asset_user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
                        params.put("asset_type", data.getAsset_type());
                        addComment(params);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    /**
     * 获取通知详情
     */
    private void getNoticeDetail() {
        Map<String, Object> params = new HashMap<>();
        params.put("notice_id", notice_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_NOTICE_DETAIL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<NoticeDetailModel> result = GsonUtils.json2Bean(s, NoticeDetailModel.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    mScrollView.stopRefresh();
                    data = result.getData();
                    initController();
                    getNoticeUnreadUser();
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage() : "数据解析错误");
                }
                isShowNoDataView(data);
            }

            @Override
            public void onFailure(String msg) {
                showErrorView();
            }
        });
    }

    /**
     * 初始化View
     */
    private void initController() {
        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(true);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {
            @Override
            public void onRefresh() {
                getNoticeDetail();
                //
                getNoticeUnreadUser();
                //获取评论
                mCommentModels.clear();
                pageIndex = 1;
                getCommnetList();
            }

            @Override
            public void onLoadMore() {
                //
                pageIndex++;
                //获取评论
                getCommnetList();
            }
        });
        mScrollView.setRefreshTime(CommonUtils.getTime());

        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.content_notice_detail, null);
            mViewHolder = new ViewHolder(mView);
            mScrollView.setView(mView);
        }

        mViewHolder.mLvComment.setFocusable(false);
        mViewHolder.mRecyclerView.setFocusable(false);
        mScrollView.setFocusable(true);
        GlideUtils.loadAvatarImage(mViewHolder.mIvAvatar, data.getAvatar());
        mViewHolder.mTvName.setText(data.getOperator_name());
        mViewHolder.mTvIdentify.setText(data.getOperator_duty());
        mViewHolder.mTvTitle.setText(data.getTitle());
        mViewHolder.mTvContent.setText(data.getContent());
        mViewHolder.mTvSign.setText(CommonUtils.getTagStr(data.getTag()));
        mViewHolder.mTvEditTime.setText(CommonUtils.getDryTime(data.getUpdate_at()) + "最后编辑");
        //设置九宫格图片
        ArrayList<ImageInfo> imageInfo = new ArrayList<>();
        List<String> imageDetails = data.getImages();
        if (!ListUtils.isEmpty(imageDetails)) {
            for (String imageDetail : imageDetails) {
                ImageInfo info = new ImageInfo();
                if (imageDetail != null) {
                    info.setBigImageUrl(imageDetail);
                    info.setThumbnailUrl(imageDetail);
                }
                imageInfo.add(info);
            }
            //查看九宫格图片
            CommonUtils.NineGridViewPicShow(mContext, imageInfo, mViewHolder.mNineGridView);
        } else {
            mViewHolder.mNineGridView.setVisibility(View.GONE);
        }

        //点击录制语音
        mBtnRecordVoice.setOnFinishListener(new AudioButton.onFinishListener() {
            @Override
            public void finishRecord(String path, float time) {
                if (FileUtils.isFileExist(path)) {
//                    File file = new File(path);
                    audioTime = (int) time + "";
                    String md5String = MD5Utils.encode(System.currentTimeMillis() + Math.random() + "");
                    uploadAudioToOSS(md5String + ".wav", path);
                }
            }
        });

        // 附件列表
        if (!ListUtils.isEmpty(data.getAttachment())) {
            AttachmentAdapter mAdapter = new AttachmentAdapter(mContext, data.getAttachment());
            mViewHolder.mRvAttachment.setLayoutManager(new LinearLayoutManager(mContext));
            mViewHolder.mRvAttachment.setAdapter(mAdapter);
            mViewHolder.mRvAttachment.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.mRvAttachment.setVisibility(View.GONE);
        }
    }


    /**
     * 上传音频到oss
     *
     * @param audioName
     * @param audioPath
     */
    private void uploadAudioToOSS(String audioName, String audioPath) {
        cacheName = AppConstants.AUDIO_OBJECT_KEY + CommonUtils.getDate() + "/" + audioName;
        PutObjectRequest put = new PutObjectRequest(AppConstants.BUCKET, cacheName, audioPath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

            }
        });

        OSSAsyncTask task = GroupVarManager.getInstance().oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {

                Message msg = new Message();
                msg.what = UPLOAD_AUDIO_COMMENT;
                handler.sendMessage(msg);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    LogUtil.e("ErrorCode", serviceException.getErrorCode());
                    LogUtil.e("RequestId", serviceException.getRequestId());
                    LogUtil.e("HostId", serviceException.getHostId());
                    LogUtil.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        task.waitUntilFinished();
    }

    private static final int UPLOAD_AUDIO_COMMENT = 1001;

    @SuppressLint("HandlerLeak")
    private Handler handler = new Handler() { //更新UI 必须通知主线程更新
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == UPLOAD_AUDIO_COMMENT) {
                String audioPath = HttpConfig.PIC_URL_BASE_GET + cacheName;
                Map<String, Object> params = new HashMap<>();
                params.put("foreign_id", notice_id);
                params.put("content", "");
                params.put("top_id", top_id);
                params.put("asset_user_id", data.getUser_id());
                params.put("asset_type", data.getAsset_type());
                params.put("audio_url", audioPath);
                params.put("audio_duration_sec", audioTime);
                addComment(params);
            }
        }
    };

    @Override
    protected void onResume() {
        super.onResume();
        if (GroupVarManager.getInstance().isUpdateComment == 1) {
            GroupVarManager.getInstance().isUpdateComment = 0;
            pageIndex = 1;
            mCommentModels.clear();
            getCommnetList();
        }
    }

    @OnClick({R.id.head_ll_back, R.id.iv_share, R.id.iv_menu, R.id.ll_collect, R.id.ll_comment, R.id.iv_keyboard, R.id.iv_voice_record, R.id.iv_file_add})
    public void onViewClicked(View view) {
        Intent intent;
        imm = (InputMethodManager) mEtContent.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        switch (view.getId()) {
            case R.id.head_ll_back:
                NoticeDetailActivity.this.finish();
                break;
            case R.id.iv_share:

                break;
            case R.id.iv_menu:

                break;
            case R.id.ll_collect:
                if (data.getHas_favor().equals("1")) {
                    cancelCollect();
                } else {
                    addCollect();
                }

                break;
            case R.id.ll_comment:
                mLlNgvi.setVisibility(View.GONE);
                mLlInput.setVisibility(View.VISIBLE);
                mEtContent.setHint("请输入评论");
                mEtContent.requestFocus();
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;

            case R.id.iv_keyboard:    //切换文本输入
                mEtContent.setVisibility(View.VISIBLE);
                mIvKeyboard.setVisibility(View.GONE);
                mBtnRecordVoice.setVisibility(View.GONE);
                mIvVoiceRecord.setVisibility(View.VISIBLE);
                isAudioRecord = 0;
                imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);
                break;
            case R.id.iv_voice_record: //切换语语音录制
                mIvKeyboard.setVisibility(View.VISIBLE);
                mIvVoiceRecord.setVisibility(View.GONE);
                mEtContent.setVisibility(View.GONE);
                mEtContent.clearFocus();
                mBtnRecordVoice.setVisibility(View.VISIBLE);
                isAudioRecord = 1;
                imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                break;
            case R.id.iv_file_add:
                intent = new Intent(mContext, CommentImgTxtActivity.class);
                intent.putExtra("foreign_id", notice_id);
                intent.putExtra("asset_type", data.getAsset_type());
                startActivity(intent);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                mCommentImgList.addAll(images);

                //返回来直接压缩上传
                compressHelpImg();
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                mCommentImgList.clear();
                mCommentImgList.addAll(images);
            }
        }
    }

    private List<ImgCompressModel> mCommnetImgFile = new ArrayList<>();

    /**
     * 压缩
     */
    private void compressHelpImg() {
        if (!ListUtils.isEmpty(mCommentImgList)) {
            CommonUtils.compress(mCommentImgList, new XCImageCompressor.ImageCompressListener() {
                @Override
                public void onSuccess(List<String> outFilePathList) {
                    LogUtil.d("tag", outFilePathList.toString());
                    mCommnetImgFile.clear();
                    for (int i = 0; i < outFilePathList.size(); i++) {
//                        File f = new File(outFilePathList.get(i));
                        String md5String = MD5Utils.encode(System.currentTimeMillis() + Math.random() + "");
                        ImgCompressModel item = new ImgCompressModel();
                        item.setImgPath(outFilePathList.get(i));
                        item.setMd5Sting(md5String);
                        mCommnetImgFile.add(item);
                    }

                    getImgUpload(mCommnetImgFile);
                }

                @Override
                public void onFailure(String message) {
                    LogUtil.d("tag", message);
                }
            });
        }
    }

    /**
     * 先上传完图片 再添加
     *
     * @param mList
     */
    private void getImgUpload(List<ImgCompressModel> mList) {
        for (int i = 0; i < mList.size(); i++) {
            uploadPicToOSS(mList.get(i).getMd5Sting() + ".jpg", mList.get(i).getImgPath());
        }
        while (mCommentImgUrlList.size() < mList.size()) {
            return;
        }

        Map<String, Object> params = new HashMap<>();
        params.put("foreign_id", notice_id);
        params.put("content", content);
        params.put("top_id", top_id);
        params.put("asset_user_id", data.getUser_id());
        params.put("asset_type", data.getAsset_type());
        params.put("audio_url", "");
        params.put("audio_duration_sec", "0");
        params.put("images", GsonUtils.toJson(mCommentImgUrlList));
        addComment(params);
    }

    /**
     * 上传图片到oss
     *
     * @param imgaeName
     * @param imagePath
     */
    private void uploadPicToOSS(String imgaeName, String imagePath) { //
        final String cacheName = AppConstants.HELP_OBJECT_KEY + CommonUtils.getDate() + "/" + imgaeName;
        PutObjectRequest put = new PutObjectRequest(AppConstants.BUCKET, cacheName, imagePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

            }
        });

        OSSAsyncTask task = GroupVarManager.getInstance().oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                DialogHelper.dismissDialog();
                mCommentImgUrlList.add(HttpConfig.PIC_URL_BASE_GET + cacheName);
            }

            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    LogUtil.e("ErrorCode", serviceException.getErrorCode());
                    LogUtil.e("RequestId", serviceException.getRequestId());
                    LogUtil.e("HostId", serviceException.getHostId());
                    LogUtil.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        task.waitUntilFinished();
    }


    class ViewHolder {
        @BindView(R.id.tv_title)
        TextView mTvTitle;
        @BindView(R.id.iv_avatar)
        CircleImageView mIvAvatar;
        @BindView(R.id.tv_name)
        TextView mTvName;
        @BindView(R.id.tv_identify)
        TextView mTvIdentify;
        @BindView(R.id.tv_content)
        TextView mTvContent;
        @BindView(R.id.tv_sign)
        TextView mTvSign;
        @BindView(R.id.tv_edit_time)
        TextView mTvEditTime;
        @BindView(R.id.nine_grid_view)
        NineGridView mNineGridView;
        @BindView(R.id.recycler_view)
        RecyclerView mRecyclerView;
        @BindView(R.id.tv_all)
        TextView mTvAll;
        @BindView(R.id.lv_comment)
        CustomListView mLvComment;
        @BindView(R.id.rv_attachment)
        RecyclerView mRvAttachment;
        @BindView(R.id.ll_like_layout)
        LinearLayout llLikeLayout;
        @BindView(R.id.tv_like_title)
        TextView tvLikeTitle;

        @OnClick({R.id.tv_all})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.tv_all:
                    Intent intent = new Intent(NoticeDetailActivity.this, MemberConActivity.class);
                    intent.putExtra("title", "未收看通知的用户全部");
                    intent.putExtra("notice_id", notice_id);
                    startActivity(intent);
                    break;
            }
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    /**
     * 添加收藏
     */
    private void addCollect() {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", data.getAsset_type());
        params.put("foreign_id", notice_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_ADD_FAVOR, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<String> result = GsonUtils.json2Bean(s, String.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    ToastUtils.show(mContext, result.getMessage());
                    mLlCollect.setSelected(true);
                    data.setHas_favor("1");
                } else {

                }
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
        params.put("foreign_id", notice_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DEL_FAVOR, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    mLlCollect.setSelected(false);
                    data.setHas_favor("0");
                    ToastUtils.show(mContext, result.getMessage());
                } else {

                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 获取通知未读人列表
     */
    private void getNoticeUnreadUser() {
        Map<String, Object> params = new HashMap<>();
        params.put("notice_id", notice_id);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_NOTICE_UNREAD_USER, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<UserModel> resultList = GsonUtils.json2List(s, UserModel.class);
                if (resultList.getCode() == HttpConfig.STATUS_SUCCESS && resultList != null) {
                    if (mAvatarAdapter == null) {
                        mContactsModels.addAll(resultList.getData());
                        if (ListUtils.isEmpty(mContactsModels)) {
                            mViewHolder.llLikeLayout.setVisibility(View.GONE);
                        } else {
                            mViewHolder.llLikeLayout.setVisibility(View.VISIBLE);
                            mViewHolder.tvLikeTitle.setText("未阅读的学员");
                            LinearLayoutManager ms = new LinearLayoutManager(NoticeDetailActivity.this);
                            ms.setOrientation(LinearLayoutManager.HORIZONTAL);// 设置 recyclerview 布局方式为横向布局
                            mViewHolder.mRecyclerView.setLayoutManager(ms);  //给RecyClerView 添加设置好的布局样式
                            mAvatarAdapter = new AvatarAdapter(mContext, mContactsModels);
                            mViewHolder.mRecyclerView.setAdapter(mAvatarAdapter);
                        }
                    } else {
                        mContactsModels.clear();
                        mContactsModels.addAll(resultList.getData());
                        mAvatarAdapter.notifyDataSetChanged();
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

    /**
     * 添加新的评论
     *
     * @param params
     */
    private void addComment(Map<String, Object> params) {
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COMMENT_ADD, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                DialogHelper.dismissDialog();
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    InputMethodUtil.hiddenInputMethod(mContext, mViewHolder.mLvComment);
                    ToastUtils.show(mContext, "评论成功");
                    mEtContent.setText("");
                    mEtContent.setHint("请输入评论");
                    mEtContent.clearFocus();
                    //刷新评论列表 先这样 后面优化不刷新
                    pageIndex = 1;
                    mCommentModels.clear();
                    getCommnetList();
                } else {
                    ToastUtils.show(mContext, result.getMessage());
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
     * 获取评论列表
     */
    private void getCommnetList() {
        Map<String, Object> params = new HashMap<>();
        params.put("foreign_id", notice_id);
        params.put("asset_type", AppConstants.TAG_NOTICE);
        params.put("p", pageIndex + "");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COMMNET_LIST, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<CommentListModel> resultList = GsonUtils.json2Bean(s, CommentListModel.class);
                mScrollView.stopLoadMore();
                if (resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    mCommentModels.addAll(resultList.getData().getList());
                    if (mCommentAdapter == null) {
                        mCommentAdapter = new CommentWithLikeAdapter(mContext, mCommentModels, notice_id, new OnCommentListener() {
                            @Override
                            public void OnClick(final CommentModel item, final int position) {
                                new AlertDialog.Builder(NoticeDetailActivity.this).setTitle("提示").
                                        setMessage("您确认删除该评论吗").
                                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int which) {
                                                delComment(item.getComment_id(), position);
                                            }
                                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        return;
                                    }
                                }).create().show();
                            }
                        });
                        mViewHolder.mLvComment.setAdapter(mCommentAdapter);
                    } else {
                        mCommentAdapter.notifyDataSetChanged();
                    }

                } else {
                    ToastUtils.show(mContext, resultList != null ? resultList.getMessage() : "数据解析错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 删除评论
     *
     * @param comment_id
     */
    private void delComment(String comment_id, final int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("comment_id", comment_id);
        params.put("top_id", top_id);
        params.put("asset_type", data.getAsset_type());
        params.put("foreign_id", notice_id);
        DialogHelper.showDialog(mContext);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COMMENT_DEL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                DialogHelper.dismissDialog();
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    pageIndex = 1;
                    mCommentModels.clear();
                    getCommnetList();
                } else {
                    ToastUtils.show(mContext, result != null ? result.getMessage() : "");
                }
            }

            @Override
            public void onFailure(String msg) {
                DialogHelper.dismissDialog();
                ToastUtils.show(mContext, msg);
            }
        });
    }
}
