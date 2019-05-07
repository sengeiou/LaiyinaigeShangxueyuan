package com.jianzhong.lyag.ui.exam;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.baselib.widget.CustomListView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.ClearanceExamAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.ExaminationItemModel;
import com.jianzhong.lyag.model.ExaminationModel;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
/**
 * 考试通关
 * Created by zhengwencheng on 2018/2/2 0002.
 * package com.jianzhong.bs.ui.exam
 */

public class ClearanceExamFragment extends BaseFragment {
    @BindView(R.id.tv_title)
    TextView mTvTitle;
    @BindView(R.id.lv_exam)
    CustomListView mLvExam;
    private ClearanceExamAdapter mAdapter;
    private List<ExaminationItemModel> mList = new ArrayList<>();
    private ExaminationModel data;
    private int position;
    private int dataSize;
    public static ClearanceExamFragment newInstance(ExaminationModel examItem,int position,int dataSize) {

        ClearanceExamFragment fragment = new ClearanceExamFragment();
        Bundle args = new Bundle();
        args.putSerializable("examItem", examItem);
        args.putInt("position",position);
        args.putInt("dataSize",dataSize);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    //点击下一题时
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getPostAnswer(String tag) {
        if (tag.equals(AppConstants.EVENT_ANSWER) && GroupVarManager.getInstance().examIndex == position) {
            GroupVarManager.getInstance().isSelectedAnswer = 2;
            mAdapter.setStatus(1);
            mAdapter.notifyDataSetChanged();

        }else if(tag.equals(AppConstants.EVENT_TIME_OUT) && GroupVarManager.getInstance().examIndex == position){
            mAdapter.setStatus(2);
            mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_clearance_exam, null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();

        GroupVarManager.getInstance().examIndex = 0;
        data = (ExaminationModel) getArguments().get("examItem");
        position = getArguments().getInt("position",-1);
        dataSize = getArguments().getInt("dataSize",-1);
        initView();
    }

    @SuppressLint("SetTextI18n")
    private void initView() {
        if (data == null)
            return;
        mTvTitle.setText((position+1)+"/"+dataSize+" "+data.getTitle());
        mList.addAll(data.getItem());
        mAdapter = new ClearanceExamAdapter(mContext,mList,0);
        mLvExam.setAdapter(mAdapter);
        GroupVarManager.getInstance().mExamMap.put(position, getListViewParams());
        if(position == 0){
            EventBus.getDefault().post(AppConstants.KEY_EXAM);
        }
    }

    /**
     * 获取Listview的高度 给首页的viewpager重新设置高度
     *
     * @return
     */
    private ViewGroup.LayoutParams getListViewParams() {
        //通过ListView获取其中的适配器adapter
        ListAdapter listAdapter = mLvExam.getAdapter();
        //声明默认高度为0
        int totalHeight = 0;
        //遍历listView中所有Item，累加所有item的高度就是ListView的实际高度（后面会考虑分割线的高度）
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View item = listAdapter.getView(i, null, mLvExam);
            item.measure(0, 0);
            totalHeight += item.getMeasuredHeight();
        }

        ViewGroup.LayoutParams lp = mLvExam.getLayoutParams();
        lp.height = totalHeight + (mLvExam.getDividerHeight() * (listAdapter.getCount()) *2);

        return lp;
    }

}
