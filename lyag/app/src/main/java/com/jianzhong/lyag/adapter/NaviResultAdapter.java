package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.baselib.util.StringUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.NaviResultListModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;
import com.jianzhong.lyag.ui.exam.ColumnDetailActivity;
import com.jianzhong.lyag.ui.exam.DryCargoDetailActivity;
import com.jianzhong.lyag.ui.live.LiveDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.util.MediaPlayerUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 导航列表搜索结果适配器
 * Created by zhengwencheng on 2018/1/29 0029.
 * package com.jianzhong.bs.adapter
 */

public class NaviResultAdapter extends CommonAdapter<NaviResultListModel> {
    private String tag;

    public NaviResultAdapter(Context context, List<NaviResultListModel> datas, String tag) {
        super(context, R.layout.item_navi_result, datas);
        this.tag = tag;
    }

    @Override
    protected void convert(ViewHolder holder, final NaviResultListModel item, int position) {
        if (tag.equals(AppConstants.TAG_COURSE)) {    //课程分类
            holder.setVisible(R.id.ll_class, true);
            holder.setVisible(R.id.ll_dry_cargo, false);
            holder.setVisible(R.id.ll_special, false);
            holder.setVisible(R.id.ll_live, false);

            holder.setText(R.id.tv_class_title, item.getTitle())
                    .setText(R.id.tv_class_expert, item.getExpert().getRealname() + "/" + item.getExpert().getPos_duty())
                    .setText(R.id.tv_class_time, "总时长："+ CommonUtils.secToTime(Integer.valueOf(item.getDuration_sec())))
                    .setText(R.id.tv_class_count, item.getPlay_num() + "");
            GlideUtils.load((ImageView) holder.getView(R.id.iv_class), item.getCover());
            holder.setOnClickListener(R.id.ll_class, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent  = new Intent(mContext,ClassDetailActivity.class);
                    intent.putExtra("course_id",item.getAsset_id());
                    mContext.startActivity(intent);
                }
            });

        } else if (tag.equals(AppConstants.TAG_AUDIO)) { //音频
            holder.setVisible(R.id.ll_class, false);
            holder.setVisible(R.id.ll_dry_cargo, true);
            holder.setVisible(R.id.ll_special, false);
            holder.setVisible(R.id.ll_live, false);
            holder.setText(R.id.tv_dry_title, item.getTitle())
                    .setText(R.id.tv_dry_time, CommonUtils.getDryTime(item.getCreate_at()))
                    .setText(R.id.tv_dry_duration, "时长：" + CommonUtils.secToTime2(Integer.valueOf(item.getDuration_sec())));

            //分享
            holder.setOnClickListener(R.id.tv_dry_share, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ToastUtils.show(mContext, "分享一下");
                }
            });

            //播放
            holder.setOnClickListener(R.id.iv_play, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                    ToastUtils.show(mContext, "播放一下");
                }
            });

            //查看详情
            holder.setOnClickListener(R.id.ll_dry_cargo, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(GroupVarManager.getInstance().mAudioDetailModel != null && !GroupVarManager.getInstance().mAudioDetailModel.getAudio_id().equals(item.getAsset_id())){
                        if(MediaPlayerUtils.getInstance().getMediaPlayer() != null && MediaPlayerUtils.getInstance().isPlaying()){
                            GroupVarManager.getInstance().mAudioDetailModel = null;
                            MediaPlayerUtils.getInstance().stop();
                        }
                    }

                    Intent intent  = new Intent(mContext,DryCargoDetailActivity.class);
                    intent.putExtra("audio_id",item.getAsset_id());
                    mContext.startActivity(intent);
                }
            });
        } else if (tag.equals(AppConstants.TAG_COLUMN)) { //专栏
            holder.setVisible(R.id.ll_class, false);
            holder.setVisible(R.id.ll_dry_cargo, false);
            holder.setVisible(R.id.ll_special, true);
            holder.setVisible(R.id.ll_live, false);

            GlideUtils.load((ImageView) holder.getView(R.id.iv_special), item.getCover());
            holder.setText(R.id.tv_special_title, item.getTitle())
                    .setText(R.id.tv_special_explain, item.getBrief())
                    .setText(R.id.tv_special_identify, item.getExpert().getRealname() + "/" + item.getExpert().getPos_duty())
                    .setText(R.id.tv_special_update, item.getNow_ep() + "/" + item.getTotal_ep())
                    .setText(R.id.tv_special_update_time, item.getUpdate_day())
                    .setText(R.id.tv_special_subscribe, "已有" + item.getPlay_num() + "人观看");

            //获取组件的宽度
//            holder.getView(R.id.ll_tag).measure(0, 0);
//            int width = (holder.getView(R.id.ll_tag).getMeasuredWidth() - DeviceInfoUtil.dip2px(mContext, 8)) / 2;
//            CommonUtils.reSizeTextView((TextView) holder.getView(R.id.tv_special_update_time), "每周" + item.getUpdate_day() + "更新", width);
            holder.setOnClickListener(R.id.ll_special, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(tag.equals(AppConstants.TAG_COURSE)){
                        Intent intent = new Intent(mContext, ClassDetailActivity.class);
                        intent.putExtra("course_id",item.getAsset_id());
                        mContext.startActivity(intent);
                    }else if(tag.equals(AppConstants.TAG_AUDIO)){
                        Intent intent = new Intent(mContext, DryCargoDetailActivity.class);
                        intent.putExtra("audio_id",item.getAsset_id());
                        mContext.startActivity(intent);
                    }else if(tag.equals(AppConstants.TAG_COLUMN)){
                        Intent intent = new Intent(mContext, ColumnDetailActivity.class);
                        intent.putExtra("column_id",item.getAsset_id());
                        mContext.startActivity(intent);
                    }

                }
            });
        }else if(tag.equals(AppConstants.TAG_LIVE)){
            holder.setVisible(R.id.ll_class, false);
            holder.setVisible(R.id.ll_dry_cargo, false);
            holder.setVisible(R.id.ll_special, false);
            holder.setVisible(R.id.ll_live, true);

            holder.setText(R.id.tv_live_title,item.getTitle());
            if(item.getExpert() != null){
                holder.setText(R.id.tv_live_expert,item.getExpert().getRealname()+"/"+item.getExpert().getPos_duty());
            }
            holder.setText(R.id.tv_live_time, CommonUtils.getDryTime(item.getStart_at()));
            GlideUtils.load((ImageView) holder.getView(R.id.iv_live),item.getCover());
            if(!StringUtils.isEmpty(item.getIs_publish())){
                if(item.getIs_publish().equals("0")){ //预告
                    holder.setText(R.id.tv_count,"已有"+ item.getOnline_user_num()+"人收藏");
                    holder.setImageDrawable(R.id.iv_tag,mContext.getResources().getDrawable(R.drawable.homepage_label1));
                }else if(item.getIs_publish().equals("1") && item.getIs_finish().equals("0")){ //直播
                    holder.setText(R.id.tv_count,item.getOnline_user_num()+"人正在观看");
                    holder.setImageDrawable(R.id.iv_tag,mContext.getResources().getDrawable(R.drawable.homepage_label2));
                }else if(item.getIs_publish().equals("1") && item.getIs_finish().equals("1")){
                    holder.setText(R.id.tv_count,"已有"+ item.getOnline_user_num()+"人观看");
                    holder.setImageDrawable(R.id.iv_tag,mContext.getResources().getDrawable(R.drawable.homepage_label3));
                }
            }

            holder.setOnClickListener(R.id.ll_live, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext,LiveDetailActivity.class);
                    intent.putExtra("live_id",item.getAsset_id());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    public void setTag(String tag) {
        this.tag = tag;
    }
}
