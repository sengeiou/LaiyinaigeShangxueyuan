package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.View;

import com.baselib.util.ListUtils;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.model.AttachmentModel;
import com.jianzhong.lyag.ui.common.PreviewImageActivity;
import com.jianzhong.lyag.ui.common.TbsReaderViewActivity;
import com.zhy.adapter.recyclerview.CommonAdapter;
import com.zhy.adapter.recyclerview.base.ViewHolder;

import java.util.List;

/**
 * Created by chenzhikai on 2018/04/23.
 * 附件适配器
 */

public class AttachmentAdapter extends CommonAdapter<AttachmentModel> {

    public AttachmentAdapter(Context context, List<AttachmentModel> datas) {
        super(context, R.layout.item_attachment, datas);
    }

    @Override
    protected void convert(ViewHolder viewHolder, final AttachmentModel item, int position) {
        if (null != mDatas && !ListUtils.isEmpty(mDatas)) {
            viewHolder.setText(R.id.tv_title, item.getTitle());
            viewHolder.setText(R.id.tv_file_size, item.getFile_size());
            // 设置文件类型图片
            if (item.getExt().equals("jpg") || item.getExt().equals("png") || item.getExt().equals("jpeg")) {
                viewHolder.setImageResource(R.id.iv_file_type, R.drawable.icon_jpg_120x120);
            } else if (item.getExt().equals("xls") || item.getExt().equals("xlsx")) {
                viewHolder.setImageResource(R.id.iv_file_type, R.drawable.icon_excel_120x120);
            } else if (item.getExt().equals("doc") || item.getExt().equals("docx")) {
                viewHolder.setImageResource(R.id.iv_file_type, R.drawable.icon_word_120x120);
            } else if (item.getExt().equals("ppt") || item.getExt().equals("pptx")) {
                viewHolder.setImageResource(R.id.iv_file_type, R.drawable.icon_ppt_120x120);
            } else if (item.getExt().equals("pdf")) {
                viewHolder.setImageResource(R.id.iv_file_type, R.drawable.icon_pdf_120x120);
            } else if (item.getExt().equals("txt")) {
                viewHolder.setImageResource(R.id.iv_file_type, R.drawable.icon_txt_120x120);
            } else {
                viewHolder.setImageResource(R.id.iv_file_type, R.drawable.icon_ppt_120x120);
            }

            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (item.getExt().equals("pdf")
                            || item.getExt().equals("ppt")
                            || item.getExt().equals("xls")
                            || item.getExt().equals("doc")
                            || item.getExt().equals("docx")
                            || item.getExt().equals("xlsx")
                            || item.getExt().equals("pptx")
                            || item.getExt().equals("txt")) {
                        Intent intent = new Intent(mContext, TbsReaderViewActivity.class);
                        intent.putExtra(TbsReaderViewActivity.KEY_URL, item.getPath());
                        intent.putExtra(TbsReaderViewActivity.KEY_TITLE, item.getTitle());
                        mContext.startActivity(intent);
                    } else if (item.getExt().equals("jpg") || item.getExt().equals("png") || item.getExt().equals("jpeg")) {
                        Intent intent = new Intent(mContext, PreviewImageActivity.class);
                        intent.putExtra(PreviewImageActivity.KEY_URL, item.getPath());
                        intent.putExtra(PreviewImageActivity.KEY_TITLE, item.getTitle());
                        mContext.startActivity(intent);
                    } else {
                        Intent intent = new Intent(mContext, TbsReaderViewActivity.class);
                        intent.putExtra(TbsReaderViewActivity.KEY_URL, item.getPath());
                        intent.putExtra(TbsReaderViewActivity.KEY_TITLE, item.getTitle());
                        mContext.startActivity(intent);
                    }
                }
            });
        }
    }
}
