package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.AssignTaskModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;
import com.jianzhong.lyag.ui.exam.ColumnDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 学习任务列表适配器指派
 * create by zhengwencheng on 2018/3/21 0021
 * package com.jianzhong.bs.adapter
 */
public class ReceiveTaskAdapter extends CommonAdapter<AssignTaskModel> {
    public ReceiveTaskAdapter(Context context, List<AssignTaskModel> datas) {
        super(context, R.layout.item_receive_task, datas);

    }

    @Override
    protected void convert(ViewHolder holder, final AssignTaskModel item, int position) {
        holder.setText(R.id.tv_title, item.getTitle());
        holder.setText(R.id.tv_require, item.getDictate_title());
        holder.setText(R.id.tv_time, CommonUtils.getDryTime(item.getCreate_at()));
        if (Integer.valueOf(item.getAssign_user_id()) > 1) {
            holder.setText(R.id.tv_tag, "领导指派");
        } else {
            holder.setText(R.id.tv_tag, "岗位必修");
        }
        if (item.getIs_read().equals("0")) {
            // 这一步必须要做,否则不会显示
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.wd_sddxxrw_unread);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView) holder.getView(R.id.tv_title)).setCompoundDrawables(drawable, null, null, null);
        } else {
            Drawable drawable = mContext.getResources().getDrawable(R.drawable.wd_sddxxrw_already);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            ((TextView) holder.getView(R.id.tv_title)).setCompoundDrawables(drawable, null, null, null);
        }

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if (item.getAsset_type().equals(AppConstants.TAG_COLUMN)) {
                    intent = new Intent(mContext, ColumnDetailActivity.class);
                    intent.putExtra("column_id", item.getForeign_id());
                    mContext.startActivity(intent);
                } else if (item.getAsset_type().equals(AppConstants.TAG_COURSE)) {
                    intent = new Intent(mContext, ClassDetailActivity.class);
                    intent.putExtra("course_id", item.getForeign_id());
                    mContext.startActivity(intent);
                    getDictateRead(item.getDictate_id());
                }
            }
        });
    }

    private void getDictateRead(String dictateId) {
        Map<String, Object> params = new HashMap<>();
        params.put("dictate_id", dictateId);

        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_DICTATE_READ, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<AssignTaskModel> resultList = GsonUtils.json2List(s, AssignTaskModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext, msg);
            }
        });
    }
}
