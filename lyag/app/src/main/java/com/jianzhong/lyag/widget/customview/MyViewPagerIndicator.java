package com.jianzhong.lyag.widget.customview;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.jianzhong.lyag.R;
import com.jianzhong.lyag.listener.OnItemsSelectInterface;
import com.jianzhong.lyag.util.DimensonalTools;

/**
 * viewPager下的指示器封装
 * @author Alpha_Huang
 *
 */
public class MyViewPagerIndicator extends LinearLayout{
	ViewPager viewPager;
	/**上下文*/
	Context context;
	/**存放指示器图片View*/
	ImageView[] imageViews;
	/**圆点指示器的默认宽度大小*/
	public static final int indicatorWidth = 8;
	private static long default_auto_scroll_time = 8000l;
//	private OnItemsSelectInterface onItemsSelectInterface;
	public MyViewPagerIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
//		this.onItemsSelectInterface = onItemsSelectInterface;
	}
	
	/**
	 * 设置ViewPager
	 * @param viewPager
	 */
	public void setViewPager(ViewPager viewPager, OnItemsSelectInterface onItemsSelectInterface){
		this.viewPager = viewPager;
		init(onItemsSelectInterface);
//		invalidate();
	}
	
	/**
	 * 设置指示器的样式
	 * @param drawableResId
	 */
	public void setIndicatorStyle(int drawableResId){
		for (int i = 0; i < imageViews.length; i++) {
			imageViews[i].setImageResource(drawableResId);
		}
	}
	
	/**
	 * 初始化
	 */
	private void init(final OnItemsSelectInterface onItemsSelectInterface) {
		this.removeAllViews();
		//根据Adapter数据源的个数来初始化指示器的个数
		int radioCount = this.viewPager.getAdapter().getCount();
		 imageViews = new ImageView[radioCount];
		 
		 for (int i = 0; i < imageViews.length; i++) {
			imageViews[i] = new ImageView(getContext());
			//设置布局参数
			LayoutParams layoutParams = new LayoutParams(DimensonalTools.dip2px(getContext(), indicatorWidth), DimensonalTools.dip2px(getContext(), indicatorWidth));
			if (i!=imageViews.length) {
				layoutParams.rightMargin = DimensonalTools.dip2px(getContext(), 5);
			}
			imageViews[i].setLayoutParams(layoutParams);
			//设置默认的指示器样式
			imageViews[i].setImageResource(R.drawable.viewpager_indicator_selector);
			this.addView(imageViews[i]);
		}
		 this.displayRatio(0);
		 //原本为addOnPageChangeListener 先版本根据提示改成setOn……
		 this.viewPager.setOnPageChangeListener(new OnPageChangeListener() {
				
				@Override
				public void onPageSelected(int arg0) {
					displayRatio(arg0);
					if(onItemsSelectInterface != null){
						onItemsSelectInterface.OnClick(arg0);
					}
				}
				
				@Override
				public void onPageScrolled(int arg0, float arg1, int arg2) {
					
				}
				
				@Override
				public void onPageScrollStateChanged(int arg0) {
//					LogUtils.d(arg0+"");
				}
			}); 
	}
	private int currentItem = 0;
	/**
	 *	开启自动轮播，时间为5秒
	 * @param anable
	 */
	public void autoScrollEnable(boolean anable){
		if (anable) {
			postDelayed(autoScrollCallback, default_auto_scroll_time);
		}else{
			removeCallbacks(autoScrollCallback);
		}
	}
	public void setAutoScrollTime(long time){
		default_auto_scroll_time = time;
	}
	private Runnable autoScrollCallback = new Runnable() {
		
		@Override
		public void run() {
			//获取当前viewpager显示的item Index
			currentItem = viewPager.getCurrentItem();
			//如果当前条目在总数之类
			if (currentItem < getIndicatorCount()-1) {
				//缓慢滑动到下一页
				viewPager.setCurrentItem(currentItem+1, true);
			}else{
				viewPager.setCurrentItem(0, false);//如果已经显示最后一页，就跳回第一页
			}
			postDelayed(autoScrollCallback, 5000);//不断循环
		}
	};
	/** 显示图片下面的圆点(选中) */
	public void displayRatio(int id) {

		for (int i = 0; i < imageViews.length; i++) {
			if (i == id) {
				imageViews[i].setSelected(true);
			} else {
				imageViews[i].setSelected(false);

			}
		}
	}
	/**
	 * 返回指示器的个数
	 * @return 指示器点的个数
	 */
	public int getIndicatorCount(){
		return imageViews.length;
	}
	
}
