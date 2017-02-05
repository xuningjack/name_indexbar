package com.example.utils;

import java.util.Comparator;
import com.example.voo.SortModel;

/**
 * 用户名称的比较器
 * @author Jack
 * @version 创建时间：2014-2-6  下午3:51:57
 */
public class PinyinComparator implements Comparator<SortModel> {

	public int compare(SortModel o1, SortModel o2) {
		
		if (o1.getSortLetters().equals("@") || o2.getSortLetters().equals("#")) {
			return -1;
		} else if (o1.getSortLetters().equals("#") || o2.getSortLetters().equals("@")) {
			return 1;
		} else {
			return o1.getSortLetters().compareTo(o2.getSortLetters());
		}
	}
}
