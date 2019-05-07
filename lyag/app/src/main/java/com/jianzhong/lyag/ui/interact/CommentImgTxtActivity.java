package com.jianzhong.lyag.ui.interact;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
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
import com.baselib.util.InputMethodUtil;
import com.baselib.util.ListUtils;
import com.baselib.util.LogUtil;
import com.baselib.util.Result;
import com.baselib.util.StringUtils;
import com.baselib.util.ToastUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.adapter.ImagePickerAdapter;
import com.jianzhong.lyag.base.ToolbarActivity;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.global.AppUserModel;
import com.jianzhong.lyag.global.GroupVarManager;
import com.jianzhong.lyag.http.HttpCallBack;
import com.jianzhong.lyag.http.HttpConfig;
import com.jianzhong.lyag.http.HttpRequest;
import com.jianzhong.lyag.model.ImgCompressModel;
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
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 评论中的文字和图片
 * Created by zhengwencheng on 2018/4/23 0023.
 * package com.jianzhong.lyag.ui.interact
 */
public class CommentImgTxtActivity extends ToolbarActivity {

    @BindView(R.id.et_content)
    EditText mEtContent;
    @BindView(R.id.tv_length)
    TextView mTvLength;
    @BindView(R.id.recycler_view)
    RecyclerView mRecyclerView;

    private ArrayList<ImageItem> addHelpImageList; //
    private ImagePickerAdapter mAddHelpAdapter; //
    public static final int IMAGE_ITEM_ADD = -1;
    public static final int REQUEST_CODE_SELECT = 100;
    public static final int REQUEST_CODE_PREVIEW = 101;
    private int maxImgCount = 3;

    private List<ImgCompressModel> mHelpImgFile = new ArrayList<>();
    private List<String> mCommentImgUrlList = new ArrayList<>();
    private String foreign_id;
    private String top_id = "0"; //评论的时候使用 默认为0 回复别人时不为0
    private String asset_type;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_comment_img_txt);
        ButterKnife.bind(this);
    }

    @Override
    public void initData() {
        super.initData();

        foreign_id = getIntent().getStringExtra("foreign_id");
        asset_type = getIntent().getStringExtra("asset_type");

        setHeadTitle("评论");
        showHeadTitle();
        setHeadRight("发表");
        showHeadRight();

        initController();
    }

    private void initController() {
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
                        Intent intent = new Intent(CommentImgTxtActivity.this, ImageGridActivity.class);
                        startActivityForResult(intent, REQUEST_CODE_SELECT);
                        break;
                    default:
                        //打开预览
                        Intent intentPreview = new Intent(CommentImgTxtActivity.this, ImagePreviewDelActivity.class);
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
                mTvLength.setText(editable.toString().length() + "/1000");
            }
        });
    }

    @OnClick(R.id.head_right)
    public void onViewClicked() {
        if(StringUtils.isEmpty(mEtContent.getText().toString())){
            ToastUtils.show(mContext, "请输入求助内容");
        }else if(ListUtils.isEmpty(addHelpImageList)){
            ToastUtils.show(mContext, "请选择要评论的图片");
        }else {
            DialogHelper.showDialog(mContext);
            compressHelpImg();
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
        while (mCommentImgUrlList.size() < mList.size()){
            return;
        }

        AddComment();
    }

    /**
     * 提交求助发布
     */
    private void AddComment(){
        Map<String, Object> params = new HashMap<>();
        params.put("foreign_id", foreign_id);
        params.put("content", mEtContent.getText().toString());
        params.put("top_id", top_id);
        params.put("asset_user_id", AppUserModel.getInstance().getmUserModel().getUser_id());
        params.put("asset_type", asset_type);
        params.put("audio_url", "");
        params.put("audio_duration_sec", "0");
        params.put("images", GsonUtils.toJson(mCommentImgUrlList));
        HttpRequest.getInstance().post(HttpConfig.URL_BASE + HttpConfig.URL_COMMENT_ADD, params, new HttpCallBack() {
            @Override
            public void onSuccess(String s) {
                DialogHelper.dismissDialog();
                Result result = GsonUtils.json2Bean(s, Result.class);
                if (result.getCode() == HttpConfig.STATUS_SUCCESS) {
                    InputMethodUtil.hiddenInputMethod(mContext, mEtContent);
                    ToastUtils.show(mContext, "评论成功");
                    GroupVarManager.getInstance().isUpdateComment = 1;
                    CommentImgTxtActivity.this.finish();
                } else {
                    ToastUtils.show(mContext, result.getMessage());
                }
            }

            @Override
            public void onFailure(String msg) {
                DialogHelper.dismissDialog();
                ToastUtils.show(mContext, msg);
            }
        });
    }


    /**
     * 上传图片到oss
     * @param imgaeName
     * @param imagePath
     */
    private void uploadPicToOSS(String imgaeName,String imagePath){ //
        final String cacheName = AppConstants.COMMENT_IMAGE_KEY  + CommonUtils.getDate() + "/" + imgaeName;
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
                mCommentImgUrlList.add(HttpConfig.PIC_URL_BASE_GET + cacheName);
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
}
