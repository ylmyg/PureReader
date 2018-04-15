package io.weicools.purereader.data.local;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
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
    private CompositeDisposable mDisposable;

    @Nullable
    private static ZhihuDailyNewsLocalDataSource sInstance;

    public static ZhihuDailyNewsLocalDataSource getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new ZhihuDailyNewsLocalDataSource(context);
        }
        return sInstance;
    }

    public void destroyInstance() {
        mDisposable.clear();
        sInstance = null;
    }

    private ZhihuDailyNewsLocalDataSource(Context context) {
        mDisposable = new CompositeDisposable();
        DatabaseCreator creator = DatabaseCreator.getInstance();
        if (!creator.isDatabaseCreated()) {
            creator.createDb(context);
        }
    }

    @Override
    public void getZhihuDailyNews(boolean forceUpdate, boolean cleanCache, final long date, @NonNull final LoadZhihuDailyNewsCallback callback) {
        if (mDatabase == null) {
            mDatabase = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDatabase != null) {
            mDisposable.add(mDatabase.zhihuDailyNewsDao().queryAllByDate(date)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<ZhihuDailyNewsQuestion>>() {
                        @Override
                        public void accept(List<ZhihuDailyNewsQuestion> list) throws Exception {
                            if (list == null) {
                                callback.onDataNotAvailable();
                            } else {
                                callback.onNewsLoaded(list);
                            }
                        }
                    }));
        }
    }

    @Override
    public void getFavorites(@NonNull final LoadZhihuDailyNewsCallback callback) {
        if (mDatabase == null) {
            mDatabase = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDatabase != null) {
            mDisposable.add(mDatabase.zhihuDailyNewsDao().queryAllFavorites()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<List<ZhihuDailyNewsQuestion>>() {
                        @Override
                        public void accept(List<ZhihuDailyNewsQuestion> list) throws Exception {
                            if (list == null) {
                                callback.onDataNotAvailable();
                            } else {
                                callback.onNewsLoaded(list);
                            }
                        }
                    }));
        }
    }

    @Override
    public void getItem(final int itemId, @NonNull final GetNewsItemCallback callback) {
        if (mDatabase == null) {
            mDatabase = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDatabase != null) {
            mDisposable.add(mDatabase.zhihuDailyNewsDao().queryItemById(itemId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ZhihuDailyNewsQuestion>() {
                        @Override
                        public void accept(ZhihuDailyNewsQuestion item) throws Exception {
                            if (item == null) {
                                callback.onDataNotAvailable();
                            } else {
                                callback.onItemLoaded(item);
                            }
                        }
                    }));
        }
    }

    @Override
    public void favoriteItem(final int itemId, final boolean favorite) {
        if (mDatabase == null) {
            mDatabase = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDatabase != null) {
            mDisposable.add(mDatabase.zhihuDailyNewsDao().queryItemById(itemId)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(new Consumer<ZhihuDailyNewsQuestion>() {
                        @Override
                        public void accept(ZhihuDailyNewsQuestion temp) throws Exception {
                            temp.setFavorite(favorite);
                            mDatabase.zhihuDailyNewsDao().update(temp);
                        }
                    }));
        }
    }

    @Override
    public void saveAll(@NonNull final List<ZhihuDailyNewsQuestion> list) {
        if (mDatabase == null) {
            mDatabase = DatabaseCreator.getInstance().getDatabase();
        }

        if (mDatabase != null) {
            mDisposable.add(Completable.fromAction(new Action() {
                @Override
                public void run() throws Exception {
                    mDatabase.beginTransaction();
                    try {
                        mDatabase.zhihuDailyNewsDao().insertAll(list);
                        mDatabase.setTransactionSuccessful();
                    } finally {
                        mDatabase.endTransaction();
                    }
                }
            }).subscribeOn(Schedulers.io()).subscribe());
        }
    }
}
