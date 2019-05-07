package com.jianzhong.lyag.ui.user.cache;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.text.format.Formatter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.akexorcist.roundcornerprogressbar.TextRoundCornerProgressBar;
import com.baselib.util.ListUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.BaseApplication;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.model.DownbodyModel;
import com.lzy.okserver.download.DownloadInfo;
import com.lzy.okserver.download.DownloadManager;
import com.lzy.okserver.listener.DownloadListener;
import com.lzy.okserver.task.ExecutorWithListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 音频缓存Fragment
 * <p>
 * Created by zhengwencheng on 2018/4/9 0009.
 * package com.jianzhong.bs.ui.user.cache
 */

public class AudioCacheNowFragment extends BaseFragment {

    @BindView(R.id.listView)
    ListView mListView;
    @BindView(R.id.tv_del)
    TextView mTvDel;
    @BindView(R.id.ll_item)
    LinearLayout mLlItem;

    private AudioDownloadAdapter mAdapter;
    private List<DownloadInfo> mAllTask = new ArrayList<>();
    public static AudioCacheNowFragment newInstance() {

        Bundle args = new Bundle();
        AudioCacheNowFragment fragment = new AudioCacheNowFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_audio_cache_son, null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();

        List<DownloadInfo> mAllTaskCache = BaseApplication.mDownloadManager.getAllTask();
        if(!ListUtils.isEmpty(mAllTaskCache)){
            for (int i = 0; i < mAllTaskCache.size(); i++) {
                if(mAllTaskCache.get(i).getState() != DownloadManager.FINISH){
                    DownbodyModel item = (DownbodyModel) mAllTaskCache.get(i).getData();
                    if(item.getMediaType() == 0){
                        if(!ListUtils.isEmpty(item.getUserList())){
                            for (int j = 0; j < item.getUserList().size(); j++) {
                                if(item.getUserList().get(j).equals(AppUserModel.getInstance().getmUserModel().getUser_id())){
                                    mAllTask.add(mAllTaskCache.get(i));
                                    break;
                                }
                            }
                        }
                    }
                }
            }
        }
        mAdapter = new AudioDownloadAdapter();
        mListView.setAdapter(mAdapter);

        BaseApplication.mDownloadManager.getThreadPool().getExecutor().addOnAllTaskEndListener(new ExecutorWithListener.OnAllTaskEndListener() {
            @Override
            public void onAllTaskEnd() {
                for (DownloadInfo downloadInfo : mAllTask) {
                    if (downloadInfo.getState() != DownloadManager.FINISH) {
                        Toast.makeText(mContext, "暂停下载", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
//                Toast.makeText(mContext, "所有下载任务完成", Toast.LENGTH_SHORT).show();
            }
        });

        isShowNoDataView(mAllTask);
    }

    @SuppressLint("SetTextI18n")
    @OnClick({R.id.ll_all, R.id.ll_del})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_all:
                for (int i = 0; i < mAllTask.size(); i++) {
                    if(GroupVarManager.getInstance().isAll == 1){
                        ((DownbodyModel) mAllTask.get(i).getData()).setIsSelected(1);
                        GroupVarManager.getInstance().mCacheList.add(mAllTask.get(i).getTaskKey());
                        mTvDel.setText("删除（" + GroupVarManager.getInstance().mCacheList.size() + "）");
                    }else {
                        ((DownbodyModel) mAllTask.get(i).getData()).setIsSelected(0);
                        GroupVarManager.getInstance().mCacheList.remove(mAllTask.get(i).getTaskKey());
                        mTvDel.setText("删除（" + GroupVarManager.getInstance().mCacheList.size() + "）");
                    }
                }

                if(GroupVarManager.getInstance().isAll == 1){
                    GroupVarManager.getInstance().isAll = 0;
                }else {
                    GroupVarManager.getInstance().isAll = 1;
                }
                mAdapter.notifyDataSetChanged();
                isShowNoDataView(mAllTask);
                break;
            case R.id.ll_del:
                if (!GroupVarManager.getInstance().mCacheList.isEmpty()) {
                    new AlertDialog.Builder(getActivity()).setTitle("提示").
                            setMessage("确认删除正在缓存文件吗").
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
            DownloadManager.getInstance().removeTask(key, true);

            for (int i = 0; i < mAllTask.size(); i++) {
                if (mAllTask.get(i).getTaskKey().equals(key)) {
                    mAllTask.remove(i);
                    break;
                }
            }
        }
        mAdapter.notifyDataSetChanged();
        isShowNoDataView(mAllTask);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //记得移除，否者会回调多次
        BaseApplication.mDownloadManager.getThreadPool().getExecutor().removeOnAllTaskEndListener(new ExecutorWithListener.OnAllTaskEndListener() {
            @Override
            public void onAllTaskEnd() {

            }
        });

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
                for (DownloadInfo item : mAllTask) {
                    ((DownbodyModel) item.getData()).setIsSelected(0);
                }
            }
            mAdapter.notifyDataSetChanged();
        } else if (tag.equals(AppConstants.TAG_CACHE_DEL)) {
            mTvDel.setText("删除（" + GroupVarManager.getInstance().mCacheList.size() + "）");
        }
        isShowNoDataView(mAllTask);
    }


    private class AudioDownloadAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return mAllTask.size();
        }

        @Override
        public DownloadInfo getItem(int position) {
            return mAllTask.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            DownloadInfo downloadInfo = getItem(position);
            ViewHolder holder;
            if (convertView == null) {
                convertView = View.inflate(mContext, R.layout.item_audio_download, null);
                holder = new ViewHolder(convertView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            holder.refresh(downloadInfo);

            //对于非进度更新的ui放在这里，对于实时更新的进度ui，放在holder中
            DownbodyModel info = (DownbodyModel) downloadInfo.getData();
            if (info != null) {
                holder.mTvTitle.setText(info.getTitle());
            } else {
                holder.mTvTitle.setText(downloadInfo.getFileName());
            }
            holder.mLlItem.setOnClickListener(holder);
            DownloadListener downloadListener = new MyDownloadListener();
            downloadListener.setUserTag(holder);
            downloadInfo.setListener(downloadListener);
            return convertView;
        }
    }

    private class ViewHolder implements View.OnClickListener {
        private DownloadInfo downloadInfo;
        private TextView mTvTitle;
        private TextView downloadSize;
        private TextView tvProgress;
        private TextView netSpeed;
        private TextRoundCornerProgressBar pbProgress;
        private LinearLayout mLlItem;

        @SuppressLint("WrongViewCast")
        public ViewHolder(View convertView) {
            mTvTitle = convertView.findViewById(R.id.tv_title);
            downloadSize = convertView.findViewById(R.id.downloadSize);
            tvProgress = convertView.findViewById(R.id.tvProgress);
            netSpeed = convertView.findViewById(R.id.netSpeed);
            pbProgress = convertView.findViewById(R.id.pbProgress);
            mLlItem =  convertView.findViewById(R.id.ll_item);
        }

        public void refresh(DownloadInfo downloadInfo) {
            this.downloadInfo = downloadInfo;
            refresh();
        }

        //对于实时更新的进度ui，放在这里，例如进度的显示，而图片加载等，不要放在这，会不停的重复回调
        //也会导致内存泄漏
        private void refresh() {
            String downloadLength = Formatter.formatFileSize(mContext, downloadInfo.getDownloadLength());
            String totalLength = Formatter.formatFileSize(mContext, downloadInfo.getTotalLength());
            downloadSize.setText(downloadLength + "/" + totalLength);
            if (downloadInfo.getState() == DownloadManager.NONE) {
                netSpeed.setText("停止");
            } else if (downloadInfo.getState() == DownloadManager.PAUSE) {
                netSpeed.setText("暂停中");
            } else if (downloadInfo.getState() == DownloadManager.ERROR) {
                netSpeed.setText("下载出错");
            } else if (downloadInfo.getState() == DownloadManager.WAITING) {
                netSpeed.setText("等待中");
            } else if (downloadInfo.getState() == DownloadManager.FINISH) {
                netSpeed.setText("下载完成");
            } else if (downloadInfo.getState() == DownloadManager.DOWNLOADING) {
                String networkSpeed = Formatter.formatFileSize(getActivity(), downloadInfo.getNetworkSpeed());
                netSpeed.setText(networkSpeed + "/s");
            }
            tvProgress.setText((Math.round(downloadInfo.getProgress() * 10000) * 1.0f / 100) + "%");
            pbProgress.setMax((int) downloadInfo.getTotalLength());
            pbProgress.setProgress((int) downloadInfo.getDownloadLength());
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == mLlItem.getId() && GroupVarManager.getInstance().isEditCache == 0) {
                switch (downloadInfo.getState()) {
                    case DownloadManager.PAUSE:
                        BaseApplication.mDownloadManager.addTask(downloadInfo.getUrl(), downloadInfo.getRequest(), downloadInfo.getListener());
                        break;
                    case DownloadManager.NONE:
                        BaseApplication.mDownloadManager.addTask(downloadInfo.getUrl(), downloadInfo.getRequest(), downloadInfo.getListener());
                        break;
                    case DownloadManager.ERROR:
                        BaseApplication.mDownloadManager.addTask(downloadInfo.getUrl(), downloadInfo.getRequest(), downloadInfo.getListener());
                        break;
                    case DownloadManager.DOWNLOADING:
                        BaseApplication.mDownloadManager.pauseTask(downloadInfo.getUrl());
                        break;
                    case DownloadManager.FINISH:

                        break;
                }
                refresh();
            } else if (v.getId() == mLlItem.getId() && GroupVarManager.getInstance().isEditCache == 1) {
                if (((DownbodyModel) downloadInfo.getData()).getIsSelected() == 1) {
                    ((DownbodyModel) downloadInfo.getData()).setIsSelected(0);
                    for (String s : GroupVarManager.getInstance().mCacheList) {
                        if (s.equals(downloadInfo.getTaskKey())) {
                            GroupVarManager.getInstance().mCacheList.remove(s);
                            break;
                        }
                    }
                } else {
                    ((DownbodyModel) downloadInfo.getData()).setIsSelected(1);
                    GroupVarManager.getInstance().mCacheList.add(downloadInfo.getTaskKey());
                }
                EventBus.getDefault().post(AppConstants.TAG_CACHE_DEL);
                mAdapter.notifyDataSetChanged();
            }
        }
    }

    private class MyDownloadListener extends DownloadListener {

        @Override
        public void onProgress(com.lzy.okserver.download.DownloadInfo downloadInfo) {
            if (getUserTag() == null) return;
            ViewHolder holder = (ViewHolder) getUserTag();
            holder.refresh();  //这里不能使用传递进来的 DownloadInfo，否者会出现条目错乱的问题
        }

        @Override
        public void onFinish(DownloadInfo downloadInfo) {
            Toast.makeText(mContext, "下载完成:" + downloadInfo.getTargetPath(), Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onError(DownloadInfo downloadInfo, String errorMsg, Exception e) {
            if (errorMsg != null) Toast.makeText(mContext, errorMsg, Toast.LENGTH_SHORT).show();
        }
    }
}
