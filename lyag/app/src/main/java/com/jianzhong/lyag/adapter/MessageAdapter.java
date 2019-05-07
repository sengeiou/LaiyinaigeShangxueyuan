package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.view.View;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.logic.CommonLogic;
import com.jianzhong.lyag.model.MessageModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by chenzhikai on 2018/4/25.
 */

public class MessageAdapter extends CommonAdapter<MessageModel> {

    public MessageAdapter(Context context, List<MessageModel> datas) {
        super(context, R.layout.item_message, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final MessageModel item, int position) {
        // 判断线条
        if (position == 0) {
            holder.setVisible(R.id.view_head, true);
        } else {
            holder.setVisible(R.id.view_head, false);
        }

        holder.setText(R.id.tv_title, item.getTitle())
                .setText(R.id.tv_content, item.getContent())
                .setText(R.id.tv_create_at, CommonUtils.getDryTime(item.getCreate_at() + ""));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CommonLogic.startActivityPush(mContext, item.getAsset_type(), item.getForeign_id() + "");
            }
        });
    }

}