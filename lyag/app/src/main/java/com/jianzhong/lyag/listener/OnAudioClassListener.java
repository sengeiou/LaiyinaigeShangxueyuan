package com.jianzhong.lyag.listener;

import com.jianzhong.lyag.model.SectionModel;

/**
 * 课程分段音频播放
 * Created by zhengwencheng on 2018/3/28 0028.
 * package com.jianzhong.bs.listener
 */

public interface OnAudioClassListener {

    void callBack(int position, SectionModel item);

}
