package com.jianzhong.lyag.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.jianzhong.lyag.db.DatabaseHelper;
import com.jianzhong.lyag.model.LiveRecorModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengwencheng on 2018/1/27.
 * E-mail 15889964542@163.com
 *
 * 直播回放播放记录的dao
 */
public class LiveRecordDao {
    private DatabaseHelper helper;
    private Dao<LiveRecorModel,Integer> dao;

    public LiveRecordDao(Context context){
        try {
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(LiveRecorModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //批量插入数据
    public int insertList(List<LiveRecorModel> models){
        try {
            return dao.create(models);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //插入单条数据
    public int insert(LiveRecorModel model){
        try {
            return dao.create(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //删除某一条数据
    public int delete(LiveRecorModel model){
        try {
            return dao.delete(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //删除全部数据
    public int deleteForAll(List<LiveRecorModel> models){
        try {
            return dao.delete(models);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //更改数据
    public int update(LiveRecorModel model){
        try {
            return dao.update(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //查询数据 这里只查询全部 因为只有keyword
    public List<LiveRecorModel> queryForAll(){
        List<LiveRecorModel> list = null;
        try {
            list = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    //查询数据 这里只查一条
    public List<LiveRecorModel> queryForValue(Map<String,Object> map){
        List<LiveRecorModel> list = null;
        try {
            list = dao.queryForFieldValues(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
