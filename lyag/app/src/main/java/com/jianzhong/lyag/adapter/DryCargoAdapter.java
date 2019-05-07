package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.ContentDetailModel;
import com.jianzhong.lyag.ui.exam.DryCargoDetailActivity;
import com.zhy.adapter.abslistview.CommonAdapter;
import com.zhy.adapter.abslistview.ViewHolder;

import java.io.Serializable;
import java.util.List;

/**
 * 每日音频列表适配器
 * Created by zhengwencheng on 2018/1/16 0016.
 * package com.jianzhong.bs.adapter
 */

public class DryCargoAdapter extends CommonAdapter<ContentDetailModel> {
    public DryCargoAdapter(Context context, List<ContentDetailModel> datas) {
        super(context, R.layout.item_dry_cargo, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final ContentDetailModel item, int position) {
        holder.setText(R.id.tv_title, item.getTitle());

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DryCargoDetailActivity.class);
                intent.putExtra("audio_id",item.getAudio_id());
                intent.putExtra("mDatas", (Serializable) mDatas);
                mContext.startActivity(intent);
            }
        });
    }
}
