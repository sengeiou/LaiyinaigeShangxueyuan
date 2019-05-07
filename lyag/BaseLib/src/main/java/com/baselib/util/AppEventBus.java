package com.baselib.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * 观察者模式
 */
public class AppEventBus {
	private static final String TAG = AppEventBus.class.getSimpleName();
	private static AppEventBus instance;
	public static boolean DEBUG = false;

	public static synchronized AppEventBus getDefault() {
		if (null == instance) {
			instance = new AppEventBus();
		}
		return instance;
	}

	private AppEventBus() {
	}

	private HashMap<String, List<Subject>> subjectMapper = new HashMap<String, List<Subject>>();

	public Subject register(String tag, Subject subject) {
		List<Subject> subjectList = subjectMapper.get(tag);
		if (null == subjectList) {
			subjectList = new ArrayList<Subject>();
			subjectMapper.put(tag, subjectList);
		}
		subjectList.add(subject);
		LogUtil.d(TAG, tag);
		return subject;
	}

	public void unregister(String tag, Subject subject) {
		List<Subject> subjects = subjectMapper.get(tag);
		if (null != subjects) {
			subjects.remove(subject);
			if (subjects.size() <= 0) {
				subjectMapper.remove(tag);
			}
		}
		LogUtil.d(TAG, tag);
	}

	public void post(String tag, Object... content) {
		List<Subject> subjectList = subjectMapper.get(tag);
		if (null != subjectList && subjectList.size() > 0) {
			for (Subject subject : subjectList) {
				subject.onPostAccept(tag, content);
			}
		}
	}

	// 回调接口
	public interface Subject {
		void onPostAccept(String tag, Object... content);
	}

}
