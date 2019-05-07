package com.jianzhong.lyag.ui.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.TextView;

import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.baselib.util.DateUtils;
import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.LogUtil;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.bigkoo.pickerview.OptionsPickerView;
import com.google.gson.Gson;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.JsonBean;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.DialogHelper;
import com.jianzhong.lyag.util.GetJsonDataUtil;
import com.jianzhong.lyag.util.GlideUtils;
import com.jianzhong.lyag.util.MD5Utils;
import com.jianzhong.lyag.util.PopWindowUtil;
import com.jzxiang.pickerview.TimePickerDialog;
import com.jzxiang.pickerview.listener.OnDateSetListener;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;

import net.bither.util.imagecompressor.XCImageCompressor;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * 编辑个人资料
 * Created by zhengwencheng on 2018/1/22 0022.
 * package com.jianzhong.bs.ui.user
 */

public class EditPersonInfoActivity extends ToolbarActivity {

    @BindView(R.id.iv_avatar)
    CircleImageView mIvAvatar;
    @BindView(R.id.tv_name)
    TextView mTvName;
    @BindView(R.id.tv_job)
    TextView mTvJob;
    @BindView(R.id.tv_sex)
    TextView mTvSex;
    @BindView(R.id.tv_birthday)
    TextView mTvBirthday;
    @BindView(R.id.tv_area)
    TextView mTvArea;

    private ArrayList<JsonBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;
    private Thread thread;
    private static final int REQUEST_CODE_SELECT = 3014;
    private String sex;
    private String sexValue;
    private String address;
    private String birth;
    private String avatar_url;

    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    isLoaded = true;
                    break;
                case MSG_LOAD_FAILED:
                    ToastUtils.show(mContext, "地区数据解析失败");
                    break;

            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_personal_info_edit);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        mHandler.sendEmptyMessage(MSG_LOAD_DATA);

        setHeadTitle("编辑资料");
        showHeadTitle();

        initController();
    }

    private void initController(){
        GlideUtils.loadAvatarImage(mIvAvatar, AppUserModel.getInstance().getmUserModel().getAvatar());
        mTvName.setText(AppUserModel.getInstance().getmUserModel().getRealname());
        mTvJob.setText(AppUserModel.getInstance().getmUserModel().getPos_name());
        if(AppUserModel.getInstance().getmUserModel().getSex().equals("1")){
            mTvSex.setText("男");
        }else if(AppUserModel.getInstance().getmUserModel().getSex().equals("2")){
            mTvSex.setText("女");
        }
        if(!StringUtils.isEmpty(AppUserModel.getInstance().getmUserModel().getBirth())){
            mTvBirthday.setText(AppUserModel.getInstance().getmUserModel().getBirth());
            mTvBirthday.setTextColor(mContext.getResources().getColor(R.color.color_666666));
        }
        if(!StringUtils.isEmpty(AppUserModel.getInstance().getmUserModel().getAddress())){
            mTvArea.setText(AppUserModel.getInstance().getmUserModel().getAddress());
            mTvArea.setTextColor(mContext.getResources().getColor(R.color.color_666666));
        }
    }

    @OnClick({R.id.ll_personal, R.id.ll_sex, R.id.ll_birthday, R.id.ll_area})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_personal:
                //打开选择,本次允许选择的数量
                ImagePicker.getInstance().setSelectLimit(1);
                Intent intent = new Intent(this, ImageGridActivity.class);
                startActivityForResult(intent, REQUEST_CODE_SELECT);
                break;
            case R.id.ll_sex:
                PopWindowUtil.getInstance().showCommonItemSelect(mActivity, mTvSex, CommonUtils.getSexList(), mTvSex, new PopWindowUtil.OnItemClickListener() {
                    @Override
                    public void OnItemClickListener(String title, int position) {
                        // 判断是否要修改
                        if (AppUserModel.getInstance().getmUserModel().getSex().equals((position + 1) + ""))
                            return;
                        // 修改性别
                        if (title.equals("男")) {
                            sex = "1";
                            sexValue = "男";
                        } else {
                            sex = "2";
                            sexValue = "女";
                        }
                        DialogHelper.showDialog(mContext);
                        updatePersonInfo("sex");
                    }
                });
                break;
            case R.id.ll_birthday:
                CommonUtils.getDate(this, new OnDateSetListener() {
                    @Override
                    public void onDateSet(TimePickerDialog timePickerView, long millseconds) {
                        birth = DateUtils.timeStampToDate(millseconds, "yyyy-MM-dd");
                        DialogHelper.showDialog(mContext);
                        updatePersonInfo("birth");
                    }
                });
                break;
            case R.id.ll_area:
                if (isLoaded) {
                    ShowPickerView();
                } else {
                    return;
                }
                break;
        }
    }

    private void updatePersonInfo(final String tag) {
        Map<String, Object> params = new HashMap<>();
        if (tag.equals("sex")) {
            params.put("sex", sex);
        } else if (tag.equals("avatar")) {
            params.put("avatar", avatar_url);
        } else if (tag.equals("address")) {
            params.put("address", address);
        } else if (tag.equals("birth")) {
            params.put("birth", birth);
        }

        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_USER_UPDATE, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                DialogHelper.dismissDialog();
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result != null && result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    if (tag.equals("sex")) {
                        mTvSex.setText(sexValue);
                        AppUserModel.getInstance().getmUserModel().setSex(sex);
                    } else if (tag.equals("avatar")) {
                        GlideUtils.loadAvatarImage(mIvAvatar,avatar_url);
                        EventBus.getDefault().post("avatar");
                    } else if (tag.equals("address")) {
                        mTvArea.setText(address);
                        AppUserModel.getInstance().getmUserModel().setAddress(address);
                        mTvArea.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                    } else if (tag.equals("birth")) {
                        mTvBirthday.setText(birth);
                        AppUserModel.getInstance().getmUserModel().setBirth(birth);
                        mTvBirthday.setTextColor(mContext.getResources().getColor(R.color.color_666666));
                    }
                } else {
                    if (result != null) {
                        ToastUtils.show(mContext, result.getMessage());
                    }
                }

            }

            @Override
            public void onFailure(String msg) {
                DialogHelper.dismissDialog();
                ToastUtils.show(mContext, msg);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                final ArrayList<ImageItem> selImageList = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                if (!ListUtils.isEmpty(selImageList)) {
                    CommonUtils.compress(selImageList, new XCImageCompressor.ImageCompressListener() {
                        @Override
                        public void onSuccess(List<String> outFilePathList) {
                            LogUtil.d("tag", outFilePathList.toString());
                            for (int i = 0; i < outFilePathList.size(); i++) {
                                String md5String = MD5Utils.encode(System.currentTimeMillis() + Math.random()+"");
                                uploadPicToOSS(md5String +".jpg", outFilePathList.get(i));
                            }
                        }

                        @Override
                        public void onFailure(String message) {
                            LogUtil.d("tag", message);
                        }
                    });
                }
            }
        }
    }

    /**
     * 上传图片到oss
     * @param imgaeName
     * @param imagePath
     */
    private void uploadPicToOSS(String imgaeName,String imagePath){
        DialogHelper.showDialog(mContext);
        final String cacheName = AppConstants.AVATAR_OBJECT_KEY  + CommonUtils.getDate() + "/" +imgaeName;
        PutObjectRequest put = new PutObjectRequest(AppConstants.BUCKET, cacheName, imagePath);
        // 异步上传时可以设置进度回调
        put.setProgressCallback(new OSSProgressCallback<PutObjectRequest>() {
            @Override
            public void onProgress(PutObjectRequest request, long currentSize, long totalSize) {

            }
        });

        OSSAsyncTask task = GroupVarManager.getInstance().oss.asyncPutObject(put, new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
            @Override
            public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                DialogHelper.dismissDialog();
                avatar_url = HttpConfig.PIC_URL_BASE_GET + cacheName;
                AppUserModel.getInstance().getmUserModel().setAvatar(avatar_url);
                updatePersonInfo("avatar");
            }
            @Override
            public void onFailure(PutObjectRequest request, ClientException clientExcepion, ServiceException serviceException) {
                // 请求异常
                if (clientExcepion != null) {
                    // 本地异常如网络异常等
                    clientExcepion.printStackTrace();
                }
                if (serviceException != null) {
                    // 服务异常
                    LogUtil.e("ErrorCode", serviceException.getErrorCode());
                    LogUtil.e("RequestId", serviceException.getRequestId());
                    LogUtil.e("HostId", serviceException.getHostId());
                    LogUtil.e("RawMessage", serviceException.getRawMessage());
                }
            }
        });

        task.waitUntilFinished();
    }

    /**
     * 弹出所在地的选择器
     */
    private void ShowPickerView() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                if (options1Items.get(options1).getPickerViewText().equals(options2Items.get(options1).get(options2))) {
                    address = options1Items.get(options1).getPickerViewText() +
                            options3Items.get(options1).get(options2).get(options3);
                } else {
                    address = options1Items.get(options1).getPickerViewText() +
                            options2Items.get(options1).get(options2) +
                            options3Items.get(options1).get(options2).get(options3);
                }
                DialogHelper.showDialog(mContext);
                updatePersonInfo("address");
            }
        }).setTitleText("选择地区")
                .setDividerColor(mContext.getResources().getColor(R.color.color_grey_divider))
                .setTextColorCenter(mContext.getResources().getColor(R.color.color_333333)) //设置选中项文字颜色
                .setContentTextSize(16).
                        setTitleSize(16).
                        setSubmitColor(getResources().getColor(R.color.color_theme))
                .setCancelColor(getResources().getColor(R.color.color_theme)).build();

        pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();
    }

    private void initJsonData() {//解析数据
        //注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件 关键逻辑在于循环体
        String JsonData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据
        ArrayList<JsonBean> jsonBean = parseData(JsonData);//用Gson 转成实体
        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）
            for (int c = 0; c < jsonBean.get(i).getCityList().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCityList().get(c).getName();
                CityList.add(CityName);//添加城市

                ArrayList<String> City_AreaList = new ArrayList<>();//该城市的所有地区列表

                //如果无地区数据，建议添加空字符串，防止数据为null 导致三个选项长度不匹配造成崩溃
                if (jsonBean.get(i).getCityList().get(c).getArea() == null
                        || jsonBean.get(i).getCityList().get(c).getArea().size() == 0) {
                    City_AreaList.add("");
                } else {

                    for (int d = 0; d < jsonBean.get(i).getCityList().get(c).getArea().size(); d++) {//该城市对应地区所有数据
                        String AreaName = jsonBean.get(i).getCityList().get(c).getArea().get(d);

                        City_AreaList.add(AreaName);//添加该城市所有地区数据
                    }
                }
                Province_AreaList.add(City_AreaList);//添加该省所有地区数据
            }
            //添加城市数据
            options2Items.add(CityList);
            //添加地区数据
            options3Items.add(Province_AreaList);
        }
        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);
    }
    /**
     * 解释本地的数据json
     *
     * @param result
     * @return
     */
    public ArrayList<JsonBean> parseData(String result) {//Gson 解析
        ArrayList<JsonBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                JsonBean entity = gson.fromJson(data.optJSONObject(i).toString(), JsonBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }
}
