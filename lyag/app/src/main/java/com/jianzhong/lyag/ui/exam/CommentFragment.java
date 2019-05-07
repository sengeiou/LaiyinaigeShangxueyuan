package com.jianzhong.lyag.ui.exam;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.InputMethodUtil;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.CommentAdapter;
import com.jianzhong.lyag.base.BaseFragment;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnCommentListener;
import com.jianzhong.lyag.model.CommentListModel;
import com.jianzhong.lyag.model.CommentModel;
import com.jianzhong.lyag.util.DialogHelper;
import com.yqritc.recyclerviewflexibledivider.HorizontalDividerItemDecoration;
import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

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
 * 评论Fragment
 * Created by zhengwencheng on 2018/2/27 0027.
 * package com.jianzhong.bs.ui.exam
 */
public class CommentFragment extends BaseFragment {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.ptr_frame)
    PtrClassicFrameLayout mPtrFrame;
    @BindView(R.id.et_content)
    EditText mEtContent;
    private List<CommentModel> mList = new ArrayList<>();
    private CommentAdapter mAdapter;
    private int pageIndex = 1;
    private String course_id;
    //要添加的头
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private View headView;
    private ViewHolder mViewHolder;
    private boolean isAddHead = false;
    private String content = "";
    private String top_id = "0"; //评论的时候使用 默认为0 回复别人时不为0
    public static CommentFragment newInstance(String course_id) {

        Bundle args = new Bundle();
        CommentFragment fragment = new CommentFragment();
        args.putString("course_id", course_id);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View initView(LayoutInflater inflater) {
        mRootView = inflater.inflate(R.layout.fragment_comment, null);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();

        course_id = getArguments().getString("course_id");
        initRecyclerView();

        initCommentList();
    }

    private void initRecyclerView() {
        /**设置RecyclerView的模式*/
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(mContext)
                .color(getResources().getColor(R.color.color_white))
                .sizeResId(R.dimen.default_divider_zero)
                .build());
        mPtrFrame.setMode(PtrFrameLayout.Mode.BOTH);

        /**上拉和下拉*/
        mPtrFrame.setPtrHandler(new PtrDefaultHandler2() {
            @Override
            public void onLoadMoreBegin(PtrFrameLayout frame) {
                pageIndex++;
                getCommnetList();
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {

                pageIndex = 1;
                mList.clear();
                getCommnetList();
            }

        });
    }

    /**
     * 初始化评论列表
     */
    private void initCommentList() {
        mAdapter = new CommentAdapter(mContext, mList, course_id, new OnCommentListener() {

            @Override
            public void OnClick(final CommentModel item, final int position) {
                new AlertDialog.Builder(getActivity()).setTitle("提示").
                        setMessage("您确认删除该评论吗").
                        setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                delComment(item, position);
                            }
                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        return;
                    }
                }).create().show();
            }
        });
        if (headView == null) {
            // 避免出现RecyclerView has no LayoutManager的错误
            mRecyclerView.setHasFixedSize(true);
            // 计算RecyclerView的大小，可以显示其内容
            mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
            headView = mInflater.inflate(R.layout.headview_comment_list, mRecyclerView, false);
            mViewHolder = new ViewHolder(headView);
        }

        if (mHeaderAndFooterWrapper == null) {
            mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mAdapter);
        }
        if (!isAddHead) {
            mHeaderAndFooterWrapper.addHeaderView(headView);
            isAddHead = true;
        }
        mRecyclerView.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();

        mEtContent.setHint("请输入评论");
        mEtContent.clearFocus();
        // 评论动态
        mEtContent.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEND || (event != null && event.getKeyCode() == KeyEvent.KEYCODE_ENTER)) {
                    if(!StringUtils.isEmpty(mEtContent.getText().toString().trim()) && !content.equals(mEtContent.getText().toString().trim())){
                        content = mEtContent.getText().toString().trim();
                        Map<String, Object> params = new HashMap<String, Object>();
                        params.put("foreign_id", course_id);
                        params.put("content", content);
                        params.put("top_id", top_id);
                        params.put("asset_user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
                        params.put("asset_type", AppConstants.TAG_COURSE);
                        addComment(params);
                    }
                    return true;
                } else {
                    return false;
                }
            }
        });

        //
        getCommnetList();
    }

    /**
     * 获取评论列表
     */
    private void getCommnetList() {
        Map<String, Object> params = new HashMap<>();
        params.put("foreign_id", course_id);
        params.put("asset_type", AppConstants.TAG_COURSE);
        params.put("p", pageIndex + "");
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COMMNET_LIST, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result<CommentListModel> resultList = GsonUtils.json2Bean(s, CommentListModel.class);
                if (resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    mPtrFrame.refreshComplete();
                    mList.addAll(resultList.getData().getList());
                    mViewHolder.mTvCommentCount.setText("共" + resultList.getData().getCount() + "条评论");
                    mHeaderAndFooterWrapper.notifyDataSetChanged();
                } else {
                    ToastUtils.show(mContext, resultList != null ? resultList.getMessage() : "数据解析错误");
                }
            }

            @Override
            public void onFailure(String msg) {
                mPtrFrame.refreshComplete();
                ToastUtils.show(mContext, msg);
            }
        });
    }

    /**
     * 添加新的评论
     * @param params
     */
    private void addComment(Map<String,Object> params){
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COMMENT_ADD, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                DialogHelper.dismissDialog();
                Result result = GsonUtils.json2Bean(s,Result.class);
                if(result.getCode() == HttpConfig.STATUS_SUCCESS){
                    InputMethodUtil.hiddenInputMethod(mContext, mViewHolder.mTvCommentCount);
                    ToastUtils.show(mContext,"评论成功");
                    mEtContent.setText("");
                    mEtContent.setHint("请输入评论");
                    mEtContent.clearFocus();
                    //刷新评论列表 先这样 后面优化不刷新
                    pageIndex = 0;
                    mList.clear();
                    getCommnetList();
                }else {
                    ToastUtils.show(mContext,result.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                DialogHelper.dismissDialog();
                ToastUtils.show(mContext,msg);
            }
        });
    }

    /**
     * 删除评论
     * @param
     */
    private void delComment(CommentModel item, final int position){
        Map<String,Object> params = new HashMap<>();
        params.put("comment_id", item.getComment_id());
        params.put("top_id", item.getTop_id());
        params.put("asset_type", item.getAsset_type());
        params.put("foreign_id", course_id);

        DialogHelper.showDialog(mContext);
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COMMENT_DEL, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s,Result.class);
                DialogHelper.dismissDialog();
                if(result.getCode() == HttpConfig.STATUS_SUCCESS){
                    mPtrFrame.autoRefresh();
                }else {
                    ToastUtils.show(mContext,result != null ? result.getMessage():"");
                }
            }

            @Override
            public void onFailure(String msg) {
                DialogHelper.dismissDialog();
                ToastUtils.show(mContext,msg);
            }
        });
    }

    class ViewHolder {
        @BindView(R.id.tv_comment_count)
        TextView mTvCommentCount;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
