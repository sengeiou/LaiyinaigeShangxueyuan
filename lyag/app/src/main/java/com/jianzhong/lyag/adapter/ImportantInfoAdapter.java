package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.NoticeMsgModel;
import com.jianzhong.lyag.ui.notice.NoticeDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.util.List;
/**
 * 重要通知列表适配器
 * Created by zhengwencheng on 2018/1/16 0016.
 * package com.jianzhong.bs.adapter
 */

public class ImportantInfoAdapter extends CommonAdapter<NoticeMsgModel> {
    public ImportantInfoAdapter(Context context, List<NoticeMsgModel> datas) {
        super(context, R.layout.item_important_info, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final NoticeMsgModel item, int position) {
        viewHolder.setText(R.id.tv_info,item.getTitle());
        viewHolder.setText(R.id.tv_time, CommonUtils.timeStampToDate(item.getCreate_at(),"MM-dd"));

        viewHolder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, NoticeDetailActivity.class);
                intent.putExtra("notice_id",item.getNotice_id());
                mContext.startActivity(intent);
            }
        });
    }
}
