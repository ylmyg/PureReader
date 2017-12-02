package io.weicools.purereader.database;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.util.Log;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Create by Weicools on 2017/12/2.
 * <p>
 * Create the {@link AppDatabase} asynchronously.
 */

public class DatabaseCreator {
    private AppDatabase mDb;

    private final AtomicBoolean mInitializing = new AtomicBoolean(true);
    private final AtomicBoolean mIsDbCreated = new AtomicBoolean(false);

    // For Singleton instantiation
    private static final Object LOCK = new Object();

    @Nullable
    private static DatabaseCreator sInstance = null;

    private DatabaseCreator() {
    }

    public static DatabaseCreator getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                if (sInstance == null) {
                    sInstance = new DatabaseCreator();
                }
            }
        }

        return sInstance;
    }

    @SuppressLint("StaticFieldLeak")
    public void createDb(Context context) {
        Log.d("DatabaseCreator", "Creating DB from " + Thread.currentThread().getName());

        if (!mInitializing.compareAndSet(true, false)) {
            return;
        }

        new AsyncTask<Context, Void, Void>() {

            @Override
            protected Void doInBackground(Context... contexts) {
                Log.d("DatabaseCreator", "Starting bg job " + Thread.currentThread().getName());

                Context ctx = contexts[0].getApplicationContext();

                mDb = Room.databaseBuilder(ctx, AppDatabase.class, AppDatabase.DATABASE_NAME).build();

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                mIsDbCreated.set(true);
            }
        }.execute(context.getApplicationContext());
    }

    public boolean isDatabaseCreated() {
        return mIsDbCreated.get();
    }

    @Nullable
    public AppDatabase getDatabase() {
        return mDb;
    }
}
