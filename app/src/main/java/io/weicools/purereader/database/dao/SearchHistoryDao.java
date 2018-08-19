package io.weicools.purereader.database.dao;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import io.reactivex.Flowable;
import io.weicools.purereader.data.SearchHistory;
import java.util.List;

/**
 * @author Weicools Create on 2018.08.17
 *
 * desc:
 */
@Dao
public interface SearchHistoryDao {
  /**
   * @return search list
   */
  @Query("SELECT keyword FROM search_history")
  Flowable<List<String>> getHistoryKeyword();

  /**
   * save search history
   * @param history history
   */
  @Insert(onConflict = OnConflictStrategy.REPLACE)
  void insertSearchHistory(SearchHistory history);

  /**
   * Delete all users.
   */
  @Query("DELETE FROM search_history")
  void deleteAllHistory();
}
