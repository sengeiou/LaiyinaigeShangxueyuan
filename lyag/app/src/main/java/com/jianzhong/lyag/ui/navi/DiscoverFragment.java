package com.jianzhong.lyag.ui.navi;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.Result;
import com.baselib.widget.xlistview.XScrollView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.AudioSearchAdapter;
import com.jianzhong.lyag.adapter.ColumnSearchAdapter;
import com.jianzhong.lyag.adapter.CourseSearchAdapter;
import com.jianzhong.lyag.adapter.LiveSearchAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.AudioModel;
import com.jianzhong.lyag.model.ColumnContentModel;
import com.jianzhong.lyag.model.CourseListModel;
import com.jianzhong.lyag.model.DiscoverModel;
import com.jianzhong.lyag.model.LiveModel;
import com.jianzhong.lyag.util.CommonUtils;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 搜索结果发现页面
 * Created by zhengwencheng on 2018/3/27 0027.
 * package com.jianzhong.bs.ui.navi
 */
public class DiscoverFragment extends BaseFragment {

    @BindView(R.id.scroll_view)
    XScrollView mScrollView;
    private View mView; //要添加的头view
    private ViewHolder mViewHolder;
    private String keyWord;
    private DiscoverModel data;

    private CourseSearchAdapter mCourseSearchAdapter;
    private List<CourseListModel> mCourseListModels = new ArrayList<>();
    private ColumnSearchAdapter mColumnSearchAdapter;
    private List<ColumnContentModel> mColumnModels = new ArrayList<>();
    private LiveSearchAdapter mLiveSearchAdapter;
    private List<LiveModel> mLiveModels = new ArrayList<>();
    private AudioSearchAdapter mAudioSearchAdapter;
    private List<AudioModel> mAudioModels = new ArrayList<>();

    public static DiscoverFragment newInstance(String keyWord) {

        Bundle args = new Bundle();
        DiscoverFragment fragment = new DiscoverFragment();
        args.putString("keyWord", keyWord);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_discover, null);
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

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSearch(String tag) {
        if (tag.equals(AppConstants.TAG_DISCOVER)) {
            keyWord = GroupVarManager.getInstance().keyWord;

            getSearchFind();
        }
    }

    @Override
    protected void initData() {
        super.initData();
        keyWord = getArguments().getString("keyWord");

        mScrollView.setPullRefreshEnable(true);
        mScrollView.setPullLoadEnable(false);
        mScrollView.setAutoLoadEnable(true);
        mScrollView.setIXScrollViewListener(new XScrollView.IXScrollViewListener() {
            @Override
            public void onRefresh() {
                getSearchFind();
            }

            @Override
            public void onLoadMore() {
            }
        });
        mScrollView.setRefreshTime(CommonUtils.getTime());

        showCommonLoading();
        getSearchFind();
    }

    /**
     * 初始化View
     */
    private void initView() {

        if (mView == null) {
            mView = LayoutInflater.from(mContext).inflate(R.layout.content_discover, null);
            mViewHolder = new ViewHolder(mView);
            mViewHolder.mRvCourse.setFocusable(false);
            mViewHolder.mRvColumn.setFocusable(false);
            mViewHolder.mRvLive.setFocusable(false);
            mViewHolder.mRvAudio.setFocusable(false);
            mScrollView.setFocusable(true);
            mScrollView.setView(mView);
        }

        mCourseListModels.clear();
        mColumnModels.clear();
        mLiveModels.clear();
        mAudioModels.clear();

        //课程
        if (!ListUtils.isEmpty(data.getCourse())) {
            mCourseListModels.addAll(data.getCourse());
            mViewHolder.mLlCourse.setVisibility(View.VISIBLE);
        } else {
            mViewHolder.mLlCourse.setVisibility(View.GONE);
        }
        if (mCourseSearchAdapter == null) {
            mViewHolder.mRvCourse.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                    .color(getResources().getColor(R.color.color_grey_divider))
                    .sizeResId(R.dimen.default_divider_one)
                    .build());
            mViewHolder.mRvCourse.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mViewHolder.mRvCourse.setLayoutManager(mLayoutManager);
            mCourseSearchAdapter = new CourseSearchAdapter(mContext, mCourseListModels);
            mViewHolder.mRvCourse.setAdapter(mCourseSearchAdapter);
        } else {
            mCourseSearchAdapter.notifyDataSetChanged();
        }

        //专栏
        if (!ListUtils.isEmpty(data.getColumn())) {
            mViewHolder.mLlCourse.setVisibility(View.VISIBLE);
            mColumnModels.addAll(data.getColumn());
        } else {
            mViewHolder.mLlColumn.setVisibility(View.GONE);
        }
        if (mColumnSearchAdapter == null) {
            mViewHolder.mRvColumn.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                    .color(getResources().getColor(R.color.color_grey_divider))
                    .sizeResId(R.dimen.default_divider_one)
                    .build());
            mViewHolder.mRvColumn.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mViewHolder.mRvColumn.setLayoutManager(mLayoutManager);
            mColumnSearchAdapter = new ColumnSearchAdapter(mContext, mColumnModels);
            mViewHolder.mRvColumn.setAdapter(mColumnSearchAdapter);
        } else {
            mColumnSearchAdapter.notifyDataSetChanged();
        }

        //直播
        if (!ListUtils.isEmpty(data.getLive())) {
            mViewHolder.mLlLive.setVisibility(View.VISIBLE);
            mLiveModels.addAll(data.getLive());
        } else {
            mViewHolder.mLlLive.setVisibility(View.GONE);
        }
        if (mLiveSearchAdapter == null) {
            mViewHolder.mRvLive.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                    .color(getResources().getColor(R.color.color_grey_divider))
                    .sizeResId(R.dimen.default_divider_one)
                    .build());
            mViewHolder.mRvLive.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mViewHolder.mRvLive.setLayoutManager(mLayoutManager);
            mLiveSearchAdapter = new LiveSearchAdapter(mContext, mLiveModels);
            mViewHolder.mRvLive.setAdapter(mLiveSearchAdapter);

        } else {
            mLiveSearchAdapter.notifyDataSetChanged();
        }

        //音频
        if (!ListUtils.isEmpty(data.getAudio())) {
            mViewHolder.mLlAudio.setVisibility(View.VISIBLE);
            mAudioModels.addAll(data.getAudio());
        } else {
            mViewHolder.mLlAudio.setVisibility(View.GONE);
        }
        if (mAudioSearchAdapter == null) {
            mViewHolder.mRvAudio.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                    .color(getResources().getColor(R.color.color_grey_divider))
                    .sizeResId(R.dimen.default_divider_one)
                    .build());
            mViewHolder.mRvAudio.setHasFixedSize(true);
            LinearLayoutManager mLayoutManager = new LinearLayoutManager(getContext());
            mLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
            mViewHolder.mRvAudio.setLayoutManager(mLayoutManager);
            mAudioSearchAdapter = new AudioSearchAdapter(mContext, mAudioModels);
            mViewHolder.mRvAudio.setAdapter(mAudioSearchAdapter);
        } else {
            mAudioSearchAdapter.notifyDataSetChanged();
        }
    }

    /**
     * 获取搜索发现数据
     */
    private void getSearchFind() {
        Map<String, Object> params = new HashMap<>();
        params.put("key_word", keyWord);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_SERCH_FIND, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<DiscoverModel> result = GsonUtils.json2Bean(s, DiscoverModel.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    hideCommonLoading();
                    mScrollView.stopRefresh();
                    data = result.getData();
                    initView();
                }
                isShowNoDataView(data);
            }

            @Override
            public void onFailure(String msg) {
                showErrorView();
            }
        });
    }

    class ViewHolder {
        @BindView(R.id.rv_course)
        RecyclerView mRvCourse;
        @BindView(R.id.ll_course)
        LinearLayout mLlCourse;
        @BindView(R.id.rv_column)
        RecyclerView mRvColumn;
        @BindView(R.id.ll_column)
        LinearLayout mLlColumn;
        @BindView(R.id.rv_live)
        RecyclerView mRvLive;
        @BindView(R.id.ll_live)
        LinearLayout mLlLive;
        @BindView(R.id.rv_audio)
        RecyclerView mRvAudio;
        @BindView(R.id.ll_audio)
        LinearLayout mLlAudio;

        @OnClick({R.id.tv_course_more, R.id.tv_column_more, R.id.tv_live_more, R.id.tv_audio_more})
        public void onViewClicked(View view) {
            switch (view.getId()) {
                case R.id.tv_course_more:
//                    intent = new Intent(mContext, NaviResultActivity.class);
//                    intent.putExtra("search", AppConstants.TAG_COURSE);
//                    intent.putExtra("is_more", "1");
//                    intent.putExtra("type", 1);
//                    intent.putExtra("title", "精品课程");
//                    GroupVarManager.getInstance().path_1 = AppConstants.TAG_COURSE_NAVI;
//                    startActivity(intent);
                    GroupVarManager.getInstance().curSearch = 1;
                    EventBus.getDefault().post(AppConstants.EVENT_SWITCH_SEARCH);
                    break;
                case R.id.tv_column_more:
//                    intent = new Intent(mContext, NaviResultActivity.class);
//                    intent.putExtra("search", AppConstants.TAG_COLUMN);
//                    intent.putExtra("is_more", "1");
//                    intent.putExtra("type", 1);
//                    intent.putExtra("title", "专栏");
//                    GroupVarManager.getInstance().path_1 = AppConstants.TAG_COLUMN_NAVI;
//                    startActivity(intent);
                    GroupVarManager.getInstance().curSearch = 2;
                    EventBus.getDefault().post(AppConstants.EVENT_SWITCH_SEARCH);
                    break;
                case R.id.tv_live_more:
//                    intent = new Intent(mContext, NaviResultActivity.class);
//                    intent.putExtra("search", AppConstants.TAG_LIVE);
//                    intent.putExtra("is_more", "1");
//                    intent.putExtra("type", 1);
//                    intent.putExtra("title", "直播");
//                    GroupVarManager.getInstance().path_1 = AppConstants.TAG_LIVE_NAVI;
//                    startActivity(intent);
                    GroupVarManager.getInstance().curSearch = 3;
                    EventBus.getDefault().post(AppConstants.EVENT_SWITCH_SEARCH);
                    break;
                case R.id.tv_audio_more:
//                    intent = new Intent(mContext, NaviResultActivity.class);
//                    intent.putExtra("search", AppConstants.TAG_AUDIO);
//                    intent.putExtra("is_more", "1");
//                    intent.putExtra("type", 1);
//                    intent.putExtra("title", "音频");
//                    GroupVarManager.getInstance().path_1 = AppConstants.TAG_AUDIO_NAVI;
//                    startActivity(intent);
                    GroupVarManager.getInstance().curSearch = 4;
                    EventBus.getDefault().post(AppConstants.EVENT_SWITCH_SEARCH);
                    break;
            }
        }

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}



