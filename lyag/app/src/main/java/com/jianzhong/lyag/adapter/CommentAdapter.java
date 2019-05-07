package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.ImageView;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.listener.OnCommentListener;
import com.jianzhong.lyag.model.CommentModel;
import com.jianzhong.lyag.ui.exam.CommentDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * 评论列表适配器
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.adapter
 */
public class CommentAdapter extends CommonAdapter<CommentModel> {
    private String course_id;
    private OnCommentListener listener;

    public CommentAdapter(Context context, List<CommentModel> datas, String course_id, OnCommentListener listener) {
        super(context, R.layout.item_comment, datas);

        this.course_id = course_id;
        this.listener = listener;
    }

    @Override
    protected void convert(ViewHolder holder, final CommentModel item, final int position) {
        if (item.getUser() != null) {
            GlideUtils.loadAvatarImage((ImageView) holder.getView(R.id.iv_avatar), item.getUser().getAvatar());
            holder.setText(R.id.tv_name, item.getUser().getRealname());
            if (item.getUser().getUser_id().equals(AppUserModel.getInstance().getmUserModel().getUser_id())) {
                holder.setVisible(R.id.iv_del, true);
            } else {
                holder.setVisible(R.id.iv_del, false);
            }
        }
        holder.setText(R.id.tv_count, item.getReply_num());
        holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getCreate_at()));
        holder.setText(R.id.tv_content, item.getContent());
        holder.setOnClickListener(R.id.iv_del, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnClick(item, position-1);
            }
        });



        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentDetailActivity.class);
                intent.putExtra("foreign_id", course_id);
                intent.putExtra("comment_type", item.getAsset_type());
                intent.putExtra("top_id", item.getComment_id());
                intent.putExtra("commentModel", item);
                intent.putExtra("type",1);
                mContext.startActivity(intent);
            }
        });
    }

}
