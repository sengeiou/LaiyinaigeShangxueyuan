package com.jianzhong.lyag.model;

/**
 * 回放对象
 * Created by zhengwencheng on 2018/4/13 0013.
 * package com.jianzhong.bs.model
 */

public class RecordUrlModel extends BaseModel {

    private String section_id;
    private String live_id;
    private String video_url;
    private String video_cdn_url;
    private String is_edit;
    private String create_at;
    private String update_at;
    private String is_del;
    private String title;
    private int is_section_finish;
    private int isSelected;         //是否选中
    private int isPlay;

    public String getSection_id() {
        return section_id;
    }

    public void setSection_id(String section_id) {
        this.section_id = section_id;
    }

    public String getLive_id() {
        return live_id;
    }

    public void setLive_id(String live_id) {
        this.live_id = live_id;
    }

    public String getVideo_url() {
        return video_url;
    }

    public void setVideo_url(String video_url) {
        this.video_url = video_url;
    }

    public String getVideo_cdn_url() {
        return video_cdn_url;
    }

    public void setVideo_cdn_url(String video_cdn_url) {
        this.video_cdn_url = video_cdn_url;
    }

    public String getIs_edit() {
        return is_edit;
    }

    public void setIs_edit(String is_edit) {
        this.is_edit = is_edit;
    }

    public String getCreate_at() {
        return create_at;
    }

    public void setCreate_at(String create_at) {
        this.create_at = create_at;
    }

    public String getUpdate_at() {
        return update_at;
    }

    public void setUpdate_at(String update_at) {
        this.update_at = update_at;
    }

    public String getIs_del() {
        return is_del;
    }

    public void setIs_del(String is_del) {
        this.is_del = is_del;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getIs_section_finish() {
        return is_section_finish;
    }

    public void setIs_section_finish(int is_section_finish) {
        this.is_section_finish = is_section_finish;
    }

    public int getIsSelected() {
        return isSelected;
    }

    public void setIsSelected(int isSelected) {
        this.isSelected = isSelected;
    }

    public int getIsPlay() {
        return isPlay;
    }

    public void setIsPlay(int isPlay) {
        this.isPlay = isPlay;
    }
}
