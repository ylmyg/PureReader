package io.weicools.purereader.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import io.weicools.purereader.data.ZhihuDailyContent;
import io.weicools.purereader.data.ZhihuDailyNewsQuestion;
import io.weicools.purereader.database.dao.ZhihuDailyContentDao;
import io.weicools.purereader.database.dao.ZhihuDailyNewsDao;

/**
 * Create by Weicools on 2017/12/2.
 * <p>
 * Main database description.
 */

@Database(entities = {
        ZhihuDailyNewsQuestion.class,
        ZhihuDailyContent.class},
        version = 1)
public abstract class AppDatabase extends RoomDatabase {
    static final String DATABASE_NAME = "pure-reader-db";

    public abstract ZhihuDailyNewsDao zhihuDailyNewsDao();

    public abstract ZhihuDailyContentDao zhihuDailyContentDao();
}
