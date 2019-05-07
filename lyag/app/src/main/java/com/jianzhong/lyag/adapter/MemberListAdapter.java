package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.SectionIndexer;
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
 * 排序用的适配器
 */
public class MemberListAdapter extends CommonAdapter<ContactsModel> implements SectionIndexer {

    private List<ContactsModel> list = null;
    private int type;
    public MemberListAdapter(Context context, List<ContactsModel> datas,int type) {
        super(context, R.layout.item_member_select, datas);

        this.list = datas;
        this.type = type;
    }

    @Override
    protected void convert(ViewHolder holder, final ContactsModel item, int position) {
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);

        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
        if(type == 1){
            if (position == getPositionForSection(section)) {
                holder.setVisible(R.id.tv_letter, true);
                holder.setText(R.id.tv_letter, item.getSortLetters());
            } else {
                holder.setVisible(R.id.tv_letter, false);
            }
        }else {
            holder.setVisible(R.id.tv_letter, false);
        }


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

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<ContactsModel> list) {
        this.list = list;
        notifyDataSetChanged();
    }


    /**
     * 根据ListView的当前位置获取分类的首字母的Char ascii值
     */
    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    /**
     * 根据分类的首字母的Char ascii值获取其第一次出现该首字母的位置
     */
    public int getPositionForSection(int section) {
        for (int i = 0; i < getItemCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }

        return -1;
    }

    /**
     * 提取英文的首字母，非英文字母用#代替。
     *
     * @param str
     * @return
     */
    private String getAlpha(String str) {
        String sortStr = str.trim().substring(0, 1).toUpperCase();
        // 正则表达式，判断首字母是否是英文字母
        if (sortStr.matches("[A-Z]")) {
            return sortStr;
        } else {
            return "#";
        }
    }

    @Override
    public Object[] getSections() {
        return null;
    }
}