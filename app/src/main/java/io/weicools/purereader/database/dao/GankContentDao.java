package io.weicools.purereader.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import io.reactivex.Flowable;
import io.weicools.purereader.data.GankContent;
import java.util.List;

/**
 * @author Weicools Create on 2018.08.17
 *
 * desc:
 */
@Dao
public interface GankContentDao {
  /**
   * @return search list
   */
  @Query("SELECT * FROM gank_content")
  Flowable<List<GankContent>> getFavoriteList ();

  @Query("SELECT * FROM gank_content WHERE _id = :id")
  Flowable<GankContent> getFavorite (String id);

  /**
   * save gank content
   *
   * @param content content
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertGankContent (GankContent content);

  @Delete
  void deleteGankContent (GankContent content);
}
