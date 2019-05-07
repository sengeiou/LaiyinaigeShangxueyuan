package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.SectionIndexer;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.DepartmentModel;
import com.jianzhong.lyag.ui.organization.MemberByOrganizeActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

/**
 * 按岗位标签选择列表适配器
 */
public class TagSelectAdapter extends CommonAdapter<DepartmentModel> implements SectionIndexer {

    private List<DepartmentModel> list = null;

    public TagSelectAdapter(Context context, List<DepartmentModel> datas) {
        super(context, R.layout.item_tag_select, datas);

        this.list = datas;
    }

    @Override
    protected void convert(ViewHolder holder, final DepartmentModel item, final int position) {
        //根据position获取分类的首字母的Char ascii值
        int section = getSectionForPosition(position);
        //如果当前位置等于该分类首字母的Char的位置 ，则认为是第一次出现
//        if (position == getPositionForSection(section)) {
//            holder.setVisible(R.id.tv_letter,true);
//            holder.setText(R.id.tv_letter,item.getSortLetters());
//        } else {
//            holder.setVisible(R.id.tv_letter,false);
//        }
        holder.setVisible(R.id.tv_letter,false);
        holder.setText(R.id.tv_title,item.getBranch_name());
        holder.setText(R.id.tv_assist_title,item.getCount()+"人");
        if (item.getIsSelected() == 1) {
            holder.setChecked(R.id.cb_check, true);
        } else {
            holder.setChecked(R.id.cb_check, false);
        }

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) { //查看该标签下成员
                if (item.getIsSelected() == 1) {
                    item.setIsSelected(0);
                    for (int i = 0; i < GroupVarManager.getInstance().mPostModels.size(); i++) {
                        if(GroupVarManager.getInstance().mPostModels.get(i).getPos_id().equals(item.getPos_id())){
                            GroupVarManager.getInstance().mPostModels.remove(i);
                        }
                    }
                } else {
                    item.setIsSelected(1);
                    GroupVarManager.getInstance().mPostModels.add(item);
//                    GroupVarManager.getInstance().mMemberModels.addAll(item.getMember());
                }
                notifyDataSetChanged();
                EventBus.getDefault().post(AppConstants.TAG_UPDATE_ASSIGN);
            }
        });

        holder.setOnClickListener(R.id.ll_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) { //查看该标签下成员
                Intent intent = new Intent(mContext, MemberByOrganizeActivity.class);
                intent.putExtra("department",item);
                intent.putExtra("type",2); //1：代表部门 0 代表经销商
                mContext.startActivity(intent);
            }
        });
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     * @param list
     */
    public void updateListView(List<DepartmentModel> list) {
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