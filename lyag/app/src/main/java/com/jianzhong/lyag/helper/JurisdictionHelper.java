package com.jianzhong.lyag.helper;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.model.MainDataModel;
import com.jianzhong.lyag.ui.fragment.BangFragment;
import com.jianzhong.lyag.ui.fragment.HomeFragment;
import com.jianzhong.lyag.ui.fragment.InteractFragment;
import com.jianzhong.lyag.ui.fragment.MeFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * 权限帮助类
 * Created by zhengwencheng on 2018/1/27 0027.
 * package com.jianzhong.bs.helper
 */

public class JurisdictionHelper {
    /**
     * 获取首页Tab
     *
     * @return
     */
    public static MainDataModel getMainDataTab() {
        //定义数组来存放Fragment对象
        List<Class> fragmentArray = new ArrayList<>();
        //tab选项卡的文字
        List<String> textViewArray = new ArrayList<>();
        //按钮图片数组
        List<Integer> imageViewArray = new ArrayList<>();

        if (AppUserModel.getInstance().getAll_block() != null) {
            for (int i = 0; i < AppUserModel.getInstance().getAll_block().size(); i++) {
                if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals(AppConstants.HOME_LABEL) && AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("0")) {
                    fragmentArray.add(HomeFragment.class);
                    textViewArray.add("首页");
                    imageViewArray.add(R.drawable.selector_home_tab);
                } else if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals(AppConstants.INTERACT_LABEL) && AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("0")) {
                    fragmentArray.add(InteractFragment.class);
                    textViewArray.add("互动");
                    imageViewArray.add(R.drawable.selector_interact_tab);
                } else if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals(AppConstants.CLUB_LABEL) && AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("0")) {
                    fragmentArray.add(BangFragment.class);
                    textViewArray.add("帮派");
                    imageViewArray.add(R.drawable.selector_bang_tab);
                } else if (AppUserModel.getInstance().getAll_block().get(i).getLabel().equals(AppConstants.MINE_LABEL) && AppUserModel.getInstance().getAll_block().get(i).getIs_hide().equals("0")) {
                    fragmentArray.add(MeFragment.class);
                    textViewArray.add("我的");
                    imageViewArray.add(R.drawable.selector_mine_tab);
                }
            }
        }

        MainDataModel model = new MainDataModel();
        model.setFragmentArray(fragmentArray);
        model.setTextViewArray(textViewArray);
        model.setImageViewArray(imageViewArray);
        return model;
    }
}
