package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.ContactsModel;
import com.jianzhong.lyag.util.GlideUtils;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;
/**
 * 按组织架构选择成员列表适配器
 * Created by zhengwencheng on 2018/1/20 0020.
 * package com.jianzhong.bs.adapter
 */
public class MemberByOrganizeAdapter extends CommonAdapter<ContactsModel> {

    public MemberByOrganizeAdapter(Context context, List<ContactsModel> datas) {
        super(context, R.layout.item_member_by_organize, datas);
    }

    @Override
    protected void convert(ViewHolder holder, final ContactsModel item, int position) {
        if (item.getIsSelected() == 1) {
            holder.setChecked(R.id.cb_check, true);
        } else {
            holder.setChecked(R.id.cb_check, false);
        }

        holder.setText(R.id.tv_title, item.getRealname());
        holder.setText(R.id.tv_assist_title, item.getBranch_path());
        GlideUtils.loadCircleImage((ImageView) holder.getView(R.id.iv_avatar), item.getAvatar());

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getIsSelected() == 1) {
                    item.setIsSelected(0);
                    for (int i = 0; i < GroupVarManager.getInstance().mMemberModels.size(); i++) {
                        if (item.getUser_id().equals(GroupVarManager.getInstance().mMemberModels.get(i).getUser_id())) {
                            GroupVarManager.getInstance().mMemberModels.remove(i);
                            break;
                        }
                    }
                } else {
                    item.setIsSelected(1);
                    GroupVarManager.getInstance().mMemberModels.add(item);
                }


                notifyDataSetChanged();

                EventBus.getDefault().post(AppConstants.TAG_UPDATE_ASSIGN);
            }
        });
    }
}
