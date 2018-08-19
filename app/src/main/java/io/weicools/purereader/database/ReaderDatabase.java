package io.weicools.purereader.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import io.weicools.purereader.PureReaderApp;
import io.weicools.purereader.data.GankContent;
import io.weicools.purereader.data.SearchHistory;
import io.weicools.purereader.database.dao.GankContentDao;
import io.weicools.purereader.database.dao.SearchHistoryDao;

/**
 * @author Weicools Create on 2018.08.17
 *
 * desc:
 */
@Database(entities = {GankContent.class, SearchHistory.class}, version = 1)
public abstract class ReaderDatabase extends RoomDatabase {
  private static volatile ReaderDatabase INSTANCE;

  public abstract GankContentDao contentDao ();

  public abstract SearchHistoryDao historyDao ();

  public static ReaderDatabase getInstance () {
    if (INSTANCE == null) {
      synchronized (ReaderDatabase.class) {
        if (INSTANCE == null) {
          INSTANCE = Room.databaseBuilder(PureReaderApp.getAppInstance(), ReaderDatabase.class, "Reader.db").build();
        }
      }
    }
    return INSTANCE;
  }
}
