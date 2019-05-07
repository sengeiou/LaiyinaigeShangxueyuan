package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.CollectModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
/**
 * 课程收藏列表适配器
 * Created by zhengwencheng on 2018/1/29 0029.
 * package com.jianzhong.bs.adapter
 */

public class CourseCollectAdapter extends CommonAdapter<CollectModel> {
    private int isEdit;
    public CourseCollectAdapter(Context context, List<CollectModel> datas,int isEdit) {
        super(context, R.layout.item_course_collect, datas);

        this.isEdit = isEdit;
    }

    @Override
    protected void convert(ViewHolder holder, final CollectModel item, int position) {

        if(item.getIsSelected() == 1){
            holder.setChecked(R.id.cb_check,true);
        }else {
            holder.setChecked(R.id.cb_check,false);
        }

        if(isEdit == 1){
            holder.setVisible(R.id.cb_check,true);
        }else {
            holder.setVisible(R.id.cb_check,false);
        }

        if(item.getCourse() != null){
            holder.setText(R.id.tv_class_title, item.getCourse().getTitle())
                    .setText(R.id.tv_class_time, "时长：" + CommonUtils.secToTime(Integer.valueOf(item.getCourse().getDuration_sec())))
                    .setText(R.id.tv_class_count, "已有" + item.getCourse().getPlay_num() + "人观看");
            GlideUtils.load((ImageView) holder.getView(R.id.iv_class), item.getCourse().getCover());
            if (item.getCourse().getExpert() != null) {
                if (!TextUtils.isEmpty(item.getCourse().getExpert().getRealname()) && !TextUtils.isEmpty(item.getCourse().getExpert().getPos_duty())) {
                    holder.setText(R.id.tv_class_expert, item.getCourse().getExpert().getRealname() + " | " + item.getCourse().getExpert().getPos_duty());
                } else if (!TextUtils.isEmpty(item.getCourse().getExpert().getRealname()) && TextUtils.isEmpty(item.getCourse().getExpert().getPos_duty())) {
                    holder.setText(R.id.tv_class_expert, item.getCourse().getExpert().getRealname());
                } else if (TextUtils.isEmpty(item.getCourse().getExpert().getRealname()) && !TextUtils.isEmpty(item.getCourse().getExpert().getPos_duty())) {
                    holder.setText(R.id.tv_class_expert, item.getCourse().getExpert().getPos_duty());
                }
            }
        }

        holder.setOnClickListener(R.id.ll_class, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isEdit == 1){
                    if(item.getIsSelected() == 1){
                        item.setIsSelected(0);
                        for (String s: GroupVarManager.getInstance().mCollectList) {
                            if(s.equals(item.getItem_id())){
                                GroupVarManager.getInstance().mCollectList.remove(s);
                                break;
                            }
                        }
                    }else {
                        item.setIsSelected(1);
                        GroupVarManager.getInstance().mCollectList.add(item.getItem_id());
                    }
                    EventBus.getDefault().post(AppConstants.TAG_COURSE);
                    notifyDataSetChanged();
                }else {
                    Intent intent = new Intent(mContext, ClassDetailActivity.class);
                    intent.putExtra("course_id", item.getForeign_id());
                    mContext.startActivity(intent);
                }
            }
        });
    }

    public void setIsEdit(int isEdit){
        this.isEdit = isEdit;
    }
}
