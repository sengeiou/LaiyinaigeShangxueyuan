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
 * 按公司部门选择列表适配器
 */
public class DepartmentSelAdapter extends CommonAdapter<DepartmentModel> implements SectionIndexer {

    private List<DepartmentModel> list = null;

    public DepartmentSelAdapter(Context context, List<DepartmentModel> datas) {
        super(context, R.layout.item_tag_select, datas);

        this.list = datas;
    }

    @Override
    protected void convert(ViewHolder holder, final DepartmentModel item, int position) {
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
        if(item.getIsSelected() == 1){
            holder.setChecked(R.id.cb_check,true);
        }else {
            holder.setChecked(R.id.cb_check,false);
        }

        holder.setText(R.id.tv_title,item.getBranch_name());
        holder.setText(R.id.tv_assist_title,item.getCount()+"人");
//        GlideUtils.loadCircleImage((ImageView) holder.getView(R.id.iv_avatar), item.getAvatar());
        holder.setOnClickListener(R.id.ll_more, new View.OnClickListener() {
            @Override
            public void onClick(View v) { //查看该标签下成员
                Intent intent = new Intent(mContext, MemberByOrganizeActivity.class);
                intent.putExtra("department",item);
                intent.putExtra("type",1); //1：代表部门 0 代表经销商
                mContext.startActivity(intent);
            }
        });

        holder.setOnClickListener(R.id.ll_item, new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (item.getIsSelected() == 1) {
                    item.setIsSelected(0);
                    for (int i = 0; i < GroupVarManager.getInstance().mDepartmentModels.size(); i++) {
                        if (item.getBranch_id().equals(GroupVarManager.getInstance().mDepartmentModels.get(i).getBranch_id())) {
                            GroupVarManager.getInstance().mDepartmentModels.remove(i);
                            break;
                        }
                    }
                } else {
                    item.setIsSelected(1);
                    GroupVarManager.getInstance().mDepartmentModels.add(item);
                }


                notifyDataSetChanged();

                EventBus.getDefault().post(AppConstants.TAG_UPDATE_ASSIGN);
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