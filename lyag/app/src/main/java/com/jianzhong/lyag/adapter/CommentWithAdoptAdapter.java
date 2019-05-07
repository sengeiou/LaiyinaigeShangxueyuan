package com.jianzhong.lyag.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnCommentListener;
import com.jianzhong.lyag.model.CommentModel;
import com.jianzhong.lyag.ui.exam.CommentDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.util.MediaPlayerUtils;
import com.lzy.ninegrid.ImageInfo;
import com.lzy.ninegrid.NineGridView;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.jianzhong.lyag.util.MediaPlayerUtils.mMediaPlayer;

/**
 * 评论列表适配器
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.adapter
 */
public class CommentWithAdoptAdapter extends CommonAdapter<CommentModel> {
    private String notice_id;
    private OnCommentListener listener;

    private int mMinItemWith;// 设置语音对话框的最大宽度和最小宽度
    private int mMaxItemWith;

    public CommentWithAdoptAdapter(Context context, List<CommentModel> datas, String notice_id, OnCommentListener listener) {
        super(context, R.layout.item_comment_with_adopt, datas);

        this.notice_id = notice_id;
        this.listener = listener;

        // 获取系统宽度
        WindowManager wManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics outMetrics = new DisplayMetrics();
        wManager.getDefaultDisplay().getMetrics(outMetrics);
        mMaxItemWith = (int) (outMetrics.widthPixels * 0.8f);
        mMinItemWith = (int) (outMetrics.widthPixels * 0.2f);
    }

    @Override
    protected void convert(final ViewHolder holder, final CommentModel item, final int position) {
        if (item.getUser() != null) {
            GlideUtils.loadAvatarImage((ImageView) holder.getView(R.id.iv_avatar), item.getUser().getAvatar());
            holder.setText(R.id.tv_name, item.getUser().getRealname());
            if (item.getUser().getUser_id().equals(AppUserModel.getInstance().getmUserModel().getUser_id())) {
                holder.setVisible(R.id.iv_del, true);
            } else {
                holder.setVisible(R.id.iv_del, false);
            }
        }

        if (item.getHas_like().equals("1")) {
            holder.getView(R.id.tv_like).setSelected(true);
        } else {
            holder.getView(R.id.tv_like).setSelected(false);
        }
        holder.setText(R.id.tv_like, item.getLike_num());
        holder.setText(R.id.tv_count, item.getReply_num());
        holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getCreate_at()));

        if(!StringUtils.isEmpty(item.getContent())){
            holder.setVisible(R.id.tv_content, true);
            holder.setText(R.id.tv_content, item.getContent());
        }else {
            holder.setVisible(R.id.tv_content, false);
        }

        if(!StringUtils.isEmpty(item.getAudio_url())){
            holder.setVisible(R.id.ll_recorder, true);
            ViewGroup.LayoutParams lParams = holder.getView(R.id.layout_recorder_length).getLayoutParams();
            lParams.width = (int) (mMinItemWith + mMaxItemWith / 300f * Float.valueOf(item.getAudio_duration_sec()));
            holder.getView(R.id.layout_recorder_length).setLayoutParams(lParams);
            holder.setText(R.id.tv_audio_time, item.getAudio_duration_sec() + "s");
            holder.setOnClickListener(R.id.layout_recorder_length, new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    playVoice(item.getAudio_url(), holder.getConvertView());
                }
            });
        }else {
            holder.setVisible(R.id.ll_recorder, false);
        }

        if(!ListUtils.isEmpty(item.getImages())){
            holder.setVisible(R.id.nine_grid_view,true);
            holder.setVisible(R.id.ll_recorder, false);
            holder.setVisible(R.id.nine_grid_view,true);
            ArrayList<ImageInfo> imageInfo = new ArrayList<>();
            List<String> imageDetails = item.getImages();
            for (String imageDetail : imageDetails) {
                ImageInfo info = new ImageInfo();
                if (imageDetail != null) {
                    info.setBigImageUrl(imageDetail);
                    info.setThumbnailUrl(imageDetail);
                }
                imageInfo.add(info);
            }
            //查看九宫格图片
            CommonUtils.NineGridViewPicShow(mContext, imageInfo, (NineGridView) holder.getView(R.id.nine_grid_view));
        }else {
            holder.setVisible(R.id.nine_grid_view,false);
        }

        holder.setOnClickListener(R.id.tv_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mDatas.get(position).getHas_like().equals("0")) {
                    getLike(position,item);
                } else {
                    cancelLike(position,item);
                }
            }
        });

        holder.setOnClickListener(R.id.iv_del, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnClick(item, position);
            }
        });

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentDetailActivity.class);
                intent.putExtra("foreign_id", notice_id);
                intent.putExtra("comment_type", item.getAsset_type());
                intent.putExtra("top_id", item.getComment_id());
                intent.putExtra("commentModel", item);
                intent.putExtra("isAdopt",1);
                mContext.startActivity(intent);
            }
        });
    }

    private void getLike(final int position,CommentModel item) {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", item.getSelf_type());
        params.put("foreign_id", item.getComment_id());
        params.put("asset_type_out", item.getAsset_type());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DO_LIKE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    ToastUtils.show(mContext,"采纳成功");
                    mDatas.get(position).setHas_like("1");
                    mDatas.get(position).setLike_num((Integer.valueOf(mDatas.get(position).getLike_num()) + 1) + "");
                    notifyDataSetChanged();
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
     * @param position
     */
    private void cancelLike(final int position,CommentModel item) {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", item.getSelf_type());
        params.put("foreign_id", item.getComment_id());
        params.put("asset_type_out", item.getAsset_type());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DIS_LIKE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    ToastUtils.show(mContext,"已取消采纳");
                    mDatas.get(position).setHas_like("0");
                    mDatas.get(position).setLike_num((Integer.valueOf(mDatas.get(position).getLike_num()) - 1) + "");
                    notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }


    //播放语音时动画的View
    private View viewanim;

    private void playVoice(String audio,View view) {

        if (viewanim != null) {// 让第二个播放的时候第一个停止播放
            viewanim.setBackgroundDrawable(mContext.getResources().getDrawable(R.drawable.hd_fx_play));
            viewanim = null;
        }
        viewanim = view.findViewById(R.id.iv_play);
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
}
