package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.widget.ImageView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.UserModel;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;
/**
 * 显示人员头像的适配器
 * Created by zhengwencheng on 2018/3/1 0001.
 * package com.jianzhong.bs.adapter
 */

public class AvatarAdapter extends CommonAdapter<UserModel> {
    public AvatarAdapter(Context context,List<UserModel> datas) {
        super(context, R.layout.item_avatar, datas);
    }

    @Override
    protected void convert(ViewHolder holder, UserModel item, int position) {
        GlideUtils.loadAvatarImage((ImageView) holder.getView(R.id.iv_avatar),item.getAvatar());
    }
}
