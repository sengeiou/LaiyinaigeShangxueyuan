package com.jianzhong.lyag.ui.navi;

import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.ResultList;
import com.baselib.util.ToastUtils;
import com.baselib.widget.CustomListView;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.SearchRecordAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.db.dao.SearchRecordDao;
import com.jianzhong.lyag.flexboxlayout.TagAdapter;
import com.jianzhong.lyag.flexboxlayout.TagFlowLayout;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.listener.OnSearchRecListener;
import com.jianzhong.lyag.model.SearchRecordModel;
import com.jianzhong.lyag.model.SearchSignModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 全部搜索的页面
 * Created by zhengwencheng on 2018/1/27 0027.
 * package com.jianzhong.bs.ui.navigation
 */
public class CommonSearchActivity extends ToolbarActivity {

    @BindView(R.id.et_search)
    EditText mEtSearch;
    @BindView(R.id.tag_FlowLayout)
    TagFlowLayout mTagFlowLayout;
    @BindView(R.id.lv_history)
    CustomListView mLvHistory;

    private List<SearchSignModel> mSearchSignModels = new ArrayList<>();
    private LinkedList<SearchRecordModel> mSearchRecordModels = new LinkedList<>();
    private SearchRecordAdapter mSearchRecordAdapter;
    //搜索记录的数据库
    private SearchRecordDao mSearchRecordDao;
    private String keyWord;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_common_search);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        getSearchHitWord();
        //
        initSearchRecord();
        //
        initListener();
    }

    @OnClick({R.id.tv_cancel, R.id.tv_more_classify})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_cancel:    //取消
                CommonSearchActivity.this.finish();
                break;
            case R.id.tv_more_classify: //分类
                Intent intent = new Intent(mContext, HomeNaviActivity.class);
                startActivity(intent);
                break;
        }
    }

    /**
     * 初始化搜索记录
     */
    private void initSearchRecord() {
        mSearchRecordDao = new SearchRecordDao(mContext);
        Map<String, Object> map = new HashMap<>();
        map.put("user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
        List<SearchRecordModel> data = mSearchRecordDao.queryForValue(map);
        if (!ListUtils.isEmpty(data)) {
            for (int i = data.size(); i > 0; i--) {
                mSearchRecordModels.add(data.get(i - 1));
            }
        }

        mSearchRecordAdapter = new SearchRecordAdapter(mContext, mSearchRecordModels, new OnSearchRecListener() {
            @Override
            public void callBack(int position, SearchRecordModel item) {
                mSearchRecordModels.remove(position);
                mSearchRecordDao.delete(item);
                mSearchRecordAdapter.notifyDataSetChanged();
            }
        });
        mLvHistory.setAdapter(mSearchRecordAdapter);
    }

    /**
     * 初始化热门搜索标签
     */
    private void initSign(List<String> list) {
        mTagFlowLayout.setAdapter(new TagAdapter<String>(list) {
            @Override
            protected View getView(ViewGroup parent, int position, final String item) {
                TextView tv = (TextView) mInflater.inflate(R.layout.item_flow_layout, parent, false);
                tv.setText(item);

                tv.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        Intent intent = new Intent(mContext, SearchResultActivity.class);
                        intent.putExtra("keyWord", item);
                        startActivity(intent);

                        //数据库插入数据
                        SearchRecordModel mSearchRecordModel = new SearchRecordModel();
                        mSearchRecordModel.setSearch_record(item);
                        mSearchRecordModel.setUser_id(AppUserModel.getInstance().getmUserModel().getUser_id());
                        if (mSearchRecordModels.size() == 15) {
                            mSearchRecordDao.delete(mSearchRecordModels.getLast());
                            mSearchRecordModels.removeLast();
                        }
                        mSearchRecordDao.insert(mSearchRecordModel);
                        mSearchRecordModels.addFirst(mSearchRecordModel);
                        mSearchRecordAdapter.notifyDataSetChanged();

                    }
                });
                return tv;
            }

        });
    }

    /**
     * 获取搜索热词
     */
    private void getSearchHitWord() {
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_SEARCH_HIT_WORD, null, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<String> resultList = GsonUtils.json2List(s, String.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    initSign(resultList.getData());
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    private void initListener() {
        mEtSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {

            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {

                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(INPUT_METHOD_SERVICE))
                            .hideSoftInputFromWindow(CommonSearchActivity.this.getCurrentFocus()
                                    .getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    //进行搜索操作的方法，在该方法中可以加入mEtClassSearch的非空判断
                    if ((mEtSearch.getText().toString()).isEmpty()) {
                        ToastUtils.show(mContext, "请输入搜索关键字");
                    } else {
                        keyWord = mEtSearch.getText().toString();

                        Intent intent = new Intent(CommonSearchActivity.this, SearchResultActivity.class);
                        intent.putExtra("keyWord", keyWord);
                        startActivity(intent);

                        //数据库插入数据
                        SearchRecordModel item = new SearchRecordModel();
                        item.setSearch_record(keyWord);
                        item.setUser_id(AppUserModel.getInstance().getmUserModel().getUser_id());
                        if (mSearchRecordModels.size() == 15) {
                            mSearchRecordDao.delete(mSearchRecordModels.getLast());
                            mSearchRecordModels.removeLast();
                        }
                        mSearchRecordDao.insert(item);
                        mSearchRecordModels.addFirst(item);
                        mSearchRecordAdapter.notifyDataSetChanged();
                    }

                }
                return false;
            }
        });
    }
}
