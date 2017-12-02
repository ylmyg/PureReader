package io.weicools.purereader.data.local;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import io.weicools.purereader.data.ZhihuDailyContent;
import io.weicools.purereader.data.datasource.ZhihuDailyContentDataSource;
import io.weicools.purereader.database.AppDatabase;
import io.weicools.purereader.database.DatabaseCreator;

/**
 * Created by Weicools on 2017/12/2.
 * <p>
 * Concrete implementation of a {@link ZhihuDailyContent} data source as database.
 */

public class ZhihuDailyContentLocalDataSource implements ZhihuDailyContentDataSource {
    @Nullable
    private AppDatabase mDatabase;

    @Nullable
    private static ZhihuDailyContentLocalDataSource sInstance = null;

    public static ZhihuDailyContentLocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ZhihuDailyContentLocalDataSource(context);
        }

        return sInstance;
    }

    private ZhihuDailyContentLocalDataSource(Context context) {
        DatabaseCreator creator = DatabaseCreator.getInstance();
        if (!creator.isDatabaseCreated()) {
            creator.createDb(context);
        }
        mDatabase = creator.getDatabase();
    }

    @SuppressLint("StaticFieldLeak")
    @Override
    public void getZhihuDailyContent(final int id, @NonNull final LoadZhihuDailyContentCallback contentCallback) {
        if (mDatabase == null) {
            contentCallback.onDataNotAvailable();
            return;
        }

        new AsyncTask<Void, Void, ZhihuDailyContent>() {

            @Override
            protected ZhihuDailyContent doInBackground(Void... voids) {
                return mDatabase.zhihuDailyContentDao().queryContentById(id);
            }

            @Override
            protected void onPostExecute(ZhihuDailyContent content) {
                super.onPostExecute(content);
                if (content == null) {
                    contentCallback.onDataNotAvailable();
                } else {
                    contentCallback.onContentLoaded(content);
                }
            }

        }.execute();
    }

    @Override
    public void saveContent(@NonNull final ZhihuDailyContent content) {
        if (mDatabase == null) {
            mDatabase = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDatabase != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    mDatabase.beginTransaction();
                    try {
                        mDatabase.zhihuDailyContentDao().insert(content);
                        mDatabase.setTransactionSuccessful();
                    } finally {
                        mDatabase.endTransaction();
                    }
                }
            }).start();
        }
    }
}
