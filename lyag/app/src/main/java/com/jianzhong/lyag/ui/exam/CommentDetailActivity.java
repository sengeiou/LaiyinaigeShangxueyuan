package com.jianzhong.lyag.ui.exam;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.DisplayMetrics;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.InputMethodUtil;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.baselib.widget.CustomListView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.CommentSubAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnCommentSubListener;
import com.jianzhong.lyag.model.CommentModel;
import com.jianzhong.lyag.model.CommentSubModel;
import com.jianzhong.lyag.model.ReplyModel;
import com.jianzhong.lyag.model.UserModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.DialogHelper;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.util.MediaPlayerUtils;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.jianzhong.lyag.util.MediaPlayerUtils.mMediaPlayer;

/**
 * 评论详情
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.ui.exam
 */
public class CommentDetailActivity extends ToolbarActivity {

    @BindView(R.id.iv_avatar)
    ImageView mIvAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_time)
    TextView mTvTime;
    @BindView(R.id.tv_content)
    TextView mTvContent;
    @BindView(R.id.lv_comment)
    CustomListView mLvComment;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.tv_count)
    TextView mTvCount;
    @BindView(R.id.iv_play)
    View mIvPlay;
    @BindView(R.id.tv_audio_time)
    TextView mTvAudioTime;
    @BindView(R.id.layout_recorder_length)
    FrameLayout mLayoutRecorderLength;
    @BindView(R.id.ll_recorder)
    LinearLayout mLlRecorder;
    @BindView(R.id.nine_grid_view)
    NineGridView mNineGridView;
    @BindView(R.id.tv_like)
    TextView mTvLike;
    @BindView(R.id.tv_adopt)
    TextView mTvAdopt;
    @BindView(R.id.tv_comment_count)
    TextView mTvCommentCount;

    private String foreign_id;
    private String comment_type;
    private CommentModel data;
    private CommentSubAdapter mAdapter;
    private LinkedList<CommentSubModel> mList = new LinkedList<>();

    private String content = "";
    private String top_id; //评论的时候使用 默认为0 回复别人时不为0
    private int isDelSub = 0;

    private int mMinItemWith;// 设置语音对话框的最大宽度和最小宽度
    private int mMaxItemWith;

    private int isAdopt;
    //type:1是课程评论详情 其他为0
    private int type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment_detail);
        ButterKnife.bind(this);
    }


    @Override
    public void initData() {
        super.initData();

        foreign_id = getIntent().getStringExtra("foreign_id");
        comment_type = getIntent().getStringExtra("comment_type");
        top_id = getIntent().getStringExtra("top_id");
        data = (CommentModel) getIntent().getSerializableExtra("commentModel");
        isAdopt = getIntent().getIntExtra("isAdopt", 0);
        type = getIntent().getIntExtra("type",0);

        setHeadTitle("评论详情");
        showHeadTitle();

        initController();
    }


    private void initController() {
        if (data == null) {
            return;
        }
        hideCommonLoading();
        initVideoWith();
        if (data.getUser() != null) {
            GlideUtils.loadAvatarImage(mIvAvatar, data.getUser().getAvatar());
            mTvName.setText(data.getUser().getRealname());
            if (data.getUser().getUser_id().equals(AppUserModel.getInstance().getmUserModel().getUser_id())) {
                setHeadRight("删除");
                showHeadRight();
            }
        }

        if(type == 0){
            if (isAdopt == 0) {
                mTvLike.setVisibility(View.VISIBLE);
                mTvLike.setText(data.getLike_num());
                if (data.getHas_like().equals("1")) {
                    mTvLike.setSelected(true);
                } else {
                    mTvLike.setSelected(false);
                }
            } else {
                mTvAdopt.setVisibility(View.VISIBLE);
                mTvAdopt.setText(data.getLike_num());
                if (data.getHas_like().equals("1")) {
                    mTvAdopt.setSelected(true);
                } else {
                    mTvAdopt.setSelected(false);
                }
            }
        }


        if (!StringUtils.isEmpty(data.getContent())) {
            mTvContent.setVisibility(View.VISIBLE);
            mTvContent.setText(data.getContent());
        } else {
            mTvContent.setVisibility(View.GONE);
        }

        if (!StringUtils.isEmpty(data.getAudio_url())) {
            mLlRecorder.setVisibility(View.VISIBLE);
            ViewGroup.LayoutParams lParams = mLayoutRecorderLength.getLayoutParams();
            lParams.width = (int) (mMinItemWith + mMaxItemWith / 300f * Float.valueOf(data.getAudio_duration_sec()));
            mLayoutRecorderLength.setLayoutParams(lParams);
            mTvAudioTime.setText(data.getAudio_duration_sec() + "s");

            mLayoutRecorderLength.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playVoice(data.getAudio_url());
                }
            });
        } else {
            mLlRecorder.setVisibility(View.GONE);
        }

        if (!ListUtils.isEmpty(data.getImages())) {
            mNineGridView.setVisibility(View.VISIBLE);
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            List<String> imageDetails = data.getImages();
            for (String imageDetail : imageDetails) {
                ImageInfo info = new ImageInfo();
                if (imageDetail != null) {
                    info.setBigImageUrl(imageDetail);
                    info.setThumbnailUrl(imageDetail);
                }
                imageInfo.add(info);
            }
            //查看九宫格图片
            CommonUtils.NineGridViewPicShow(mContext, imageInfo, mNineGridView);
        } else {
            mNineGridView.setVisibility(View.VISIBLE);
        }

        mTvTime.setText(CommonUtils.getDryTime(data.getCreate_at()));
        mList.addAll(data.getSub());
        mTvCount.setText("共" + mList.size() + "条评论");
        mTvCommentCount.setText(mList.size() + "");
        mAdapter = new CommentSubAdapter(mContext, mList, new OnCommentSubListener() {
            @Override
            public void OnClick(final CommentSubModel item, final int position) {
                new AlertDialog.Builder(CommentDetailActivity.this).setTitle("提示").
                        setMessage("您确认删除该评论吗").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                isDelSub = 1;
                                delCommentSub(item, position);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();
            }
        }, isAdopt,type);
        mLvComment.setAdapter(mAdapter);

        //评论
        mEtContent.setHint("请输入评论");
        mEtContent.clearFocus();
        // 评论动态
        mEtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if (!StringUtils.isEmpty(mEtContent.getText().toString().trim()) && !content.equals(mEtContent.getText().toString().trim())) {
                        content = mEtContent.getText().toString().trim();
                        Map<String, Object> params = new HashMap<>();
                        params.put("foreign_id", foreign_id);
                        params.put("content", content);
                        params.put("top_id", top_id);
                        params.put("asset_type", comment_type);
                        params.put("asset_user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
                        addComment(params);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });
    }

    //播放语音时动画的View
    private View viewanim;

    private void playVoice(String audio) {
        if (viewanim != null) {// 让第二个播放的时候第一个停止播放
            viewanim.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.hd_fx_play));
            viewanim = null;
        }
        viewanim = findViewById(R.id.iv_play);
        viewanim.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.hd_fx_stop));

        if (mMediaPlayer != null && mMediaPlayer.isPlaying()) {
            MediaPlayerUtils.getInstance().stop();
            viewanim.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.hd_fx_play));
        } else {
            MediaPlayerUtils.getInstance().initStart(mContext, audio, 0, new MediaPlayer.OnPreparedListener() {
                @Override
                public void onPrepared(MediaPlayer mp) {
                    viewanim.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.hd_fx_stop));
                }
            }, new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    ((Activity) mContext).runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            viewanim.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.hd_fx_play));
                        }
                    });
                }
            });
        }
    }

    /**
     * 初始化音频的长度
     */
    private void initVideoWith() {
        // 获取系统宽度
        WindowManager wManager = (WindowManager) mContext.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.8f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.2f);
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
                Result<ReplyModel> result = GsonUtils.json2Bean(s, ReplyModel.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    ToastUtils.show(mContext, "评论成功");
                    mEtContent.setText("");
                    mEtContent.setHint("请输入评论");
                    mEtContent.clearFocus();
                    CommentSubModel item = new CommentSubModel();
                    item.setContent(content);
                    item.setComment_id(result.getData().getComment_id());
                    item.setCreate_at(result.getData().getCreate_at());
                    item.setHas_like("0");
                    UserModel userModel = new UserModel();
                    userModel.setAvatar(AppUserModel.getInstance().getmUserModel().getAvatar());
                    userModel.setRealname(AppUserModel.getInstance().getmUserModel().getRealname());
                    userModel.setUser_id(AppUserModel.getInstance().getmUserModel().getUser_id());
                    item.setUser(userModel);
                    mTvCount.setText("共" + mList.size() + "条评论");
                    mTvCommentCount.setText(mList.size() + "");
                    mList.addFirst(item);
                    mAdapter.notifyDataSetChanged();
                    InputMethodUtil.hiddenInputMethod(mContext, mEtContent);
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

    @OnClick(R.id.head_right)
    public void onViewClicked() {
        new AlertDialog.Builder(CommentDetailActivity.this).setTitle("提示").
                setMessage("您确认删除该评论吗").
                setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        isDelSub = 0;
                        delComment(data);
                    }
                }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                return;
            }
        }).create().show();
    }

    /**
     * 删除评论
     *
     * @param
     */
    private void delComment(CommentModel item) {
        Map<String, Object> params = new HashMap<>();
        params.put("comment_id", item.getComment_id());
        params.put("top_id", item.getTop_id());
        params.put("asset_type", item.getAsset_type());
        params.put("foreign_id", foreign_id);
        DialogHelper.showDialog(mContext);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COMMENT_DEL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                DialogHelper.dismissDialog();
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    GroupVarManager.getInstance().isUpdateComment = 1;
                    CommentDetailActivity.this.finish();

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

    /**
     * 删除评论
     *
     * @param
     */
    private void delCommentSub(CommentSubModel item, final int position) {
        Map<String, Object> params = new HashMap<>();
        params.put("comment_id", item.getComment_id());
        params.put("top_id", top_id);
        params.put("asset_type", data.getAsset_type());
        params.put("foreign_id", foreign_id);
        DialogHelper.showDialog(mContext);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COMMENT_DEL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                DialogHelper.dismissDialog();
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    if (isDelSub == 0) {
                        GroupVarManager.getInstance().isUpdateComment = 1;
                        CommentDetailActivity.this.finish();
                    } else {
                        mList.remove(position);
                        mAdapter.notifyDataSetChanged();
                    }
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

    @OnClick({R.id.tv_adopt, R.id.tv_like})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_adopt:
                if (data.getHas_like().equals("1")) {
                    cancelLike();
                } else {
                    getLike();
                }
                break;
            case R.id.tv_like:
                if (data.getHas_like().equals("1")) {
                    cancelLike();
                } else {
                    getLike();
                }
                break;
        }
    }

    private void getLike() {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", data.getSelf_type());
        params.put("foreign_id", data.getComment_id());
        params.put("asset_type_out", data.getAsset_type());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DO_LIKE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    GroupVarManager.getInstance().isUpdateComment = 1;
                    data.setHas_like("1");
                    data.setLike_num((Integer.valueOf(data.getLike_num()) + 1) + "");
                    if (isAdopt == 0) {
                        ToastUtils.show(mContext, result.getMessage());
                        mTvLike.setText(data.getLike_num());
                        mTvLike.setSelected(true);
                    } else {
                        ToastUtils.show(mContext, "采纳成功");
                        mTvAdopt.setText(data.getLike_num());
                        mTvAdopt.setSelected(true);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 取消点赞
     *
     * @param
     */
    private void cancelLike() {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", data.getSelf_type());
        params.put("foreign_id", data.getComment_id());
        params.put("asset_type_out", data.getAsset_type());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DIS_LIKE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    GroupVarManager.getInstance().isUpdateComment = 1;
                    data.setHas_like("0");
                    data.setLike_num((Integer.valueOf(data.getLike_num()) - 1) + "");
                    if (isAdopt == 0) {
                        ToastUtils.show(mContext, result.getMessage());
                        mTvLike.setText(data.getLike_num());
                        mTvLike.setSelected(false);
                    } else {
                        ToastUtils.show(mContext, "已取消采纳");
                        mTvAdopt.setText(data.getLike_num());
                        mTvAdopt.setSelected(false);
                    }
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }

}
