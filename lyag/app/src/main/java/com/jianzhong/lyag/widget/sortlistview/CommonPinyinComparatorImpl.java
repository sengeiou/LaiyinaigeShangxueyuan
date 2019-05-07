package com.jianzhong.lyag.widget.sortlistview;

import java.util.Comparator;

/**
 * 
 * @author xiaanming
 *
 */
public class CommonPinyinComparatorImpl implements Comparator<CommonPinyinComparator> {

	@Override
	public int compare(CommonPinyinComparator o1, CommonPinyinComparator o2) {
		if (o1.getSortLetters().equals("@")
				|| o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#")
				|| o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}

}
