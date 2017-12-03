package io.weicools.purereader.data.repository;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import io.weicools.purereader.data.ZhihuDailyNewsQuestion;
import io.weicools.purereader.data.datasource.ZhihuDailyNewsDataSource;
import io.weicools.purereader.data.local.ZhihuDailyNewsLocalDataSource;
import io.weicools.purereader.data.remote.ZhihuDailyNewsRemoteDataSource;

/**
 * Create by weicools on 2017/12/3.
 * <p>
 * Concrete implementation to load {@link ZhihuDailyNewsQuestion}s from the data sources into a cache.
 * <p>
 * Use the remote data source firstly, which is obtained from the server.
 * If the remote data was not available, then use the local data source,
 * which was from the locally persisted in database.
 */

public class ZhihuDailyNewsRepository implements ZhihuDailyNewsDataSource {
    @NonNull
    private final ZhihuDailyNewsLocalDataSource mLocalDataSource;
    @NonNull
    private final ZhihuDailyNewsRemoteDataSource mRemoteDataSource;

    private Map<Integer, ZhihuDailyNewsQuestion> mCachedItems;

    @Nullable
    private static ZhihuDailyNewsRepository sInstance = null;

    // Prevent direct instantiation.
    private ZhihuDailyNewsRepository(@NonNull ZhihuDailyNewsLocalDataSource localDataSource,
                                     @NonNull ZhihuDailyNewsRemoteDataSource remoteDataSource) {
        this.mLocalDataSource = localDataSource;
        this.mRemoteDataSource = remoteDataSource;
    }

    @NonNull
    public static ZhihuDailyNewsRepository getInstance(@NonNull ZhihuDailyNewsLocalDataSource localDataSource,
                                                       @NonNull ZhihuDailyNewsRemoteDataSource remoteDataSource) {
        if (sInstance == null) {
            sInstance = new ZhihuDailyNewsRepository(localDataSource, remoteDataSource);
        }
        return sInstance;
    }

    public static void destroyInstance() {
        sInstance = null;
    }

    private void refreshCache(boolean clearCache, List<ZhihuDailyNewsQuestion> list) {
        if (mCachedItems == null) {
            mCachedItems = new LinkedHashMap<>();
        }
        if (clearCache) {
            mCachedItems.clear();
        }
        for (ZhihuDailyNewsQuestion item : list) {
            mCachedItems.put(item.getId(), item);
        }
    }

    @Override
    public void getZhihuDailyNews(boolean forceUpdate, final boolean cleanCache, final long date,
                                  @NonNull final LoadZhihuDailyNewsCallback callback) {
        if (mCachedItems != null && !forceUpdate) {
            callback.onNewsLoaded(new ArrayList<ZhihuDailyNewsQuestion>(mCachedItems.values()));
            return;
        }

        // Get data by accessing network first.
        mRemoteDataSource.getZhihuDailyNews(false, cleanCache, date, new LoadZhihuDailyNewsCallback() {
            @Override
            public void onNewsLoaded(@NonNull List<ZhihuDailyNewsQuestion> list) {
                refreshCache(cleanCache, list);
                callback.onNewsLoaded(new ArrayList<ZhihuDailyNewsQuestion>(list));
                // Save these item to database.
                saveAll(list);
            }

            @Override
            public void onDataNotAvailable() {
                mLocalDataSource.getZhihuDailyNews(false, false, date, new LoadZhihuDailyNewsCallback() {
                    @Override
                    public void onNewsLoaded(@NonNull List<ZhihuDailyNewsQuestion> list) {
                        refreshCache(cleanCache, list);
                        callback.onNewsLoaded(new ArrayList<>(mCachedItems.values()));
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void getFavorites(@NonNull final LoadZhihuDailyNewsCallback callback) {
        mLocalDataSource.getFavorites(new LoadZhihuDailyNewsCallback() {
            @Override
            public void onNewsLoaded(@NonNull List<ZhihuDailyNewsQuestion> list) {
                callback.onNewsLoaded(list);
            }

            @Override
            public void onDataNotAvailable() {
                callback.onDataNotAvailable();
            }
        });
    }

    @Override
    public void getItem(final int itemId, @NonNull final GetNewsItemCallback callback) {
        ZhihuDailyNewsQuestion cachedItem = getItemWithId(itemId);

        if (cachedItem != null) {
            callback.onItemLoaded(cachedItem);
            return;
        }

        mLocalDataSource.getItem(itemId, new GetNewsItemCallback() {
            @Override
            public void onItemLoaded(@NonNull ZhihuDailyNewsQuestion item) {
                if (mCachedItems == null) {
                    mCachedItems = new LinkedHashMap<>();
                }
                mCachedItems.put(item.getId(), item);
                callback.onItemLoaded(item);
            }

            @Override
            public void onDataNotAvailable() {
                mRemoteDataSource.getItem(itemId, new GetNewsItemCallback() {
                    @Override
                    public void onItemLoaded(@NonNull ZhihuDailyNewsQuestion item) {
                        if (mCachedItems == null) {
                            mCachedItems = new LinkedHashMap<>();
                        }
                        mCachedItems.put(item.getId(), item);
                        callback.onItemLoaded(item);
                    }

                    @Override
                    public void onDataNotAvailable() {
                        callback.onDataNotAvailable();
                    }
                });
            }
        });
    }

    @Override
    public void favoriteItem(int itemId, boolean favorite) {
        mRemoteDataSource.favoriteItem(itemId, favorite);
        mLocalDataSource.favoriteItem(itemId, favorite);

        ZhihuDailyNewsQuestion cachedItem = getItemWithId(itemId);
        if (cachedItem != null) {
            cachedItem.setFavorite(favorite);
        }
    }

    @Override
    public void saveAll(@NonNull List<ZhihuDailyNewsQuestion> list) {
        mLocalDataSource.saveAll(list);
        mRemoteDataSource.saveAll(list);

        if (mCachedItems == null) {
            mCachedItems = new LinkedHashMap<>();
        }

        for (ZhihuDailyNewsQuestion item : list) {
            // Note:  Setting of timestamp was done in the {@link ZhihuDailyNewsRemoteDataSource} class.
            mCachedItems.put(item.getId(), item);
        }
    }

    @Nullable
    private ZhihuDailyNewsQuestion getItemWithId(int id) {
        return (mCachedItems == null || mCachedItems.isEmpty()) ? null : mCachedItems.get(id);
    }
}
