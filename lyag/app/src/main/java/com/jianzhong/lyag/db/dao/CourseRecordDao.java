package com.jianzhong.lyag.db.dao;

import android.content.Context;

import com.j256.ormlite.dao.Dao;
import com.jianzhong.lyag.db.DatabaseHelper;
import com.jianzhong.lyag.model.CourseRecorModel;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by zhengwencheng on 2018/1/27.
 * E-mail 15889964542@163.com
 *
 * 课程播放记录的dao
 */
public class CourseRecordDao {
    private DatabaseHelper helper;
    private Dao<CourseRecorModel,Integer> dao;

    public CourseRecordDao(Context context){
        try {
            helper = DatabaseHelper.getHelper(context);
            dao = helper.getDao(CourseRecorModel.class);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    //批量插入数据
    public int insertList(List<CourseRecorModel> models){
        try {
            return dao.create(models);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //插入单条数据
    public int insert(CourseRecorModel model){
        try {
            return dao.create(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //删除某一条数据
    public int delete(CourseRecorModel model){
        try {
            return dao.delete(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //删除全部数据
    public int deleteForAll(List<CourseRecorModel> models){
        try {
            return dao.delete(models);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }
    //更改数据
    public int update(CourseRecorModel model){
        try {
            return dao.update(model);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    //查询数据 这里只查询全部 因为只有keyword
    public List<CourseRecorModel> queryForAll(){
        List<CourseRecorModel> list = null;
        try {
            list = dao.queryForAll();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }


    //查询数据 这里只查一条
    public List<CourseRecorModel> queryForValue(Map<String,Object> map){
        List<CourseRecorModel> list = null;
        try {
            list = dao.queryForFieldValues(map);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }
}
