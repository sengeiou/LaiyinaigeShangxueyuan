package com.jianzhong.lyag.adapter;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import java.util.List;
/**
 * 按组织架构选择的PageAdapter
 * Created by zhengwencheng on 2018/1/19 0019.
 * package com.jianzhong.bs.adapter
 */
public class FragmentViewPageAdapter extends FragmentPagerAdapter {

    private List<Fragment> list;

    public FragmentViewPageAdapter(FragmentManager fm, List<Fragment> list) {
        super(fm);

        this.list = list;
    }

    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }
}
