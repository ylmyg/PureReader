package io.weicools.purereader.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import io.reactivex.Flowable;
import io.weicools.purereader.data.ZhihuDailyNews;
import io.weicools.purereader.data.ZhihuDailyNewsQuestion;

/**
 * Create by Weicools on 2017/12/2.
 * <p>
 * Interface for database access on {@link ZhihuDailyNews} related operations.
 */

@Dao
public interface ZhihuDailyNewsDao {
    @Query("SELECT * FROM zhihu_daily_news WHERE timestamp BETWEEN (:timestamp - 24*60*60*1000 + 1) AND :timestamp ORDER BY timestamp ASC")
    Flowable<List<ZhihuDailyNewsQuestion>> queryAllByDate(long timestamp);

    @Query("SELECT * FROM zhihu_daily_news WHERE id = :id")
    Flowable<ZhihuDailyNewsQuestion> queryItemById(int id);

    @Query("SELECT * FROM zhihu_daily_news WHERE favorite = 1")
    Flowable<List<ZhihuDailyNewsQuestion>> queryAllFavorites();

    @Query("SELECT * FROM zhihu_daily_news WHERE (timestamp < :timestamp) AND (favorite = 0)")
    Flowable<List<ZhihuDailyNewsQuestion>> queryAllTimeoutItems(long timestamp);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(List<ZhihuDailyNewsQuestion> items);

    @Update
    void update(ZhihuDailyNewsQuestion item);

    @Delete
    void delete(ZhihuDailyNewsQuestion item);
}
