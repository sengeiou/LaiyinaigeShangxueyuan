package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.widget.ImageView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.MemberConModel;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
/**
 * 成员列表适配器
 * Created by zhengwencheng on 2018/3/6 0006.
 * package com.jianzhong.bs.adapter
 */
public class MemberConAdapter extends CommonAdapter<MemberConModel> {
    public MemberConAdapter(Context context,List<MemberConModel> datas) {
        super(context, R.layout.item_member_con, datas);
    }

    @Override
    protected void convert(ViewHolder holder, MemberConModel item, int position) {
        GlideUtils.loadAvatarImage((ImageView) holder.getView(R.id.iv_avatar),item.getAvatar());
        holder.setText(R.id.tv_title,item.getRealname());
        holder.setText(R.id.tv_assist_title,(item.getBranch() != null ?item.getBranch().getBranch_path():"") + (item.getPos() != null ? item.getPos().getPos_name():""));

    }
}
