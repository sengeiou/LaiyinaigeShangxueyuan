package com.jianzhong.lyag.ui.user.history;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;

import com.baselib.util.ListUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.AudioRecordAdapter;
import com.jianzhong.lyag.base.BaseRecyclerViewActivity;
import com.jianzhong.lyag.db.dao.AudioRecordDao;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.model.AudioRecorModel;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler2;
import in.srain.cube.views.ptr.PtrFrameLayout;

/**
 * 音频播放记录
 * Created by zhengwencheng on 2018/4/2 0002.
 * package com.jianzhong.bs.ui.user.history
 */

public class HistoryAudioActivity extends BaseRecyclerViewActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;

    private AudioRecordAdapter mAdapter;
    private List<AudioRecorModel> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_history_audio);
        ButterKnife.bind(this);
    }

    @Override
    protected RecyclerView.Adapter getRecyclerViewAdapter() {
        mAdapter = new AudioRecordAdapter(mContext,mList);
        return mAdapter;
    }

    @Override
    public void initData() {
        super.initData();

        AudioRecordDao mAudioRecordDao = new AudioRecordDao(mContext);

        setHeadTitle("音频历史");
        showHeadTitle();

        initRecylerView();

        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
        if(!ListUtils.isEmpty(mAudioRecordDao.queryForValue(map))){
            mList.addAll(mAudioRecordDao.queryForAll());
            mAdapter.notifyDataSetChanged();
        }

        hideCommonLoading();
    }

    /**
     * 初始化列表
     */
    private void initRecylerView() {
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_grey_divider))
                .sizeResId(R.dimen.default_divider_one)
                .build());
        mPtrFrame.setMode(PtrFrameLayout.Mode.NONE);

        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {

            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

            }
        });
    }
}
