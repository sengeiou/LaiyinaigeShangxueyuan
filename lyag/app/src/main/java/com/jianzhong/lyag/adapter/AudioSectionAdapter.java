package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.view.View;

import com.baselib.util.ListUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.listener.OnAudioClassListener;
import com.jianzhong.lyag.model.DownbodyModel;
import com.jianzhong.lyag.model.SectionModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.lzy.okgo.OkGo;
import com.lzy.okgo.request.GetRequest;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.db.DownloadDBManager;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.ArrayList;
import java.util.List;

/**
 * 课时分段列表适配器
 * Created by Max on 2018/2/27.
 */
public class AudioSectionAdapter extends CommonAdapter<SectionModel> {
    private OnAudioClassListener listener;
    private String cover;
    public AudioSectionAdapter(Context context, List<SectionModel> datas, String cover,OnAudioClassListener listener) {
        super(context, R.layout.item_section, datas);

        this.cover = cover;
        this.listener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, final SectionModel item, final int position) {

        holder.setText(R.id.tv_title,item.getTitle());
        holder.setText(R.id.tv_duration_sec, CommonUtils.secToTime(Integer.valueOf(item.getDuration_sec())));

        if(item.getIsPlay() == 1){
            holder.setImageDrawable(R.id.iv_play,mContext.getResources().getDrawable(R.drawable.spbf_stop2));
            holder.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.color_theme));
        }else {
            holder.setImageDrawable(R.id.iv_play,mContext.getResources().getDrawable(R.drawable.spbf_play2));
            holder.setTextColor(R.id.tv_title,mContext.getResources().getColor(R.color.color_333333));
        }

        holder.setOnClickListener(R.id.ll_play, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < mDatas.size(); i++) {
                    if(i != position){
                        mDatas.get(i).setIsPlay(0);
                    }else {
                        if(mDatas.get(i).getIsPlay() == 1){
                            mDatas.get(i).setIsPlay(0);
                        }else {
                            mDatas.get(i).setIsPlay(1);
                        }
                    }
                }
                notifyDataSetChanged();
                listener.callBack(position,item);
            }
        });

        holder.setOnClickListener(R.id.ll_download, new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (BaseApplication.mDownloadManager.getDownloadInfo(item.getAudio_cdn_url()) != null) {
                    ToastUtils.show(mContext, "任务已经在下载列表中");
                    DownloadInfo mDownloadInfo = BaseApplication.mDownloadManager.getDownloadInfo(item.getAudio_cdn_url());
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
                    info.setHead_id(item.getCourse_id());
                    info.setUrl(item.getAudio_cdn_url());
                    info.setTitle(item.getTitle());
                    info.setDuration_sec(item.getDuration_sec());
                    info.setSection_id(item.getSection_id());
                    info.setImg_url(cover);
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
            }
        });
    }
}
