package com.jianzhong.lyag.ui.interact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.callback.OSSProgressCallback;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.baselib.util.GsonUtils;
import com.baselib.util.ListUtils;
import com.baselib.util.LogUtil;
import com.baselib.util.Result;
import com.baselib.util.ResultList;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.ImagePickerAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.flexboxlayout.TagAdapter;
import com.jianzhong.lyag.flexboxlayout.TagFlowLayout;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.ImgCompressModel;
import com.jianzhong.lyag.model.TagModel;
import com.jianzhong.lyag.ui.organization.MemberSelectActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.DialogHelper;
import com.jianzhong.lyag.util.MD5Utils;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.lzy.imagepicker.ui.ImagePreviewDelActivity;

import net.bither.util.imagecompressor.XCImageCompressor;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 发布求助页面
 * create by zhengwencheng on 2018/3/17 0017
 * package com.jianzhong.bs.ui.interact
 */
public class AddHelpActivity extends ToolbarActivity {

    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;
    @BindView(R.id.et_title)
    EditText mEtTitle;
    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.tv_length)
    TextView mTvLength;
    @BindView(R.id.tv_range)
    TextView mTvRange;
    @BindView(R.id.tag_FlowLayout)
    TagFlowLayout mTagFlowLayout;

    private ArrayList<ImageItem> addHelpImageList; //
    private ImagePickerAdapter mAddHelpAdapter; //
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private int maxImgCount = 9;

    private List<TagModel> mTagModels = new ArrayList<>();
    private List tagArr = new ArrayList();

    private List<ImgCompressModel> mHelpImgFile = new ArrayList<>();
    private List<String> mHlepImgList = new ArrayList<>();

    private Set<String> mMemberSet = new HashSet<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_interact_help_add);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        setHeadTitle("发布求助");
        showHeadTitle();
        setHeadRight("发布");
        showHeadRight();

        getHelpTag();
    }

    /**
     * 获取求助标签
     */
    private void getHelpTag() {
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_HELP_TAG, null, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                ResultList<TagModel> resultList = GsonUtils.json2List(s, TagModel.class);
                if (resultList != null && resultList.getCode() == HttpConfig.STATUS_SUCCESS) {
                    mTagModels.addAll(resultList.getData());

                    initWidget();
                }
            }

            @Override
            public void onFailure(String msg) {

            }
        });
    }

    /**
     * 图片选择的初始化
     */
    private void initWidget() {
        //求助图片
        addHelpImageList = new ArrayList<>();
        mAddHelpAdapter = new ImagePickerAdapter(this, addHelpImageList, maxImgCount);
        mAddHelpAdapter.setOnItemClickListener(new ImagePickerAdapter.OnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                switch (position) {
                    case IMAGE_ITEM_ADD:
                        //打开选择,本次允许选择的数量
                        ImagePicker.getInstance().setSelectLimit(maxImgCount - addHelpImageList.size());
                        Intent intent = new Intent(AddHelpActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    default:
                        //打开预览
                        Intent intentPreview = new Intent(AddHelpActivity.this, ImagePreviewDelActivity.class);
                        intentPreview.putExtra(ImagePicker.EXTRA_IMAGE_ITEMS, (ArrayList<ImageItem>) mAddHelpAdapter.getImages());
                        intentPreview.putExtra(ImagePicker.EXTRA_SELECTED_IMAGE_POSITION, position);
                        intentPreview.putExtra(ImagePicker.EXTRA_FROM_ITEMS, true);
                        startActivityForResult(intentPreview, REQUEST_CODE_PREVIEW);
                        break;
                }
            }
        });
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 3));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setAdapter(mAddHelpAdapter);


        mTagFlowLayout.setAdapter(new TagAdapter<TagModel>(mTagModels) {
            @Override
            protected View getView(ViewGroup parent, int position, final TagModel item) {
                final TextView tv = (TextView) mInflater.inflate(R.layout.item_notice_flow_layout, parent, false);
                tv.setText(item.getTag_name());
                tv.setOnClickListener(new View.OnClickListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onClick(View view) {
                        if (item.getIsSelected() == 0) {
                            if (tagArr.size() < 3) {
                                item.setIsSelected(1);
                                tv.setBackground(getDrawable(R.drawable.shape_item_theme_white_16));
                                tv.setTextColor(getResources().getColor(R.color.color_theme));
                            } else {
                                ToastUtils.show(mContext, "最多可选3个标签");
                            }

                        } else {
                            item.setIsSelected(0);
                            tv.setBackground(getDrawable(R.drawable.shape_item_gray_white_16));
                            tv.setTextColor(getResources().getColor(R.color.color_888888));
                        }

                        tagArr.clear();
                        for (int i = 0; i < mTagModels.size(); i++) {
                            if (mTagModels.get(i).getIsSelected() == 1) {
                                tagArr.add(mTagModels.get(i).getTag_id());
                            }
                        }
                    }
                });
                return tv;
            }
        });


        mEtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @SuppressLint("SetTextI18n")
            @Override
            public void afterTextChanged(Editable editable) {
                mTvLength.setText(editable.toString().length()+"/1000");
            }
        });
    }

    @OnClick({R.id.head_right,R.id.ll_range})
    public void onClick(View view) {
        Intent intent;
        switch (view.getId()) {
            case R.id.head_right:
                if (StringUtils.isEmpty(mEtTitle.getText().toString())) {
                    ToastUtils.show(mContext, "请输入求助标题");
                }else if(StringUtils.isEmpty(mEtContent.getText().toString())){
                    ToastUtils.show(mContext, "请输入求助内容");
                }else if(ListUtils.isEmpty(tagArr)){
                    ToastUtils.show(mContext, "请选择求助标签");
                }else {
                    DialogHelper.showDialog(mContext);
                    if(!ListUtils.isEmpty(addHelpImageList)){
                        compressHelpImg();
                    }else {
                        AddHelp();
                    }
                }
                break;
            case R.id.ll_range:
                intent = new Intent(mContext, MemberSelectActivity.class);
                intent.putExtra("type",1);
                startActivity(intent);
                break;
        }
    }


    /**
     * 压缩量房草图
     */
    private void compressHelpImg() {
        if (!ListUtils.isEmpty(addHelpImageList)) {
            CommonUtils.compress(addHelpImageList, new XCImageCompressor.ImageCompressListener() {
                @Override
                public void onSuccess(List<String> outFilePathList) {
                    LogUtil.d("tag", outFilePathList.toString());
                    mHelpImgFile.clear();
                    for (int i = 0; i < outFilePathList.size(); i++) {
//                        File f = new File(outFilePathList.get(i));
                        String md5String = MD5Utils.encode(System.currentTimeMillis() + Math.random()+"");
                        ImgCompressModel item = new ImgCompressModel();
                        item.setImgPath(outFilePathList.get(i));
                        item.setMd5Sting(md5String);
                        mHelpImgFile.add(item);
                    }

                    getImgUpload(mHelpImgFile);
                }

                @Override
                public void onFailure(String message) {
                    LogUtil.d("tag", message);
                }
            });
        }

    }

    /**
     * 先上传完图片 再添加
     * @param mList
     */
    private void getImgUpload(List<ImgCompressModel> mList) {
        for (int i = 0; i < mList.size(); i++) {
            uploadPicToOSS(mList.get(i).getMd5Sting()+".jpg",mList.get(i).getImgPath());
        }
        while (mHlepImgList.size() < mList.size()){
            return;
        }

        AddHelp();
    }

    /**
     * 提交求助发布
     */
    private void AddHelp(){
        Map<String,Object> params = new HashMap<>();
        params.put("title",mEtTitle.getText().toString());
        params.put("content",mEtContent.getText().toString());
        params.put("images",GsonUtils.toJson(mHlepImgList));
        params.put("tag",GsonUtils.toJson(tagArr));
        params.put("invite-range",GsonUtils.toJson(mMemberSet));
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_HELP_ADD, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                Result result = GsonUtils.json2Bean(s,Result.class);
                if(result != null && result.getCode() == HttpConfig.STATUS_SUCCESS){
                    DialogHelper.dismissDialog();
                    ToastUtils.show(mContext,result.getMessage());
                    AddHelpActivity.this.finish();
                }
            }

            @Override
            public void onFailure(String msg) {
                ToastUtils.show(mContext,msg);
            }
        });
    }

    /**
     * 上传图片到oss
     * @param imgaeName
     * @param imagePath
     */
    private void uploadPicToOSS(String imgaeName,String imagePath){ //
        final String cacheName = AppConstants.HELP_OBJECT_KEY  + CommonUtils.getDate() + "/" + imgaeName;
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
                mHlepImgList.add(HttpConfig.PIC_URL_BASE_GET + cacheName);
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


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
            //添加图片返回
            if (data != null && requestCode == REQUEST_CODE_SELECT) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                addHelpImageList.addAll(images);
                mAddHelpAdapter.setImages(addHelpImageList);
            }
        } else if (resultCode == ImagePicker.RESULT_CODE_BACK) {
            //预览图片返回
            if (data != null && requestCode == REQUEST_CODE_PREVIEW) {
                ArrayList<ImageItem> images = (ArrayList<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_IMAGE_ITEMS);
                addHelpImageList.clear();
                addHelpImageList.addAll(images);
                mAddHelpAdapter.setImages(addHelpImageList);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (ListUtils.isEmpty(GroupVarManager.getInstance().mDepartmentModels) && ListUtils.isEmpty(GroupVarManager.getInstance().mAnecyModels) && ListUtils.isEmpty(GroupVarManager.getInstance().mMemberModels)) {
            mTvRange.setText("选择");
        } else {

            if(!ListUtils.isEmpty(GroupVarManager.getInstance().mDepartmentModels)){
                for (int i = 0; i < GroupVarManager.getInstance().mDepartmentModels.size(); i++) {
                    if(ListUtils.isEmpty(GroupVarManager.getInstance().mDepartmentModels.get(i).getMember())){}
                    for (int j = 0; j < GroupVarManager.getInstance().mDepartmentModels.get(i).getMember().size(); j++) {
                        mMemberSet.add(GroupVarManager.getInstance().mDepartmentModels.get(i).getMember().get(j).getUser_id());
                    }
                }
            }

            if(!ListUtils.isEmpty(GroupVarManager.getInstance().mAnecyModels)){
                for (int i = 0; i < GroupVarManager.getInstance().mAnecyModels.size(); i++) {
                    if(ListUtils.isEmpty(GroupVarManager.getInstance().mAnecyModels.get(i).getMember())){}
                    for (int j = 0; j < GroupVarManager.getInstance().mAnecyModels.get(i).getMember().size(); j++) {
                        mMemberSet.add(GroupVarManager.getInstance().mAnecyModels.get(i).getMember().get(j).getUser_id());
                    }
                }
            }
            if(!ListUtils.isEmpty(GroupVarManager.getInstance().mMemberModels)){
                for (int i = 0; i < GroupVarManager.getInstance().mMemberModels.size(); i++) {
                    mMemberSet.add(GroupVarManager.getInstance().mMemberModels.get(i).getUser_id());
                }
            }

            mTvRange.setText("已选择"+mMemberSet.size()+"个成员");
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //进来先清除选一下
        GroupVarManager.getInstance().mDepartmentModels.clear();
        GroupVarManager.getInstance().mAnecyModels.clear();
        GroupVarManager.getInstance().mMemberModels.clear();
        GroupVarManager.getInstance().mPostModels.clear();
    }
}
