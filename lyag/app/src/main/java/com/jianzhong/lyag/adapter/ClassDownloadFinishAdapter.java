package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.format.Formatter;
import android.view.View;
import android.widget.ImageView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.DownbodyModel;
import com.jianzhong.lyag.ui.exam.ClassDeatailSingleActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.lzy.okserver.download.DownloadInfo;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * Created by zhengwencheng on 2018/4/11 0011.
 * package com.jianzhong.bs.adapter
 */

public class ClassDownloadFinishAdapter extends CommonAdapter<DownloadInfo> {

    private int isEdit;

    public ClassDownloadFinishAdapter(Context context, List<DownloadInfo> datas, int isEdit) {
        super(context, R.layout.item_class_load_finish, datas);

        this.isEdit = isEdit;
    }

    @Override
    protected void convert(ViewHolder holder, final DownloadInfo item, int position) {

        if(isEdit == 1){
            holder.setVisible(R.id.cb_check,true);
        }else {
            holder.setVisible(R.id.cb_check,false);
        }

        final DownbodyModel info = (DownbodyModel) item.getData();

        if(info.getIsSelected() == 1){
            holder.setChecked(R.id.cb_check,true);
        }else {
            holder.setChecked(R.id.cb_check,false);
        }

        if(info != null){
            GlideUtils.load((ImageView) holder.getView(R.id.iv_class),info.getImg_url());
            holder.setText(R.id.tv_title,info.getTitle());
            holder.setText(R.id.tv_class_expert,info.getName()+"/"+info.getPost());
            holder.setText(R.id.tv_dry_duration, "时长：" + CommonUtils.secToTime(Integer.valueOf(info.getDuration_sec())));
        }

        holder.setText(R.id.tv_class_size,Formatter.formatFileSize(mContext, item.getTotalLength()));

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(isEdit == 1){
                    if(info.getIsSelected() == 1){
                        info.setIsSelected(0);
                        for (String s: GroupVarManager.getInstance().mCacheList) {
                            if(s.equals(item.getTaskKey())){
                                GroupVarManager.getInstance().mCacheList.remove(s);
                                break;
                            }
                        }
                    }else {
                        info.setIsSelected(1);
                        GroupVarManager.getInstance().mCacheList.add(item.getTaskKey());
                    }
                    EventBus.getDefault().post(AppConstants.TAG_CACHE_DEL);
                    notifyDataSetChanged();
                }else {
//                    Intent intent = new Intent(mContext, ClassDeatailActivity.class);
                    Intent intent = new Intent(mContext, ClassDeatailSingleActivity.class);
                    intent.putExtra("course_id",info.getHead_id());
                    intent.putExtra("url",info.getUrl());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public void setIsEdit(int isEdit){
        this.isEdit = isEdit;
    }
}
