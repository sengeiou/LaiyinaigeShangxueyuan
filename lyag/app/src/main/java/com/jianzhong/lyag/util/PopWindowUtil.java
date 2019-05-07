package com.jianzhong.lyag.util;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.baselib.util.ListUtils;
import com.baselib.util.ToastUtils;
import com.baselib.widget.FixedPopupWindow;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.CommonItemAdapter;
import com.jianzhong.lyag.adapter.CommonMenuAdapter;
import com.jianzhong.lyag.adapter.HepSortItemAdapter;
import com.jianzhong.lyag.adapter.NaviClassifyFirstAdapter;
import com.jianzhong.lyag.adapter.NaviClassifySecAdapter;
import com.jianzhong.lyag.adapter.NaviClassifyThirdAdapter;
import com.jianzhong.lyag.adapter.OrderByKeyItemAdapter;
import com.jianzhong.lyag.adapter.RankScopeAdapter;
import com.jianzhong.lyag.flexboxlayout.TagAdapter;
import com.jianzhong.lyag.flexboxlayout.TagFlowLayout;
import com.jianzhong.lyag.listener.OnItemsSelectInterface;
import com.jianzhong.lyag.listener.OnLiveListener;
import com.jianzhong.lyag.listener.OnSelectListener;
import com.jianzhong.lyag.model.CommonMenuModel;
import com.jianzhong.lyag.model.HelpSortFieldModel;
import com.jianzhong.lyag.model.NavigationFirstModel;
import com.jianzhong.lyag.model.NavigationSecModel;
import com.jianzhong.lyag.model.NavigationSecSonModel;
import com.jianzhong.lyag.model.OrderKeyModel;
import com.jianzhong.lyag.model.RankScopeModel;
import com.jianzhong.lyag.model.TagModel;

import java.util.ArrayList;
import java.util.List;

/**
 * PopWindow管理类
 * Created by zhengwencheng on 2017/2/13.
 * package com.jianzhong.ys.util
 */

public class PopWindowUtil {

    private static PopWindowUtil instance = null;
    private NaviClassifySecAdapter mSecAdapter;
    private NaviClassifyFirstAdapter mFirstAdapter;
    private NaviClassifyThirdAdapter mThirdAdapter;

    private ListView mLvFirst;
    private ListView mLvSec;
    private ListView mLvThird;
    private List<NavigationSecModel> mNavigationSecModels = new ArrayList<>();
    private List<NavigationSecSonModel> mNavigationSecSonModels = new ArrayList<>();
    private List<NavigationFirstModel> mNavigationFirstModels = new ArrayList<>();
    private FixedPopupWindow popupWindow_navi;
    private View view_navi;

    /**
     * 获取单例
     *
     * @return
     */
    public static PopWindowUtil getInstance() {
        if (instance == null) {
            instance = new PopWindowUtil();
        }
        return instance;
    }

    /**
     * 通用的Item选择PopupWindow选项框
     *
     * @param mActivity
     * @param view
     * @param mList
     * @param tvTitle
     * @param listener
     */
    public void showCommonItemSelect(final Activity mActivity, View view, final List<String> mList, final TextView tvTitle, final OnItemClickListener listener) {
        if (ListUtils.isEmpty(mList)) {
            ToastUtils.show(mActivity, "没有选项数据！");
            return;
        }

        View popwindow_view = mActivity.getLayoutInflater().inflate(R.layout.popwindow_item_select, null, false);
        // 创建PopupWindow实例
        final PopupWindow popupWindow = new PopupWindow(popwindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 让pop可以点击外面消失掉
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // 设置动画效果
        popupWindow.setAnimationStyle(R.style.PopAnimationFade);
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 0.7f;
        mActivity.getWindow().setAttributes(params);
        // 这里是位置显示方式,在屏幕的左侧
        popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);

        if (mList.size() > 10) {
            WindowManager wm = mActivity.getWindowManager();
            int height = wm.getDefaultDisplay().getHeight();
            FrameLayout.LayoutParams linearParams = (FrameLayout.LayoutParams) popwindow_view.getLayoutParams();
            linearParams.height = height / 2;
            popwindow_view.setLayoutParams(linearParams);
        }

        // 点击其它地方关闭对话框
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1f;
                mActivity.getWindow().setAttributes(params);
                popupWindow.dismiss();
            }
        });

        // 列表填充数据
        ListView mLvType = (ListView) popwindow_view.findViewById(R.id.lv_customer_type);
        CommonItemAdapter mAdapter = new CommonItemAdapter(mActivity, mList);
        mLvType.setAdapter(mAdapter);

        // 点击item设置数据
        mLvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tvTitle != null) {
                    tvTitle.setText(mList.get(position));
                }

                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    popupWindow.dismiss();
                }

                if (listener != null) {
                    listener.OnItemClickListener(mList.get(position), position);
                }
            }
        });

    }

    public interface OnItemClickListener {
        void OnItemClickListener(String title, int position);
    }


    public void showOrderByKey(final Activity mActivity, final View view, final List<OrderKeyModel> mList, final TextView tvTitle, final OnOrderByKeyClickListener listener) {
        if (ListUtils.isEmpty(mList)) {
            ToastUtils.show(mActivity, "没有选项数据！");
            return;
        }

        View popwindow_view = mActivity.getLayoutInflater().inflate(R.layout.popwindow_common_select, null, false);
        // 创建PopupWindow实例
        final FixedPopupWindow popupWindow = new FixedPopupWindow(popwindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 让pop可以点击外面消失掉
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // 设置动画效果
//        popupWindow.setAnimationStyle(R.style.PopAnimationFade_up_bottom);
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 1f;
        mActivity.getWindow().setAttributes(params);

        // 点击其它地方关闭对话框
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1f;
                mActivity.getWindow().setAttributes(params);
                view.setSelected(false);
                popupWindow.dismiss();
            }
        });

        //设置未生效的地方为半透明
        LinearLayout mLlAlpha = (LinearLayout) popwindow_view.findViewById(R.id.ll_alpha);
        mLlAlpha.setAlpha((float) 0.5);
        // 列表填充数据
        ListView mLvType = (ListView) popwindow_view.findViewById(R.id.lv_customer_type);
        OrderByKeyItemAdapter mAdapter = new OrderByKeyItemAdapter(mActivity, mList);
        mLvType.setAdapter(mAdapter);

        // 点击item设置数据
        mLvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tvTitle != null) {
                    tvTitle.setText(mList.get(position).getStr());
                }

                for (int i = 0; i < mList.size(); i++) {
                    if (i == position) {
                        mList.get(i).setIsSelected(1);
                    } else {
                        mList.get(i).setIsSelected(0);
                    }
                }

                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    view.setSelected(false);
                    popupWindow.dismiss();
                }

                if (listener != null) {
                    listener.orderByKeyClick(mList.get(position), position);
                }
            }
        });

        //点击阴影处消失
        mLlAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    view.setSelected(false);
                    popupWindow.dismiss();
                }
            }
        });

        view.setSelected(true);
        popupWindow.showAsDropDown(view);
    }

    public interface OnOrderByKeyClickListener {
        void orderByKeyClick(OrderKeyModel item, int position);
    }

    /**
     * 课程列表筛选的列表选择
     *
     * @param mActivity
     * @param view
     * @param mList
     * @param tvTitle
     * @param onItemSecClickListener
     * @param onItemSecSonClickListener
     * @param onItemFirstClickListener
     */
    public void showNavigationSelect(final Activity mActivity, final View view, final List<NavigationFirstModel> mList, final TextView tvTitle,
                                     final OnItemSecClickListener onItemSecClickListener, final OnItemSecSonClickListener onItemSecSonClickListener, final OnItemFirstClickListener onItemFirstClickListener) {
        if (ListUtils.isEmpty(mList)) {
            ToastUtils.show(mActivity, "没有选项数据");
            return;
        }

        if (view_navi == null) {
            view_navi = mActivity.getLayoutInflater().inflate(R.layout.popwindow_navigation_classify, null, false);
        }

        // 创建PopupWindow实例
        if (popupWindow_navi == null) {

            popupWindow_navi = new FixedPopupWindow(view_navi, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        }
        popupWindow_navi.setOutsideTouchable(true);
        popupWindow_navi.setFocusable(true);
        // 让pop可以点击外面消失掉
        popupWindow_navi.setBackgroundDrawable(new ColorDrawable(0));
        // 设置动画效果
//        popupWindow.setAnimationStyle(R.style.PopAnimationFade_up_bottom);
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 1f;
        mActivity.getWindow().setAttributes(params);
        // 点击其它地方关闭对话框
        popupWindow_navi.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1f;
                mActivity.getWindow().setAttributes(params);
                view.setSelected(false);
                popupWindow_navi.dismiss();
            }
        });
        //设置未生效的地方为半透明
        LinearLayout mLlAlpha = (LinearLayout) view_navi.findViewById(R.id.ll_alpha);
        mLlAlpha.setAlpha((float) 0.5);

        // 列表填充数据
        mLvFirst = (ListView) view_navi.findViewById(R.id.lv_first);
        mLvSec = (ListView) view_navi.findViewById(R.id.lv_sec);
        mLvThird = (ListView) view_navi.findViewById(R.id.lv_third);

        mNavigationFirstModels.clear();
        mNavigationFirstModels.addAll(mList);
        //初始化数据
        for (int i = 0; i < mNavigationFirstModels.size(); i++) {
            if (mNavigationFirstModels.get(i).getIsSelected() == 1) {
                mNavigationSecModels.clear();
                mNavigationSecModels.addAll(mNavigationFirstModels.get(i).getSub());
                if (!ListUtils.isEmpty(mNavigationFirstModels.get(i).getSub())) {
                    for (int j = 0; j < mNavigationFirstModels.get(i).getSub().size(); j++) {
                        if (mNavigationFirstModels.get(i).getSub().get(j).getIsSelected() == 1) {
                            mNavigationSecSonModels.clear();
                            if (!ListUtils.isEmpty(mNavigationFirstModels.get(i).getSub().get(j).getSub())) {
                                mNavigationSecSonModels.addAll(mNavigationFirstModels.get(i).getSub().get(j).getSub());
                            }
                        }
                    }
                }
            }
        }

        mFirstAdapter = new NaviClassifyFirstAdapter(mActivity, mNavigationFirstModels, new OnItemsSelectInterface() {
            @Override
            public void OnClick(int which) {
                mNavigationSecModels.clear();
                mNavigationSecSonModels.clear();
                if (!ListUtils.isEmpty(mNavigationFirstModels.get(which).getSub())) {
                    mNavigationSecModels.addAll(mNavigationFirstModels.get(which).getSub());
                }
                mSecAdapter.notifyDataSetChanged();
                mThirdAdapter.notifyDataSetChanged();
                onItemFirstClickListener.OnItemFirstClick(mNavigationFirstModels.get(which).getOrder_by(), which);
            }
        });
        mLvFirst.setAdapter(mFirstAdapter);

        mSecAdapter = new NaviClassifySecAdapter(mActivity, mNavigationSecModels, new OnItemsSelectInterface() {
            @Override
            public void OnClick(int which) {
                mNavigationSecSonModels.clear();
                if (!ListUtils.isEmpty(mNavigationSecModels.get(which).getSub())) {
                    mNavigationSecSonModels.addAll(mNavigationSecModels.get(which).getSub());
                }
                mThirdAdapter.notifyDataSetChanged();
                if (ListUtils.isEmpty(mNavigationSecSonModels)) {
                    if (tvTitle != null) {
                        tvTitle.setText(mNavigationSecModels.get(which).getTitle());
                    }

                    if (popupWindow_navi != null && popupWindow_navi.isShowing()) {
                        WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                        params.alpha = 1f;
                        mActivity.getWindow().setAttributes(params);
                        view.setSelected(false);
                        popupWindow_navi.dismiss();
                    }

                    if (onItemSecClickListener != null) {
                        onItemSecClickListener.OnItemSecClick(mNavigationSecModels.get(which), which);
                    }
                }
            }
        });
        mLvSec.setAdapter(mSecAdapter);

        mThirdAdapter = new NaviClassifyThirdAdapter(mActivity, mNavigationSecSonModels, new OnItemsSelectInterface() {
            @Override
            public void OnClick(int which) {
                if (tvTitle != null) {
                    tvTitle.setText(mNavigationSecSonModels.get(which).getTitle());
                }

                if (popupWindow_navi != null && popupWindow_navi.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    view.setSelected(false);
                    popupWindow_navi.dismiss();
                }

                if (onItemSecSonClickListener != null) {
                    onItemSecSonClickListener.OnItemSecSonClick(mNavigationSecSonModels.get(which), which);
                }

            }
        });
        mLvThird.setAdapter(mThirdAdapter);

        view.setSelected(true);
        popupWindow_navi.showAsDropDown(view);
    }

    public interface OnItemFirstClickListener {
        void OnItemFirstClick(List<OrderKeyModel> items, int position);
    }

    public interface OnItemSecClickListener {
        void OnItemSecClick(NavigationSecModel item, int position);
    }

    public interface OnItemSecSonClickListener {
        void OnItemSecSonClick(NavigationSecSonModel item, int position);
    }

    /**
     * 公共菜单栏显示
     *
     * @param mActivity
     * @param view
     * @param mList
     * @param listener
     */
    public void showMenu(final Activity mActivity, View view, final List<CommonMenuModel> mList, final OnMenuItemClickListener listener) {

        View popwindow_view = mActivity.getLayoutInflater().inflate(R.layout.popwindow_menu, null, false);
        // 创建PopupWindow实例
        final PopupWindow popupWindow = new PopupWindow(popwindow_view, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        popupWindow.showAsDropDown(view);

        // 点击其它地方关闭对话框
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1f;
                mActivity.getWindow().setAttributes(params);
                popupWindow.dismiss();
            }
        });

        // 列表填充数据
        ListView mListView = (ListView) popwindow_view.findViewById(R.id.lv_menu);
        CommonMenuAdapter mAdapter = new CommonMenuAdapter(mActivity, mList);
        mListView.setAdapter(mAdapter);

        // 点击item设置数据
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    popupWindow.dismiss();
                }

                if (listener != null) {
                    listener.OnMenuItemClickListener(mList.get(position), position);
                }
            }
        });

    }

    public interface OnMenuItemClickListener {
        void OnMenuItemClickListener(CommonMenuModel item, int position);
    }


    /**
     * 求助列表排序选择
     *
     * @param mActivity
     * @param view
     * @param mList
     * @param tvTitle
     * @param listener
     */
    public void showHelpSortField(final Activity mActivity, final View view, final List<HelpSortFieldModel> mList, final TextView tvTitle, final OnHelpSortClickListener listener) {
        if (ListUtils.isEmpty(mList)) {
            ToastUtils.show(mActivity, "没有选项数据！");
            return;
        }

        View popwindow_view = mActivity.getLayoutInflater().inflate(R.layout.popwindow_common_select, null, false);
        // 创建PopupWindow实例
        final FixedPopupWindow popupWindow = new FixedPopupWindow(popwindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 让pop可以点击外面消失掉
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // 设置动画效果
//        popupWindow.setAnimationStyle(R.style.PopAnimationFade_up_bottom);
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 1f;
        mActivity.getWindow().setAttributes(params);

        // 点击其它地方关闭对话框
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1f;
                mActivity.getWindow().setAttributes(params);
                view.setSelected(false);
                popupWindow.dismiss();
            }
        });

        //设置未生效的地方为半透明
        LinearLayout mLlAlpha = (LinearLayout) popwindow_view.findViewById(R.id.ll_alpha);
        mLlAlpha.setAlpha((float) 0.5);
        // 列表填充数据
        ListView mLvType = (ListView) popwindow_view.findViewById(R.id.lv_customer_type);
        HepSortItemAdapter mAdapter = new HepSortItemAdapter(mActivity, mList);
        mLvType.setAdapter(mAdapter);

        // 点击item设置数据
        mLvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (tvTitle != null) {
                    tvTitle.setText(mList.get(position).getStr());
                }

                for (int i = 0; i < mList.size(); i++) {
                    if (i == position && mList.get(i).getIsSelected() == 0) {
                        mList.get(i).setIsSelected(1);
                    } else {
                        mList.get(i).setIsSelected(0);
                    }
                }

                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    view.setSelected(false);
                    popupWindow.dismiss();
                }

                if (listener != null) {
                    listener.helpSortClick(mList.get(position), position);
                }
            }
        });

        //点击阴影处消失
        mLlAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    view.setSelected(false);
                    popupWindow.dismiss();
                }
            }
        });

        view.setSelected(true);
        popupWindow.showAsDropDown(view);
    }


    public interface OnHelpSortClickListener {
        void helpSortClick(HelpSortFieldModel item, int position);
    }

    /**
     * 新建标签列表选择
     *
     * @param mActivity
     * @param view
     * @param mList
     * @param tvTitle
     * @param listener
     */
    public void showTagSelect(final Activity mActivity, final View view, final List<TagModel> mList, final TextView tvTitle, final OnTagClickListener listener) {
        if (ListUtils.isEmpty(mList)) {
            ToastUtils.show(mActivity, "没有选项数据！");
            return;
        }
        View popwindow_view = mActivity.getLayoutInflater().inflate(R.layout.popwindow_tag_select, null, false);
        // 创建PopupWindow实例
        final FixedPopupWindow popupWindow = new FixedPopupWindow(popwindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 让pop可以点击外面消失掉
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // 设置动画效果
//        popupWindow.setAnimationStyle(R.style.PopAnimationFade_up_bottom);
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 1f;
        mActivity.getWindow().setAttributes(params);

        // 点击其它地方关闭对话框
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1f;
                mActivity.getWindow().setAttributes(params);
                view.setSelected(false);
                popupWindow.dismiss();
            }
        });

        //设置未生效的地方为半透明
        LinearLayout mLlAlpha = (LinearLayout) popwindow_view.findViewById(R.id.ll_alpha);
        mLlAlpha.setAlpha((float) 0.5);
        // 列表填充数据
        TagFlowLayout mTagFlowLayout = (TagFlowLayout) popwindow_view.findViewById(R.id.tag_FlowLayout);

        mTagFlowLayout.setAdapter(new TagAdapter<TagModel>(mList) {
            @Override
            protected View getView(ViewGroup parent, final int position, final TagModel item) {
                final TextView tv = (TextView) LayoutInflater.from(mActivity).inflate(R.layout.item_notice_flow_layout, parent, false);
                tv.setText(item.getTag_name());
                if (item.getIsSelected() == 1) {
                    tv.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_item_theme_white_16));
                    tv.setTextColor(mActivity.getResources().getColor(R.color.color_theme));
                } else {
                    tv.setBackground(mActivity.getResources().getDrawable(R.drawable.shape_item_gray_white_16));
                    tv.setTextColor(mActivity.getResources().getColor(R.color.color_888888));
                }
                tv.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {

                        for (int i = 0; i < mList.size(); i++) {
                            if (i == position && mList.get(i).getIsSelected() == 0) {
                                mList.get(i).setIsSelected(1);
                            } else {
                                mList.get(i).setIsSelected(0);
                            }
                        }

                        notifyDataSetChanged();

                        //点击item的回调设置
                        if (tvTitle != null) {
                            tvTitle.setText(item.getTag_name());
                        }

                        if (popupWindow != null && popupWindow.isShowing()) {
                            WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                            params.alpha = 1f;
                            mActivity.getWindow().setAttributes(params);
                            view.setSelected(false);
                            popupWindow.dismiss();
                        }

                        if (listener != null) {
                            listener.tagClick(item, position);
                        }
                    }
                });
                return tv;
            }
        });

        //点击阴影处消失
        mLlAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    view.setSelected(false);
                    popupWindow.dismiss();
                }
            }
        });

        view.setSelected(true);
        popupWindow.showAsDropDown(view);
    }


    public interface OnTagClickListener {
        void tagClick(TagModel item, int position);
    }


    /**
     * @param mActivity
     * @param view
     * @param mList
     * @param onSelectListener
     */
    public void showRankScope(final Activity mActivity, View view, final List<RankScopeModel> mList, final OnSelectListener onSelectListener) {
        if (ListUtils.isEmpty(mList)) {
            ToastUtils.show(mActivity, "没有选项数据！");
            return;
        }
        View popwindow_view = mActivity.getLayoutInflater().inflate(R.layout.popwindow_rank, null, false);
        // 创建PopupWindow实例
        final FixedPopupWindow popupWindow = new FixedPopupWindow(popwindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 让pop可以点击外面消失掉
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // 设置动画效果
//        popupWindow.setAnimationStyle(R.style.PopAnimationFade_up_bottom);
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 1f;
        mActivity.getWindow().setAttributes(params);

        // 点击其它地方关闭对话框
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1f;
                mActivity.getWindow().setAttributes(params);
                popupWindow.dismiss();
            }
        });

        LinearLayout mLlAlpha = (LinearLayout) popwindow_view.findViewById(R.id.ll_alpha);
        mLlAlpha.setAlpha((float) 0.5);
        // 列表填充数据
        ListView mLvType = (ListView) popwindow_view.findViewById(R.id.lv_customer_type);
        final RankScopeAdapter mAdapter = new RankScopeAdapter(mActivity, mList);
        mLvType.setAdapter(mAdapter);

        // 点击item设置数据
        mLvType.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                for (int i = 0; i < mList.size(); i++) {
                    if (i == position) {
                        mList.get(i).setSelected(true);
                    } else {
                        mList.get(i).setSelected(false);
                    }
                }
                mAdapter.notifyDataSetChanged();

                onSelectListener.selectItem((RankScopeModel) parent.getItemAtPosition(position));
                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    popupWindow.dismiss();
                }

            }
        });

        //点击阴影处消失
        mLlAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    popupWindow.dismiss();

                }
            }
        });

        popupWindow.showAsDropDown(view);
    }

    /**
     * 直播
     *
     * @param mActivity
     * @param view
     * @param onlineUser
     * @param listener
     */
    public void showLiveClose(final Activity mActivity, View view, String onlineUser, final OnLiveListener listener) {

        View popwindow_view = mActivity.getLayoutInflater().inflate(R.layout.popwindow_live_close, null, false);
        // 创建PopupWindow实例
        final FixedPopupWindow popupWindow = new FixedPopupWindow(popwindow_view, ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, true);
        popupWindow.setOutsideTouchable(true);
        popupWindow.setFocusable(true);
        // 让pop可以点击外面消失掉
        popupWindow.setBackgroundDrawable(new ColorDrawable(0));
        // 设置动画效果
//        popupWindow.setAnimationStyle(R.style.PopAnimationFade_up_bottom);
        Window window = mActivity.getWindow();
        WindowManager.LayoutParams params = window.getAttributes();
        params.alpha = 1f;
        mActivity.getWindow().setAttributes(params);

        // 点击其它地方关闭对话框
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {

            @Override
            public void onDismiss() {
                WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                params.alpha = 1f;
                mActivity.getWindow().setAttributes(params);
                popupWindow.dismiss();
            }
        });

        ImageView iv_close = popwindow_view.findViewById(R.id.iv_close);
        ImageView iv_goingon = popwindow_view.findViewById(R.id.iv_goingon);
        TextView tv_online_user = popwindow_view.findViewById(R.id.tv_online_user);
        LinearLayout mLlAlpha = popwindow_view.findViewById(R.id.ll_alpha);
        mLlAlpha.setAlpha((float) 0.5);

        tv_online_user.setText(onlineUser);

        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    popupWindow.dismiss();
                }
                listener.closeLive();
            }
        });

        iv_goingon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    popupWindow.dismiss();
                }
                listener.goingOn();
            }
        });

        //点击阴影处消失
        mLlAlpha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (popupWindow != null && popupWindow.isShowing()) {
                    WindowManager.LayoutParams params = mActivity.getWindow().getAttributes();
                    params.alpha = 1f;
                    mActivity.getWindow().setAttributes(params);
                    popupWindow.dismiss();

                }
            }
        });

        popupWindow.showAsDropDown(view);
    }

}
