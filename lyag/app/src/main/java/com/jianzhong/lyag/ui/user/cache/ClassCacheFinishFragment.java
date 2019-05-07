package com.jianzhong.lyag.ui.user.cache;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.baselib.util.ListUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.ClassDownloadFinishAdapter;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.DownbodyModel;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 课程缓存完成Fragment
 * <p>
 * Created by zhengwencheng on 2018/4/9 0009.
 * package com.jianzhong.bs.ui.user.cache
 */

public class ClassCacheFinishFragment extends BaseFragment {

    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.tv_del)
    TextView mTvDel;
    @BindView(R.id.ll_item)
    LinearLayout mLlItem;
    private ClassDownloadFinishAdapter mAdapter;
    private LinkedList<DownloadInfo> mList = new LinkedList<>();
    public static ClassCacheFinishFragment newInstance() {

        Bundle args = new Bundle();
        ClassCacheFinishFragment fragment = new ClassCacheFinishFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_audio_cache_son, null);
        return mRootView;
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

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void resetCahceEdit(String tag) {
        if (tag.equals(AppConstants.TAG_CAHCE_EDIT)) {
            if (GroupVarManager.getInstance().isEditCache == 1) {
                mLlItem.setVisibility(View.VISIBLE);
                mTvDel.setText("删除（" + GroupVarManager.getInstance().mCacheList.size() + "）");
            } else {
                mLlItem.setVisibility(View.GONE);
                for (DownloadInfo item : mList) {
                    ((DownbodyModel) item.getData()).setIsSelected(0);
                }
            }

            mAdapter.setIsEdit(GroupVarManager.getInstance().isEditCache);
            mAdapter.notifyDataSetChanged();
        } else if (tag.equals(AppConstants.TAG_CACHE_DEL)) {
            mTvDel.setText("删除（" + GroupVarManager.getInstance().mCacheList.size() + "）");
        }
        isShowNoDataView(mList);
    }

    @Override
    protected void initData() {
        super.initData();

        List<DownloadInfo> mAllTaskCache = BaseApplication.mDownloadManager.getAllTask();
        if (!ListUtils.isEmpty(mAllTaskCache)) {
            for (int i = 0; i < mAllTaskCache.size(); i++) {
                if (mAllTaskCache.get(i).getState() == DownloadManager.FINISH) {
                    DownbodyModel item = (DownbodyModel) mAllTaskCache.get(i).getData();
                    if (item != null && item.getMediaType() == 1) {
                        if(!ListUtils.isEmpty(item.getUserList())){
                            for (int j = 0; j < item.getUserList().size(); j++) {
                                if(item.getUserList().get(j).equals(AppUserModel.getInstance().getmUserModel().getUser_id())){
                                    mList.add(mAllTaskCache.get(i));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        mAdapter = new ClassDownloadFinishAdapter(mContext, mList, 0);
        mListView.setAdapter(mAdapter);
        isShowNoDataView(mList);
    }

    @OnClick({R.id.ll_all, R.id.ll_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_all:
                for (int i = 0; i < mList.size(); i++) {
                    if(GroupVarManager.getInstance().isAll == 1){
                        ((DownbodyModel) mList.get(i).getData()).setIsSelected(1);
                        GroupVarManager.getInstance().mCacheList.add(mList.get(i).getTaskKey());
                        mTvDel.setText("删除（" + GroupVarManager.getInstance().mCacheList.size() + "）");
                    }else {
                        ((DownbodyModel) mList.get(i).getData()).setIsSelected(0);
                        GroupVarManager.getInstance().mCacheList.remove(mList.get(i).getTaskKey());
                        mTvDel.setText("删除（" + GroupVarManager.getInstance().mCacheList.size() + "）");
                    }

                }

                if(GroupVarManager.getInstance().isAll == 1){
                    GroupVarManager.getInstance().isAll = 0;
                }else {
                    GroupVarManager.getInstance().isAll = 1;
                }
                mAdapter.notifyDataSetChanged();
                isShowNoDataView(mList);
                break;
            case R.id.ll_del:
                if (!GroupVarManager.getInstance().mCacheList.isEmpty()) {
                    new AlertDialog.Builder(getActivity()).setTitle("提示").
                            setMessage("确认删除所选缓存文件吗").
                            setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    delCache();
                                }
                            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            return;
                        }
                    }).create().show();
                }

                break;
        }
    }

    private void delCache() {
        for (Iterator<String> it = GroupVarManager.getInstance().mCacheList.iterator(); it.hasNext(); ) {
            String key = it.next();
            DownloadManager.getInstance().removeTask(key,true);

            for (int i = 0; i < mList.size(); i++) {
                if(mList.get(i).getTaskKey().equals(key)){
                    mList.remove(i);
                    break;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        isShowNoDataView(mList);
    }
}
