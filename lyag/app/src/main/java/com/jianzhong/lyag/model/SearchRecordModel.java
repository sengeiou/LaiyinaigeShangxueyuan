package com.jianzhong.lyag.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by zhengwencheng on 2018/1/27.
 * E-mail 15889964542@163.com
 *
 * 搜索记录model
 */
@DatabaseTable(tableName = "tb_search")
public class SearchRecordModel {

    @DatabaseField(generatedId = true)
    private int id;
    @DatabaseField(columnName = "search_record")
    private String search_record;

    @DatabaseField(columnName = "user_id")
    private String user_id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSearch_record() {
        return search_record;
    }

    public void setSearch_record(String search_record) {
        this.search_record = search_record;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
