package com.jianzhong.lyag.util;

import android.content.Context;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

public class DimensonalTools {
	/** dip to px */
	public static int dip2px(Context context, float dipValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dipValue * scale + 0.5f);
	}

	/** px to dip */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}
	/**
	 * 根据item数设置gridView的高度
	 * @param gridView
	 */
	public static void setGridViewHeight(GridView gridView){
		ListAdapter listAdapter = gridView.getAdapter();
		int numColumns = gridView.getNumColumns();
		if (listAdapter == null) return;
		int totalHeight = 0;
				
		for (int i = 0; i < listAdapter.getCount(); i=i+numColumns) {
			View itemView = listAdapter.getView(i, null, gridView);
			//计算子View的高度
			((LinearLayout)itemView).getChildAt(0).measure(0, MeasureSpec.makeMeasureSpec(-1, MeasureSpec.EXACTLY));
			System.out.println(">>>>>>>>>>>>>>>>>ImageView "+((LinearLayout)itemView).getMeasuredHeightAndState());
			
			totalHeight += itemView.getMeasuredHeight();
		}
		System.out.println(">>>>>>>>>>>>>>>>>totalHeight "+totalHeight);
		ViewGroup.LayoutParams layoutParams = gridView.getLayoutParams();
		layoutParams.height = totalHeight + (gridView.getVerticalSpacing()*gridView.getCount()/numColumns);
		layoutParams.height = 127;
		gridView.setLayoutParams(layoutParams);
	}
	
}
