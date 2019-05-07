package com.jianzhong.lyag.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.baselib.util.StringUtils;
import com.jianzhong.lyag.CommonWebActivity;
import com.jianzhong.lyag.R;
import com.jianzhong.lyag.global.AppConstants;
import com.jianzhong.lyag.model.BannerDisplayModel;
import com.jianzhong.lyag.ui.exam.ClassDetailActivity;
import com.jianzhong.lyag.ui.exam.ColumnDetailActivity;
import com.jianzhong.lyag.ui.exam.DryCargoDetailActivity;
import com.jianzhong.lyag.ui.live.LiveDetailActivity;
import com.jianzhong.lyag.util.CommonUtils;
import com.jianzhong.lyag.util.GlideUtils;

import java.util.List;

/**
 * Created by zhengwencheng on 2016/9/12.
 * E-mail 15889964542@163.com
 *
 * 学吧直播首页的推荐ViewPager
 */

public class BannerPageAdapter extends PagerAdapter {
    private List<BannerDisplayModel> images;
    private Context mContext;
    private LayoutInflater mLInflater;
    public BannerPageAdapter(List<BannerDisplayModel>  images, Context context) {
        this.images = images;
        this.mContext = context;
        mLInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return images.size();
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        View mView = mLInflater.inflate(R.layout.item_pager_img,null);
        ImageView imageView = (ImageView) mView.findViewById(R.id.iv_banner);
        ((LinearLayout)imageView.getParent()).removeView(imageView);
        imageView.setLayoutParams(CommonUtils.getImageLayoutParamsForViewPager(mContext, imageView,750,300,1));
        GlideUtils.load(imageView,images.get(position).getCover());

        container.addView(imageView);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent;
                if(!StringUtils.isEmpty(images.get(position).getLink())){
                    intent = new Intent(mContext, CommonWebActivity.class);
                    intent.putExtra("title",images.get(position).getTitle());
                    intent.putExtra("url", images.get(position).getLink());
                    mContext.startActivity(intent);
                }else {
                    if(images.get(position).getBanner_type().equals(AppConstants.TAG_AUDIO)){
                        intent = new Intent(mContext, DryCargoDetailActivity.class);
                        intent.putExtra("audio_id",images.get(position).getForeign_id());
                        mContext.startActivity(intent);
                    }else if(images.get(position).getBanner_type().equals(AppConstants.TAG_COLUMN)){
                        intent = new Intent(mContext, ColumnDetailActivity.class);
                        intent.putExtra("column_id",images.get(position).getForeign_id());
                        mContext.startActivity(intent);
                    }else if(images.get(position).getBanner_type().equals(AppConstants.TAG_COURSE)){
                        intent = new Intent(mContext, ClassDetailActivity.class);
                        intent.putExtra("course_id",images.get(position).getForeign_id());
                        mContext.startActivity(intent);
                    }else if(images.get(position).getBanner_type().equals(AppConstants.TAG_LIVE)){
                        intent = new Intent(mContext, LiveDetailActivity.class);
                        intent.putExtra("live_id",images.get(position).getForeign_id());
                        mContext.startActivity(intent);
                    }
                }
            }
        });
        return imageView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        ((ViewPager)container).removeView((View) object);
    }
}
