package com.kz.jv.jollyvolly.tabs.news;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

/**
 * Created by madiyarzhenis on 21.09.15.
 */
public class StoreDatabaseNews extends SQLiteOpenHelper implements BaseColumns {

    public static final String DATABASE_NAME = "news.db";
    public static final int DATABASE_VERSION = 10;
    public static String SQL_CREATE_ENTRIES =
            "CREATE TABLE news (" +
                    "objectId VARCHAR(50)" +
                    ",title VARCHAR(50)" +
                    ",description VARCHAR(50)" +
                    ",imageUrl VARCHAR(50)" +
                    ",likeCount VARCHAR(50), " +
                    "liked int(50));";

    public static String SQL_DELETE_ENTRIES = "DROP TABLE IF EXISTS news";
    public static String SQL_CLEAN_ENTRIES = "DELETE from news";

    public StoreDatabaseNews(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    public void cleanTable(SQLiteDatabase db) {
        Log.i("info", "table information");
        db.execSQL(SQL_CLEAN_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w("LOG_TAG", "Обновление базы данных с версии " + oldVersion
                + " до версии " + newVersion + ", которое удалит все старые данные");
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
