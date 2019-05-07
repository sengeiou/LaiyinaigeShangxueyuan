package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;

import com.baselib.util.GsonUtils;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnCommentSubListener;
import com.jianzhong.lyag.model.CommentSubModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 评论回复列表适配器
 * Created by zhengwencheng on 2018/2/28 0028.
 * package com.jianzhong.bs.adapter
 */
public class CommentSubAdapter extends CommonAdapter<CommentSubModel> {
    private OnCommentSubListener listener;
    private int isAdopt;
    private int type;
    public CommentSubAdapter(Context context, List<CommentSubModel> datas,OnCommentSubListener listener, int isAdopt,int type) {
        super(context, R.layout.item_comment, datas);

        this.listener = listener;
        this.isAdopt = isAdopt;
        this.type = type;
    }

    @Override
    protected void convert(ViewHolder holder, final CommentSubModel item, final int position) {
        if(item.getUser() != null){
            GlideUtils.loadAvatarImage((ImageView) holder.getView(R.id.iv_avatar), item.getUser().getAvatar());
            holder.setText(R.id.tv_name,item.getUser().getRealname());
            if(item.getUser().getUser_id().equals(AppUserModel.getInstance().getmUserModel().getUser_id())){
                holder.setVisible(R.id.iv_del,true);
            }else {
                holder.setVisible(R.id.iv_del,false);
            }
        }

        if(type == 0){
            if(isAdopt == 0){
                holder.setVisible(R.id.tv_like,true);
                holder.setText(R.id.tv_like,item.getLike_num());
                if(StringUtils.isEquals(item.getHas_like(), "1")){
                    holder.getView(R.id.tv_like).setSelected(true);
                }else {
                    holder.getView(R.id.tv_like).setSelected(false);
                }
            }else {
                holder.setVisible(R.id.tv_adopt,true);
                holder.setText(R.id.tv_adopt,item.getLike_num());
                if(StringUtils.isEquals(item.getHas_like(), "1")){
                    holder.getView(R.id.tv_adopt).setSelected(true);
                }else {
                    holder.getView(R.id.tv_adopt).setSelected(false);
                }
            }
        }

        holder.setVisible(R.id.tv_count,false);
        holder.setText(R.id.tv_count,item.getReply_num());
        holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getCreate_at()));
        holder.setText(R.id.tv_content,item.getContent());

        holder.setOnClickListener(R.id.iv_del, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.OnClick(item,position);
            }
        });

        holder.setOnClickListener(R.id.tv_like, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDatas.get(position).getHas_like().equals("0")){
                    getLike(position,item);
                }else {
                    cancelLike(position,item);
                }
            }
        });

        holder.setOnClickListener(R.id.tv_adopt, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(mDatas.get(position).getHas_like().equals("0")){
                    getLike(position,item);
                }else {
                    cancelLike(position,item);
                }
            }
        });

    }

    private void getLike(final int position,CommentSubModel item) {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", item.getAsset_type());
        params.put("foreign_id", item.getComment_id());
        params.put("asset_type_out", item.getAsset_type());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DO_LIKE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    GroupVarManager.getInstance().isUpdateComment = 1;
                    if(isAdopt == 0){
                        ToastUtils.show(mContext,result.getMessage());
                    }else {
                        ToastUtils.show(mContext,"采纳成功");
                    }
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
    private void cancelLike(final int position,CommentSubModel item) {
        Map<String, Object> params = new HashMap<>();
        params.put("asset_type", item.getSelf_type());
        params.put("foreign_id", item.getComment_id());
        params.put("asset_type_out", item.getAsset_type());
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DIS_LIKE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    GroupVarManager.getInstance().isUpdateComment = 1;
                    if(isAdopt == 0){
                        ToastUtils.show(mContext,result.getMessage());
                    }else {
                        ToastUtils.show(mContext,"已取消采纳");
                    }
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
}
