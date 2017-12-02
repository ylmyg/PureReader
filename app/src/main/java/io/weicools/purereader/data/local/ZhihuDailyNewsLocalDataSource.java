package io.weicools.purereader.data.local;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.weicools.purereader.data.ZhihuDailyNewsQuestion;
import io.weicools.purereader.data.datasource.ZhihuDailyNewsDataSource;
import io.weicools.purereader.database.AppDatabase;
import io.weicools.purereader.database.DatabaseCreator;

/**
 * Created by Weicools on 2017/12/2.
 * <p>
 * Concrete implementation of a {@link ZhihuDailyNewsQuestion} data source as database.
 */

public class ZhihuDailyNewsLocalDataSource implements ZhihuDailyNewsDataSource {
    @Nullable
    private AppDatabase mDatabase = null;

    @Nullable
    private static ZhihuDailyNewsLocalDataSource sInstance;

    public static ZhihuDailyNewsLocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ZhihuDailyNewsLocalDataSource(context);
        }
        return sInstance;
    }

    private ZhihuDailyNewsLocalDataSource(Context context) {
        DatabaseCreator creator = DatabaseCreator.getInstance();
        if (!creator.isDatabaseCreated()) {
            creator.createDb(context);
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getZhihuDailyNews(boolean forceUpdate, boolean cleanCache, final long date, @NonNull final LoadZhihuDailyNewsCallback callback) {
        if (mDatabase == null) {
            mDatabase = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDatabase != null) {
            new AsyncTask<Void, Void, List<ZhihuDailyNewsQuestion>>() {
                @Override
                protected List<ZhihuDailyNewsQuestion> doInBackground(Void... voids) {
                    return mDatabase.zhihuDailyNewsDao().queryAllByDate(date);
                }

                @Override
                protected void onPostExecute(List<ZhihuDailyNewsQuestion> list) {
                    super.onPostExecute(list);
                    if (list == null) {
                        callback.onDataNotAvailable();
                    } else {
                        callback.onNewsLoaded(list);
                    }
                }
            }.execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getFavorites(@NonNull final LoadZhihuDailyNewsCallback callback) {
        if (mDatabase == null) {
            mDatabase = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDatabase != null) {
            new AsyncTask<Void, Void, List<ZhihuDailyNewsQuestion>>() {
                @Override
                protected List<ZhihuDailyNewsQuestion> doInBackground(Void... voids) {
                    return mDatabase.zhihuDailyNewsDao().queryAllFavorites();
                }

                @Override
                protected void onPostExecute(List<ZhihuDailyNewsQuestion> list) {
                    super.onPostExecute(list);
                    if (list == null) {
                        callback.onDataNotAvailable();
                    } else {
                        callback.onNewsLoaded(list);
                    }
                }
            }.execute();
        }
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getItem(final int itemId, @NonNull final GetNewsItemCallback callback) {
        if (mDatabase == null) {
            mDatabase = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDatabase != null) {
            new AsyncTask<Void, Void, ZhihuDailyNewsQuestion>() {
                @Override
                protected ZhihuDailyNewsQuestion doInBackground(Void... voids) {
                    return mDatabase.zhihuDailyNewsDao().queryItemById(itemId);
                }

                @Override
                protected void onPostExecute(ZhihuDailyNewsQuestion item) {
                    super.onPostExecute(item);
                    if (item == null) {
                        callback.onDataNotAvailable();
                    } else {
                        callback.onItemLoaded(item);
                    }
                }
            }.execute();
        }
    }

    @Override
    public void favoriteItem(final int itemId, final boolean favorite) {
        if (mDatabase == null) {
            mDatabase = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDatabase != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ZhihuDailyNewsQuestion tmp = mDatabase.zhihuDailyNewsDao().queryItemById(itemId);
                    tmp.setFavorite(favorite);
                    mDatabase.zhihuDailyNewsDao().update(tmp);
                }
            }).start();
        }
    }

    @Override
    public void saveAll(@NonNull final List<ZhihuDailyNewsQuestion> list) {
        if (mDatabase == null) {
            mDatabase = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDatabase != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mDatabase.beginTransaction();
                    try {
                        mDatabase.zhihuDailyNewsDao().insertAll(list);
                        mDatabase.setTransactionSuccessful();
                    } finally {
                        mDatabase.endTransaction();
                    }
                }
            }).start();
        }
    }
}
