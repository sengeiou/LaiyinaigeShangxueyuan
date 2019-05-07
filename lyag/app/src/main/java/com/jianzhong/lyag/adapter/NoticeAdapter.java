package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.NoticeListModel;
import com.jianzhong.lyag.ui.notice.NoticeDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
/**
 * 重要通知列表适配器
 * Created by zhengwencheng on 2018/2/26 0026.
 * package com.jianzhong.bs.adapter
 */

public class NoticeAdapter extends CommonAdapter<NoticeListModel> {
    public NoticeAdapter(Context context,List<NoticeListModel> datas) {
        super(context, R.layout.item_notice_list, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final NoticeListModel item, int position) {

        holder.setText(R.id.tv_title,item.getTitle());
        holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getCreate_at()));
        holder.setText(R.id.tv_content,item.getContent());
        holder.setText(R.id.tv_sign,CommonUtils.getTagStr(item.getTag()));
        holder.setText(R.id.tv_like,item.getLike_num());
        holder.setText(R.id.tv_comment,item.getComment_num());

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext,NoticeDetailActivity.class);
                intent.putExtra("notice_id",item.getNotice_id());
                mContext.startActivity(intent);
            }
        });
    }
}
