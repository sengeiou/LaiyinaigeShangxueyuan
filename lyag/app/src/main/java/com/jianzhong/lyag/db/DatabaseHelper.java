package com.jianzhong.lyag.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.jianzhong.lyag.model.AudioRecorModel;
import com.jianzhong.lyag.model.CourseAudioRecorModel;
import com.jianzhong.lyag.model.CourseRecorModel;
import com.jianzhong.lyag.model.LiveRecorModel;
import com.jianzhong.lyag.model.SearchRecordModel;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

    private static final String TABLE_NAME = "lyag.db";

    private DatabaseHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        try {
            TableUtils.createTable(connectionSource, SearchRecordModel.class);
            TableUtils.createTable(connectionSource, AudioRecorModel.class);
            TableUtils.createTable(connectionSource, CourseRecorModel.class);
            TableUtils.createTable(connectionSource, CourseAudioRecorModel.class);
            TableUtils.createTable(connectionSource, LiveRecorModel.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        try {
            TableUtils.dropTable(connectionSource, SearchRecordModel.class, true);
            TableUtils.dropTable(connectionSource, AudioRecorModel.class, true);
            TableUtils.dropTable(connectionSource, CourseRecorModel.class, true);
            TableUtils.dropTable(connectionSource, CourseAudioRecorModel.class, true);
            TableUtils.dropTable(connectionSource, LiveRecorModel.class, true);
            onCreate(database, connectionSource);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static DatabaseHelper instance;

    /**
     * 单例获取该Helper
     *
     * @param context
     * @return
     */
    public static synchronized DatabaseHelper getHelper(Context context) {
        context = context.getApplicationContext();
        if (instance == null) {
            synchronized (DatabaseHelper.class) {
                if (instance == null)
                    instance = new DatabaseHelper(context);
            }
        }

        return instance;
    }
}
